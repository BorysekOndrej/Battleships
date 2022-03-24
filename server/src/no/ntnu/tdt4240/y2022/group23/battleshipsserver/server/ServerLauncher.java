package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import org.apache.commons.text.RandomStringGenerator;

import io.javalin.Javalin;

public class ServerLauncher {
	public static void main (String[] arg) {
		System.out.println("Server project started");
		Javalin app = Javalin.create().start(7070);
		RedisStorage redisStorage = RedisStorage.getInstance();

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
	}
}
