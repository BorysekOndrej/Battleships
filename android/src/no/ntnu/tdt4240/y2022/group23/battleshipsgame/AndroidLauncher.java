package no.ntnu.tdt4240.y2022.group23.battleshipsgame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.NetworkClient;

public class AndroidLauncher extends AndroidApplication {
	NetworkClient networkClient;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BattleshipsGame(), config);

		networkClient = NetworkClient.getInstance(getApplicationContext());
		networkClient.send("https://envojlo4sdzr8.x.pipedream.net/", null);
		networkClient.receive();

	}
}
