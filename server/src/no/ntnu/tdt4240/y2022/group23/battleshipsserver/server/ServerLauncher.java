package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.text.RandomStringGenerator;

import java.io.FileInputStream;
import java.io.IOException;

import io.javalin.Javalin;

public class ServerLauncher {
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
			String oldToken = ctx.formParam("oldToken"); // this can be null
			String newToken = ctx.formParamAsClass("newToken", String.class).get();

			String user_id = redisStorage.getUserIDByToken(oldToken);
			if (user_id == null){
				do {
					user_id = new RandomStringGenerator.Builder().withinRange('a', 'z').build().generate(32);
				}while (redisStorage.getUserTokenByID(user_id) != null);
			}
			redisStorage.setNewUserToken(user_id, newToken);

			ctx.status(200).result("Hello " + oldToken + " " + newToken + " " + user_id);
		});

		app.post("/matchmaking_add", ctx -> {
			String newToken = ctx.formParamAsClass("token", String.class).get();
			String user_id = redisStorage.getUserIDByToken(newToken);
			redisStorage.addUserToMatchmakingQueue(user_id);
			ctx.status(200).result("Added to matchmaking set");

			ImmutablePair<String, String> twoUsersFromMatchmaking = redisStorage.getTwoUsersFromMatchmaking();
			if (twoUsersFromMatchmaking != null){
				System.out.println(twoUsersFromMatchmaking.left + " " + twoUsersFromMatchmaking.right); // todo: implement
			}
		});



		app.post("/test_firebase_msg", ctx -> {
			// This registration token comes from the client FCM SDKs.
			String token = ctx.formParamAsClass("token", String.class).get();

			// See documentation on defining a message payload.
			Message message = Message.builder()
					.putData("score", "850")
					.putData("time", "2:45")
					.setToken(token)
					.build();

			// Send a message to the device corresponding to the provided
			// registration token.
			String response = FirebaseMessaging.getInstance().send(message);
			// Response is a message ID string.
			System.out.println("Successfully sent message: " + response);

		});

	}
}
