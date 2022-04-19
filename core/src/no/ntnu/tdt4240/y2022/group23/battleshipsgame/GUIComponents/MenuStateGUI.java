package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;

public class MenuStateGUI implements IRenderable {
    final static public int BUTTON_WIDTH = 409;
    final static public int BUTTON_HEIGHT = 170;

    private final Texture background;
    private final SimpleButton createLobbyButton;
    private final SimpleButton joinLobbyButton;
    private final SimpleButton randomButton;

    public MenuStateGUI(){
        background = new Texture("main_menu/main_menu_background.png");

        createLobbyButton = new SimpleButton(BattleshipsGame.WIDTH/2 - BUTTON_WIDTH/2, BattleshipsGame.HEIGHT/2 - BUTTON_HEIGHT/2, new Texture("main_menu/create_lobby.png"));

        joinLobbyButton = new SimpleButton(BattleshipsGame.WIDTH/2 - BUTTON_WIDTH/2, BattleshipsGame.HEIGHT/2 - BUTTON_HEIGHT*2, new Texture("main_menu/join_lobby.png"));

        randomButton = new SimpleButton(BattleshipsGame.WIDTH/2 - BUTTON_WIDTH/2, BattleshipsGame.HEIGHT/2 - BUTTON_HEIGHT*7/2, new Texture("main_menu/random.png"));

    }
    public boolean createLobbyButtonPressed(){
        return createLobbyButton.buttonTouched();
    }

    public boolean joinLobbyButtonPressed(){
        return joinLobbyButton.buttonTouched();
    }

    public boolean randomButtonPressed(){
        return randomButton.buttonTouched();
    }

    public boolean leaderBoardButtonPressed(){ return false; }

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
        createLobbyButton.render(sb);
        joinLobbyButton.render(sb);
        randomButton.render(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        joinLobbyButton.dispose();
        createLobbyButton.dispose();
        randomButton.dispose();
    }
}

