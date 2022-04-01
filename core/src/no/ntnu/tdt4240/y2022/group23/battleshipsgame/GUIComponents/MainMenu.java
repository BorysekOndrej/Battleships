package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class MainMenu implements IRenderable {
    final static public int HEIGHT = 1920;
    final static public int WIDTH = 1080;

    private final Texture background;
    private final Texture createLobbyTex;
    private SimpleButton createLobbyButton;

    private final Texture joinLobbyTex;
    private SimpleButton joinLobbyButton;

    private final Texture leaderBoardTex;
    private SimpleButton leaderBoardButton;

    public MainMenu(){
        background = new Texture("game_menu_background.png");

        createLobbyTex = new Texture("create_lobby.png");
        createLobbyButton = new SimpleButton(WIDTH/2 - createLobbyTex.getWidth()/2, HEIGHT/2 - createLobbyTex.getHeight()/2, createLobbyTex);

        joinLobbyTex = new Texture("join_lobby.png");
        joinLobbyButton = new SimpleButton(WIDTH/2 - joinLobbyTex.getWidth()/2, HEIGHT/2 - joinLobbyTex.getHeight()*3/2, joinLobbyTex);

        leaderBoardTex = new Texture("leaderboard.png");
        leaderBoardButton = new SimpleButton(WIDTH/2 - leaderBoardTex.getWidth()/2, HEIGHT/2 - leaderBoardTex.getHeight()*5/2, leaderBoardTex);

    }
    public boolean createLobbyButtonPressed(){
        return createLobbyButton.buttonTouched();
    }

    public boolean joinLobbyButtonPressed(){
        return joinLobbyButton.buttonTouched();
    }

    public boolean leaderBoardButtonPressed(){
        return leaderBoardButton.buttonTouched();
    }

    @Override
    public void handleInput() {}

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0,0, WIDTH, HEIGHT);
        sb.draw(createLobbyButton.getTexture(), createLobbyButton.getPosX(), createLobbyButton.getPosY());
        sb.draw(joinLobbyButton.getTexture(), joinLobbyButton.getPosX(), joinLobbyButton.getPosY());
        sb.draw(leaderBoardButton.getTexture(), leaderBoardButton.getPosX(), leaderBoardButton.getPosY());
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        joinLobbyTex.dispose();
        createLobbyTex.dispose();
        leaderBoardTex.dispose();

    }
}
