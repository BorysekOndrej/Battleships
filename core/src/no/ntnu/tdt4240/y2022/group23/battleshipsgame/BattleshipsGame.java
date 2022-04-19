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
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.States.TestState;

public class BattleshipsGame extends ApplicationAdapter {
	final static public int HEIGHT = 1792;
	final static public int WIDTH = 1076;
	private SpriteBatch batch;
	private GameStateManager gsm;
	private INetworkClient networkClient;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
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
