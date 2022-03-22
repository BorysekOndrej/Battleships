package no.ntnu.tdt4240.y2022.group23.battleshipsgame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.httpsClient;

public class AndroidLauncher extends AndroidApplication {
	httpsClient httpsClient;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BattleshipsGame(), config);

		httpsClient = new httpsClient(getApplicationContext());
		httpsClient.send("https://envojlo4sdzr8.x.pipedream.net/", null);
	}
}
