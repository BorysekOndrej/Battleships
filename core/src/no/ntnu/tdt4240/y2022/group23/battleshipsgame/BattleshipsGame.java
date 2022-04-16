package no.ntnu.tdt4240.y2022.group23.battleshipsgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Map;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.INetworkClient;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.States.GameStateManager;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.States.MenuState;

public class BattleshipsGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private GameStateManager gsm;
	private INetworkClient networkClient;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager(networkClient);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);

		Map<String, String> receivedMsg = networkClient.receive();
		if (receivedMsg != null){
			System.out.println("Received new msg");
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void injectNetworkClient(INetworkClient networkClient){
		this.networkClient = networkClient;
	}
}
