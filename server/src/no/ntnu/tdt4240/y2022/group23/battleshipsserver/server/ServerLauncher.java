package no.ntnu.tdt4240.y2022.group23.battleshipsserver.server;

import io.javalin.Javalin;

public class ServerLauncher {
	public static void main (String[] arg) {
		System.out.println("Server project started");
		Javalin app = Javalin.create().start(7070);

		app.get("/", ctx -> ctx.result("Hello World"));

		app.post("/token", ctx -> {

			String oldToken = ctx.formParam("oldToken"); // this can be null
			String newToken = ctx.formParamAsClass("newToken", String.class).get();;
			ctx.status(200).result("Hello " + oldToken + " " + newToken);
		});

	}
}
