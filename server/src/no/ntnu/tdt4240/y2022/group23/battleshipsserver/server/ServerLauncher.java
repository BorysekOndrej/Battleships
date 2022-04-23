package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import com.google.firebase.messaging.FirebaseMessagingException;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import io.javalin.Javalin;
import io.javalin.http.Context;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Config;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardChange;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
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

	private static String getUserID(Context ctx){
		return ctx.formParamAsClass("userID", String.class).get();
	}

	private static void join_matchmaking(Context ctx) {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = getUserID(ctx);

		String privateLobby = ctx.formParamAsClass("privateLobby", String.class).get();
		String matchmakingQueue = "true".equals(privateLobby) ? "matchmaking_casual" : "matchmaking_ranked";

		redisStorage.addUserToMatchmakingQueue(userID, matchmakingQueue);
		ctx.status(200).result("Added to matchmaking set");

		ImmutablePair<String, String> twoUsersFromMatchmaking = redisStorage.getTwoUsersFromMatchmaking(matchmakingQueue);
		if (twoUsersFromMatchmaking != null){
			Lobby lobby = new Lobby(false);
			lobby.addUser(twoUsersFromMatchmaking.left);
			lobby.addUser(twoUsersFromMatchmaking.right); // adding second player starts the game
		}
	}


	private static void placements(Context ctx) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = getUserID(ctx);
		ShipPlacements userPlacements = StringSerializer.fromString(ctx.formParam("placements"));

		redisStorage.setShipPlacements(userID, userPlacements);

		String opponentID = redisStorage.getOpponentId(userID);
		ShipPlacements opponentPlacements = redisStorage.getUserShipPlacements(opponentID);

		if (opponentPlacements != null) {
			GameBoard userBoard = new GameBoard(Config.GAME_BOARD_WIDTH, Config.GAME_BOARD_HEIGHT);
			GameBoard opponentBoard = new GameBoard(Config.GAME_BOARD_WIDTH, Config.GAME_BOARD_HEIGHT);

			GameBoard userBoardRevealed = new GameBoard(userBoard).reveal(userPlacements, GameBoardField.UNKNOWN);
			GameBoard opponentBoardRevealed = new GameBoard(opponentBoard).reveal(opponentPlacements, GameBoardField.UNKNOWN);

			redisStorage.setUserGameBoard(userID, userBoard);
			redisStorage.setUserGameBoard(opponentID, opponentBoard);

			boolean userStarts = new Random().nextBoolean();

			{
				HashMap<String, String> map1 = new HashMap<>();
				map1.put("myBoard", StringSerializer.toString(userBoardRevealed));
				map1.put("opponentBoard", StringSerializer.toString(opponentBoard));
				map1.put("nextTurn", (userStarts ? NextTurn.MY_TURN : NextTurn.OTHERS_TURN).name());

				FirebaseMessenger.sendMessage(userID, ServerClientMessage.GAME_START, map1);
			}

			{
				HashMap<String, String> map2 = new HashMap<>();
				map2.put("myBoard", StringSerializer.toString(opponentBoardRevealed));
				map2.put("opponentBoard", StringSerializer.toString(userBoard));
				map2.put("nextTurn", (userStarts ? NextTurn.OTHERS_TURN : NextTurn.MY_TURN).name());

				FirebaseMessenger.sendMessage(opponentID, ServerClientMessage.GAME_START, map2);
			}
		}
	}

	private static void action(Context ctx) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = getUserID(ctx);
		IAction action = StringSerializer.fromString(ctx.formParam("action"));

		String opponentID = redisStorage.getOpponentId(userID);
		GameBoard opponentBoard = redisStorage.getUserGameBoard(opponentID);
		ShipPlacements opponentPlacements = redisStorage.getUserShipPlacements(opponentID);

		TurnEvaluator evaluator = new TurnEvaluator(opponentPlacements, opponentBoard, action);

		ServerClientMessage type = evaluator.nextTurn() == NextTurn.GAME_OVER ? ServerClientMessage.GAME_OVER : ServerClientMessage.ACTION_PERFORMED;
		GameBoard opponentBoardAfter = evaluator.boardAfterTurn();
		redisStorage.setUserGameBoard(opponentID, opponentBoardAfter);
		ArrayList<GameBoardChange> changedCoords = new ArrayList<>(evaluator.getChangedCoords());
		ArrayList<IShip> unsunkShips = new ArrayList<>(opponentPlacements.getUnsunkShipsDisplaced(opponentBoardAfter));
		NextTurn nextTurn = evaluator.nextTurn();

		GameBoard opponentBoardAfterRevealed = new GameBoard(opponentBoardAfter).reveal(opponentPlacements, GameBoardField.UNKNOWN);
		NextTurn switchedTurn = nextTurn == NextTurn.GAME_OVER ?
				NextTurn.GAME_OVER :
				nextTurn == NextTurn.MY_TURN ? NextTurn.OTHERS_TURN : NextTurn.MY_TURN;

		{
			HashMap<String, String> map = new HashMap<>();
			map.put("board", StringSerializer.toString(opponentBoardAfter));
			map.put("changedCoords", StringSerializer.toString(changedCoords));
			map.put("unsunkShips", StringSerializer.toString(unsunkShips));
			map.put("nextTurn", nextTurn.name());

			FirebaseMessenger.sendMessage(userID, type, map);
		}

		{
			HashMap<String, String> map = new HashMap<>();
			map.put("board", StringSerializer.toString(opponentBoardAfterRevealed));
			map.put("changedCoords", StringSerializer.toString(changedCoords));
			map.put("unsunkShips", StringSerializer.toString(unsunkShips));
			map.put("nextTurn", switchedTurn.name());

			FirebaseMessenger.sendMessage(opponentID, type, map);
		}

		if (switchedTurn == NextTurn.GAME_OVER && !Lobby.getUsersGame(userID).isPrivate){
			elo_update_after_game(userID, opponentID);
		}

	}

	private static void timeout(Context ctx) throws FirebaseMessagingException {
		RedisStorage redisStorage = RedisStorage.getInstance();

		String userID = getUserID(ctx);

		String opponentID = redisStorage.getOpponentId(userID);
		GameBoard opponentBoard = redisStorage.getUserGameBoard(opponentID);
		ShipPlacements shipPlacements = redisStorage.getUserShipPlacements(opponentID);
		GameBoard opponentBoardRevealed = new GameBoard(opponentBoard).reveal(shipPlacements, GameBoardField.UNKNOWN);

		ServerClientMessage type = ServerClientMessage.ACTION_PERFORMED;
		ArrayList<GameBoardChange> changedCoords = new ArrayList<>();
		ArrayList<IShip> unsunkShips = new ArrayList<>(shipPlacements.getUnsunkShipsDisplaced(opponentBoard));

		{
			HashMap<String, String> map = new HashMap<>();

			map.put("board", StringSerializer.toString(opponentBoard));
			map.put("changedCoords", StringSerializer.toString(changedCoords));
			map.put("unsunkShips", StringSerializer.toString(unsunkShips));
			map.put("nextTurn", NextTurn.OTHERS_TURN.name());

			FirebaseMessenger.sendMessage(userID, type, map);
		}

		{
			HashMap<String, String> map = new HashMap<>();
			map.put("board", StringSerializer.toString(opponentBoardRevealed));
			map.put("changedCoords", StringSerializer.toString(changedCoords));
			map.put("unsunkShips", StringSerializer.toString(unsunkShips));
			map.put("nextTurn", NextTurn.MY_TURN.name());

			FirebaseMessenger.sendMessage(opponentID, type, map);
		}
	}

	private static void terminate(Context ctx) throws FirebaseMessagingException {
		String userID = getUserID(ctx);

		RedisStorage.getInstance().removeUserFromMatchmakingQueues(userID);

		Lobby lobby = Lobby.getUsersGame(userID);
		if (lobby == null){
			return; // todo:
		}
		lobby.endCommunication(userID);

		ctx.status(200).result("Ended communication and informed the other player");
	}

	private static void create_lobby(Context ctx) {
		String userID = getUserID(ctx);

		String privateLobby = ctx.formParam("privateLobby");
		assert "true".equals(privateLobby);

		Lobby lobby = new Lobby(true);
		lobby.addUser(userID); // todo: handle exceptions
		ctx.status(200).result(lobby.inviteID+"|Lobby created with invite ID and you've been invited.");
	}


	private static void join_lobby(Context ctx) {
		String userID = getUserID(ctx);
		String inviteID = ctx.formParamAsClass("id", String.class).get();

		Lobby lobby = Lobby.getLobbyByInvite(inviteID);
		if (lobby == null){
			FirebaseMessenger.sendMessage(userID, ServerClientMessage.NO_SUCH_LOBBY, null);
			ctx.status(404).result("Lobby doesn't exist");
			return;
		}

		lobby.addUser(userID); // todo: handle exceptions
		ctx.status(200).result("User added to Lobby");
	}

	private static void elo_get(Context ctx) {
		String userID = getUserID(ctx);
		String elo = RedisStorage.getInstance().getELO(userID);

		ctx.status(200).result("ELO is "+elo+". Will send via FCM message.");

		HashMap<String, String> map = new HashMap<>();
		map.put("elo", elo);
		FirebaseMessenger.sendMessage(userID, ServerClientMessage.ELO_UPDATE, map);
	}

	private static Pair<Integer, Integer> elo_calculate(Integer elo1, Integer elo2, int winner){

		double r1 = Math.pow(10, elo1.doubleValue()/400);
		double r2 = Math.pow(10, elo2.doubleValue()/400);

		double e1 = r1 / (r1 + r2);
		double e2 = r2 / (r1 + r2);

		double k = 32;

		double s1 = winner == 0 ? 1 : 0;
		double s2 = winner == 1 ? 1 : 0;

		double elo1_final = elo1 + k * (s1 - e1);
		double elo2_final = elo2 + k * (s2 - e2);

		return Pair.of(
				(int)Math.round(elo1_final),
				(int)Math.round(elo2_final)
		);
	}

	private static void elo_update_after_game(String winnerUserID, String looserUserID){
		RedisStorage redisStorage = RedisStorage.getInstance();

		String winner_elo = redisStorage.getELO(winnerUserID);
		String looser_elo = redisStorage.getELO(looserUserID);

		Pair<Integer, Integer> elos = elo_calculate(Integer.valueOf(winner_elo), Integer.valueOf(looser_elo), 0);

		redisStorage.setELO(winnerUserID, elos.getLeft().toString());
		redisStorage.setELO(looserUserID, elos.getRight().toString());
	}

	private static void healthcheck(Context ctx) {
		HealthCheck healthCheck = new HealthCheck();
		Map<String, String> result = healthCheck.getResult();

		int returnCode = 200;
		for (String key: result.keySet()) {
			if (key.startsWith("error_")) {
				returnCode = 500;
				break;
			}
		}

		ctx.status(returnCode).json(result);
	}


	public static void main (String[] arg) {
		logger.info("Server project started");

		Javalin app = Javalin.create(config -> {
			config.requestLogger((ctx, ms) -> {
				String userID = ctx.formParam("userID"); // don't use getUserID, as that would make it mandatory for all endpoints
				String shorterUserID = userID != null ? userID.substring(0, 10) : "NO_AUTH";
				logger.info(ctx.path() + " | "
						+ shorterUserID + " | "
						+ ctx.status() + " | "
						+ ms + " ms"
						+ " | time: " + Timestamp.from(Instant.now()));
			});
		}).start(7070);

		RedisStorage redisStorage = RedisStorage.getInstance();


		app.get("/", ctx -> ctx.result("Hello World"));

		app.post("/token", ctx -> {
			String userID = getUserID(ctx); // security: todo: make sure we have some rate limiting
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

		app.post("/elo", ServerLauncher::elo_get);

		app.get("/healthcheck", ServerLauncher::healthcheck);
	}

}
