package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import io.javalin.Javalin;
import io.javalin.http.Context;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardChange;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.ServerClientMessage;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.StringSerializer;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.TurnEvaluator;

public class ServerLauncher {
	private static final int GAME_BOARD_WIDTH = 10;
	private static final int GAME_BOARD_HEIGHT = 10;

	private static final Logger logger = LogManager.getLogger(ServerLauncher.class);

	public static void matchmaking_add(Context ctx) {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String user_id = ctx.formParamAsClass("user_id", String.class).get();
		redisStorage.addUserToMatchmakingQueue(user_id);
		ctx.status(200).result("Added to matchmaking set");

		ImmutablePair<String, String> twoUsersFromMatchmaking = redisStorage.getTwoUsersFromMatchmaking();
		if (twoUsersFromMatchmaking != null){
			System.out.println(twoUsersFromMatchmaking.left + " " + twoUsersFromMatchmaking.right); // todo: implement
		}
	}

	private static void sendMessage(Message.Builder messageBuilder, String userID) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userToken = redisStorage.getUserTokenByID(userID);
		Message message = messageBuilder.setToken(userToken).build();

		// Send a message to the device corresponding to the provided
		// registration token.
		String response = FirebaseMessaging.getInstance().send(message);
		// Response is a message ID string.
		System.out.println("Successfully sent message: " + response);
	}

	public static void placements(Context ctx) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = ctx.formParam("user_id");
		ShipPlacements userPlacements = StringSerializer.fromString(ctx.formParam("placements"));

		redisStorage.setShipPlacements(userID, userPlacements);

		String opponentID = redisStorage.getOpponentId(userID);
		ShipPlacements opponentPlacements = redisStorage.getUserShipPlacements(opponentID);

		if (opponentPlacements != null) {
			GameBoard userBoard = new GameBoard(GAME_BOARD_WIDTH, GAME_BOARD_HEIGHT);
			GameBoard opponentBoard = new GameBoard(GAME_BOARD_WIDTH, GAME_BOARD_HEIGHT);

			GameBoard userBoardRevealed = new GameBoard(userBoard).reveal(userPlacements);
			GameBoard opponentBoardRevealed = new GameBoard(opponentBoard).reveal(opponentPlacements);

			redisStorage.setUserGameBoard(userID, userBoard);
			redisStorage.setUserGameBoard(opponentID, opponentBoard);

			boolean userStarts = new Random().nextBoolean();

			Message.Builder userMessageBuilder = Message.builder()
					.putData("type", ServerClientMessage.GAME_START.name())
					.putData("myBoard", StringSerializer.toString(userBoardRevealed))
					.putData("opponentBoard", StringSerializer.toString(opponentBoard))
					.putData("nextTurn", (userStarts ? NextTurn.MY_TURN : NextTurn.OTHERS_TURN).name());

			sendMessage(userMessageBuilder, userID);

			Message.Builder opponentMessageBuilder = Message.builder()
					.putData("type", ServerClientMessage.GAME_START.name())
					.putData("myBoard", StringSerializer.toString(opponentBoardRevealed))
					.putData("opponentBoard", StringSerializer.toString(userBoard))
					.putData("nextTurn", (userStarts ? NextTurn.OTHERS_TURN : NextTurn.MY_TURN).name());

			sendMessage(opponentMessageBuilder, opponentID);
		}
	}

	public static void action(Context ctx) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = ctx.formParam("user_id");
		IAction action = StringSerializer.fromString(ctx.formParam("action"));

		String opponentID = redisStorage.getOpponentId(userID);
		GameBoard opponentBoard = redisStorage.getUserGameBoard(opponentID);
		ShipPlacements opponentPlacements = redisStorage.getUserShipPlacements(opponentID);

		TurnEvaluator evaluator = new TurnEvaluator(opponentPlacements, opponentBoard, action);

		ServerClientMessage type = evaluator.nextTurn() == NextTurn.GAME_OVER ? ServerClientMessage.GAME_OVER : ServerClientMessage.ACTION_PERFORMED;
		GameBoard opponentBoardAfter = evaluator.boardAfterTurn();
		ArrayList<GameBoardChange> changedCoords = new ArrayList<>(evaluator.getChangedCoords());
		ArrayList<IShip> unsunkShips = new ArrayList<>(opponentPlacements.getUnsunkShipsDisplaced(opponentBoardAfter));
		NextTurn nextTurn = evaluator.nextTurn();

		GameBoard opponentBoardAfterRevealed = new GameBoard(opponentBoardAfter).reveal(opponentPlacements);
		NextTurn switchedTurn = nextTurn == NextTurn.GAME_OVER ?
				NextTurn.GAME_OVER :
				nextTurn == NextTurn.MY_TURN ? NextTurn.OTHERS_TURN : NextTurn.MY_TURN;

		Message.Builder userMessageBuilder = Message.builder()
				.putData("type", type.name())
				.putData("board", StringSerializer.toString(opponentBoardAfter))
				.putData("changedCoords", StringSerializer.toString(changedCoords))
				.putData("unsunkShips", StringSerializer.toString(unsunkShips))
				.putData("nextTurn", nextTurn.name());

		Message.Builder opponentMessageBuilder = Message.builder()
				.putData("type", type.name())
				.putData("board", StringSerializer.toString(opponentBoardAfterRevealed))
				.putData("changedCoords", StringSerializer.toString(changedCoords))
				.putData("unsunkShips", StringSerializer.toString(unsunkShips))
				.putData("nextTurn", switchedTurn.name());

		sendMessage(userMessageBuilder, userID);
		sendMessage(opponentMessageBuilder, opponentID);
	}

	public static void timeout(Context ctx) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = ctx.formParam("user_id");

		String opponentID = redisStorage.getOpponentId(userID);
		GameBoard opponentBoard = redisStorage.getUserGameBoard(opponentID);
		ShipPlacements shipPlacements = redisStorage.getUserShipPlacements(opponentID);
		GameBoard opponentBoardRevealed = new GameBoard(opponentBoard).reveal(shipPlacements);

		ServerClientMessage type = ServerClientMessage.ACTION_PERFORMED;
		ArrayList<GameBoardChange> changedCoords = new ArrayList<>();
		ArrayList<IShip> unsunkShips = new ArrayList<>(shipPlacements.getUnsunkShipsDisplaced(opponentBoard));

		Message.Builder userMessageBuilder = Message.builder()
				.putData("type", type.name())
				.putData("board", StringSerializer.toString(opponentBoard))
				.putData("changedCoords", StringSerializer.toString(changedCoords))
				.putData("unsunkShips", StringSerializer.toString(unsunkShips))
				.putData("nextTurn", NextTurn.OTHERS_TURN.name());

		Message.Builder opponentMessageBuilder = Message.builder()
				.putData("type", type.name())
				.putData("board", StringSerializer.toString(opponentBoardRevealed))
				.putData("changedCoords", StringSerializer.toString(changedCoords))
				.putData("unsunkShips", StringSerializer.toString(unsunkShips))
				.putData("nextTurn", NextTurn.MY_TURN.name());

		sendMessage(userMessageBuilder, userID);
		sendMessage(opponentMessageBuilder, opponentID);
	}

	public static void terminate(Context ctx) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = ctx.formParam("user_id");
		String opponentID = redisStorage.getOpponentId(userID);

		Message.Builder messageBuilder = Message.builder()
				.putData("type", ServerClientMessage.OTHER_ENDED_COMMUNICATION.name());

		sendMessage(messageBuilder, opponentID);
	}

	public static void main (String[] arg) {
		System.out.println("Server project started");
		Javalin app = Javalin.create().start(7070);
		RedisStorage redisStorage = RedisStorage.getInstance();

		try {
			FileInputStream serviceAccount = new FileInputStream("config/java_server/firebaseSecretAccountKey.json");
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();
			FirebaseApp.initializeApp(options);

		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		app.get("/", ctx -> ctx.result("Hello World"));

		app.post("/token", ctx -> {
			String userID = ctx.formParamAsClass("userID", String.class).get(); // security: todo: make sure we have some rate limiting
			String token = ctx.formParamAsClass("token", String.class).get();

			redisStorage.setNewUserToken(userID, token);
			ctx.status(200).result("Hello " + token + " " + userID);
		});

		app.post("/matchmaking_add", ServerLauncher::matchmaking_add);
		app.post("/placements", ServerLauncher::placements);
		app.post("/action", ServerLauncher::action);
		app.post("/timeout", ServerLauncher::timeout);
		app.post("/terminate", ServerLauncher::terminate);

		app.post("/test_firebase_msg", ctx -> {
			// This registration token comes from the client FCM SDKs.
			String userID = ctx.formParamAsClass("userID", String.class).get();
			String firebaseToken = redisStorage.getUserTokenByID(userID);
			if (firebaseToken == null){
				logger.warn("Tried to send firebase message to user "+userID+" which doesn't have registered firebase token. Skipping.");
				return;
			}

			// See documentation on defining a message payload.
			Message message = Message.builder()
					.putData("score", "850")
					.putData("time", "2:45")
					.setToken(firebaseToken)
					.build();

			// Send a message to the device corresponding to the provided
			// registration token.
			String response = FirebaseMessaging.getInstance().send(message);
			// Response is a message ID string.
			System.out.println("Successfully sent message: " + response);

		});

	}
}
