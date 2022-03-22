package no.ntnu.tdt4240.y2022.group23.battleshipsgame;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.FirebaseClient;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.HttpsClient;

public class AndroidLauncher extends AndroidApplication {
	HttpsClient httpsClient;
	FirebaseClient firebaseClient;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BattleshipsGame(), config);

		httpsClient = new HttpsClient(getApplicationContext());
		httpsClient.send("https://envojlo4sdzr8.x.pipedream.net/", null);

		firebaseClient = new FirebaseClient();
		firebaseClient.injectHttpsClient(httpsClient);

		firebaseClient.receive();

	}
}
