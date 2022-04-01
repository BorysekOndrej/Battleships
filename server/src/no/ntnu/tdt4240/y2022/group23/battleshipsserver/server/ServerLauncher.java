package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.FileInputStream;
import java.io.IOException;

import io.javalin.Javalin;

public class ServerLauncher {
	private static final Logger logger = LogManager.getLogger(ServerLauncher.class);

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

		app.post("/matchmaking_add", ctx -> {
			String user_id = ctx.formParamAsClass("user_id", String.class).get();
			redisStorage.addUserToMatchmakingQueue(user_id);
			ctx.status(200).result("Added to matchmaking set");

			ImmutablePair<String, String> twoUsersFromMatchmaking = redisStorage.getTwoUsersFromMatchmaking();
			if (twoUsersFromMatchmaking != null){
				System.out.println(twoUsersFromMatchmaking.left + " " + twoUsersFromMatchmaking.right); // todo: implement
			}
		});



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
