package no.ntnu.tdt4240.y2022.group23.battleshipsgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Map;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.INetworkClient;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.States.GameStateManager;

public class BattleshipsGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	GameStateManager gsm;
	INetworkClient networkClient;

	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();

		Map<String, String> receivedMsg = networkClient.receive();
		if (receivedMsg != null){
			System.out.println("Received new msg");
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public void injectNetworkClient(INetworkClient networkClient){
		this.networkClient = networkClient;
		networkClient.send("https://envojlo4sdzr8.x.pipedream.net/", null);
	}
}
