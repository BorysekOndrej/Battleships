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
		networkClient = NetworkClient.getInstance(getApplicationContext());

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		BattleshipsGame battleshipsGame = new BattleshipsGame();
		battleshipsGame.injectNetworkClient(networkClient);
		initialize(battleshipsGame, config);
	}
}
