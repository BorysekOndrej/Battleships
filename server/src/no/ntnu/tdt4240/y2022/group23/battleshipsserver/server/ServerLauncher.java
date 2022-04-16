package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

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

	private static final Logger logger = LoggerFactory.getLogger(ServerLauncher.class);

	public static void join_matchmaking(Context ctx) {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = ctx.formParamAsClass("userID", String.class).get();

		String privateLobby = ctx.formParam("privateLobby");
		assert "false".equals(privateLobby);

		redisStorage.addUserToMatchmakingQueue(userID);
		ctx.status(200).result("Added to matchmaking set");

		ImmutablePair<String, String> twoUsersFromMatchmaking = redisStorage.getTwoUsersFromMatchmaking();
		if (twoUsersFromMatchmaking != null){
			Lobby lobby = new Lobby(false);
			lobby.addUser(twoUsersFromMatchmaking.left);
			lobby.addUser(twoUsersFromMatchmaking.right); // adding second player starts the game
		}
	}


	public static void placements(Context ctx) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = ctx.formParam("userID");
		ShipPlacements userPlacements = StringSerializer.fromString(ctx.formParam("placements"));

		redisStorage.setShipPlacements(userID, userPlacements);

		String opponentID = redisStorage.getOpponentId(userID);
		ShipPlacements opponentPlacements = redisStorage.getUserShipPlacements(opponentID);

		if (opponentPlacements != null) {
			redisStorage.setUserGameBoard(userID, new GameBoard(GAME_BOARD_WIDTH, GAME_BOARD_HEIGHT));
			redisStorage.setUserGameBoard(opponentID, new GameBoard(GAME_BOARD_WIDTH, GAME_BOARD_HEIGHT));

			Message.Builder messageBuilder = Message.builder()
					.putData("type", ServerClientMessage.GAME_START.name());

			FirebaseMessenger.sendMessageUsingMsgBuilder(messageBuilder, userID);
			// DANGER messageBuilder may have been altered by the previous call?
			FirebaseMessenger.sendMessageUsingMsgBuilder(messageBuilder, opponentID);
		}
	}

	public static void action(Context ctx) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = ctx.formParam("userID");
		IAction action = StringSerializer.fromString(ctx.formParam("action"));

		String opponentID = redisStorage.getOpponentId(userID);
		GameBoard opponentBoard = redisStorage.getUserGameBoard(opponentID);
		ShipPlacements shipPlacements = redisStorage.getUserShipPlacements(opponentID);

		TurnEvaluator evaluator = new TurnEvaluator(shipPlacements, opponentBoard, action);

		ServerClientMessage type = evaluator.nextTurn() == NextTurn.GAME_OVER ? ServerClientMessage.GAME_OVER : ServerClientMessage.ACTION_PERFORMED;
		GameBoard board = evaluator.boardAfterTurn();
		ArrayList<GameBoardChange> changedCoords = new ArrayList<>(evaluator.getChangedCoords());
		ArrayList<IShip> unsunkShips = new ArrayList<>(shipPlacements.getUnsunkShipsDisplaced(board));

		Message.Builder messageBuilder = Message.builder()
				.putData("type", type.name())
				.putData("board", StringSerializer.toString(board))
				.putData("changedCoords", StringSerializer.toString(changedCoords))
				.putData("unsunkShips", StringSerializer.toString(unsunkShips));

		FirebaseMessenger.sendMessageUsingMsgBuilder(messageBuilder, userID);
		FirebaseMessenger.sendMessageUsingMsgBuilder(messageBuilder, opponentID);
	}

	public static void timeout(Context ctx) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = ctx.formParam("userID");

		String opponentID = redisStorage.getOpponentId(userID);
		GameBoard opponentBoard = redisStorage.getUserGameBoard(opponentID);
		ShipPlacements shipPlacements = redisStorage.getUserShipPlacements(opponentID);

		ServerClientMessage type = ServerClientMessage.ACTION_PERFORMED;
		ArrayList<GameBoardChange> changedCoords = new ArrayList<>();
		ArrayList<IShip> unsunkShips = new ArrayList<>(shipPlacements.getUnsunkShipsDisplaced(opponentBoard));

		Message.Builder messageBuilder = Message.builder()
				.putData("type", type.name())
				.putData("board", StringSerializer.toString(opponentBoard))
				.putData("changedCoords", StringSerializer.toString(changedCoords))
				.putData("unsunkShips", StringSerializer.toString(unsunkShips));

		FirebaseMessenger.sendMessageUsingMsgBuilder(messageBuilder, userID);
		FirebaseMessenger.sendMessageUsingMsgBuilder(messageBuilder, opponentID);
	}

	public static void terminate(Context ctx) throws FirebaseMessagingException {
		String userID = ctx.formParamAsClass("userID", String.class).get();

		Lobby lobby = Lobby.getLobby(Lobby.findGameID(userID));
		if (lobby == null){
			return; // todo:
		}
		lobby.endCommunication(userID);

		ctx.status(200).result("Ended communication and informed the other player");
	}

	public static void create_lobby(Context ctx) {
		String userID = ctx.formParamAsClass("userID", String.class).get();

		String privateLobby = ctx.formParam("privateLobby");
		assert "true".equals(privateLobby);

		Lobby lobby = new Lobby(true);
		lobby.addUser(userID); // todo: handle exceptions
		ctx.status(200).result("Lobby created with id "+lobby.gameID+ " and you've been invited.");
	}


	public static void join_lobby(Context ctx) {
		String userID = ctx.formParamAsClass("userID", String.class).get();
		String lobbyID = ctx.formParamAsClass("id", String.class).get();

		Lobby lobby = Lobby.getLobby(lobbyID);
		if (lobby == null){
			FirebaseMessenger.sendMessage(userID, ServerClientMessage.NO_SUCH_LOBBY, null);
			ctx.status(404).result("Lobby doesn't exist");
			return;
		}

		lobby.addUser(userID); // todo: handle exceptions
		ctx.status(200).result("User added to Lobby");
	}

	public static void main (String[] arg) {
		logger.info("Server project started");
		Javalin app = Javalin.create().start(7070);
		RedisStorage redisStorage = RedisStorage.getInstance();


		app.get("/", ctx -> ctx.result("Hello World"));

		app.post("/token", ctx -> {
			String userID = ctx.formParamAsClass("userID", String.class).get(); // security: todo: make sure we have some rate limiting
			String token = ctx.formParamAsClass("token", String.class).get();

			redisStorage.setNewUserToken(userID, token);
			ctx.status(200).result("Hello " + token + " " + userID);
		});

		app.post("/placements", ServerLauncher::placements);
		app.post("/action", ServerLauncher::action);
		app.post("/timeout", ServerLauncher::timeout);
		app.post("/terminate", ServerLauncher::terminate);

		app.post("/join_matchmaking", ServerLauncher::join_matchmaking);
		app.post("/join_lobby", ServerLauncher::join_lobby);
		app.post("/create_lobby", ServerLauncher::create_lobby);
	}
}
