package no.ntnu.tdt4240.y2022.group23.battleshipsgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new BattleshipsGame(), config);
	}
}
