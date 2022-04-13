package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;

public class MainMenu implements IRenderable {

    private final Texture background;
    private final Texture createLobbyTex;
    private final SimpleButton createLobbyButton;

    private final Texture joinLobbyTex;
    private final SimpleButton joinLobbyButton;

    private final Texture leaderBoardTex;
    private final SimpleButton leaderBoardButton;

    public MainMenu(){
        background = new Texture("main_menu/main_menu_background.png");

        createLobbyTex = new Texture("main_menu/create_lobby.png");
        createLobbyButton = new SimpleButton(BattleshipsGame.WIDTH/2 - createLobbyTex.getWidth()/2, BattleshipsGame.HEIGHT/2 - createLobbyTex.getHeight()/2, createLobbyTex);

        joinLobbyTex = new Texture("main_menu/join_lobby.png");
        joinLobbyButton = new SimpleButton(BattleshipsGame.WIDTH/2 - joinLobbyTex.getWidth()/2, BattleshipsGame.HEIGHT/2 - joinLobbyTex.getHeight()*2, joinLobbyTex);

        leaderBoardTex = new Texture("main_menu/leaderboard.png");
        leaderBoardButton = new SimpleButton(BattleshipsGame.WIDTH/2 - leaderBoardTex.getWidth()/2, BattleshipsGame.HEIGHT/2 - leaderBoardTex.getHeight()*7/2, leaderBoardTex);

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
        sb.draw(background, 0,0, BattleshipsGame.WIDTH, BattleshipsGame.HEIGHT);
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

