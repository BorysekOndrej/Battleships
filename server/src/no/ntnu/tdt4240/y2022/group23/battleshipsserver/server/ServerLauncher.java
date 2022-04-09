package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.javalin.Javalin;
import io.javalin.http.Context;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.ServerClientMessage;

public class ServerLauncher {
	private static final Logger logger = LogManager.getLogger(ServerLauncher.class);

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

	public static void end_communication(Context ctx) {
		String userID = ctx.formParamAsClass("userID", String.class).get();
		Lobby lobby = Lobby.getLobby(Lobby.findGameID(userID));
		if (lobby == null){
			return; // todo:
		}
		lobby.endCommunication(userID);
		ctx.status(200).result("Ended communication and informed the other player");
	}

	public static void main (String[] arg) {
		System.out.println("Server project started");
		Javalin app = Javalin.create().start(7070);
		RedisStorage redisStorage = RedisStorage.getInstance();


		app.get("/", ctx -> ctx.result("Hello World"));

		app.post("/token", ctx -> {
			String userID = ctx.formParamAsClass("userID", String.class).get(); // security: todo: make sure we have some rate limiting
			String token = ctx.formParamAsClass("token", String.class).get();

			redisStorage.setNewUserToken(userID, token);
			ctx.status(200).result("Hello " + token + " " + userID);
		});

		app.post("/join_matchmaking", ServerLauncher::join_matchmaking);
		app.post("/join_lobby", ServerLauncher::join_lobby);
		app.post("/create_lobby", ServerLauncher::create_lobby);
		app.post("/end_communication", ServerLauncher::end_communication);

		app.post("/test_firebase_msg", ctx -> {
			// This registration token comes from the client FCM SDKs.
			String userID = ctx.formParamAsClass("userID", String.class).get();
			System.out.println("Successfully sent message");

		});

	}
}
