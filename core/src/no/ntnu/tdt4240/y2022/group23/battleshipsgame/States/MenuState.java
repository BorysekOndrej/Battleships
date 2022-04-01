package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MainMenu;

public class MenuState extends AbstractState {
    MainMenu mainMenu;

    protected MenuState(GameStateManager gsm) {
        mainMenu = new MainMenu();
        super(gsm);
    }

    //Handles the input of clicking the button
    protected void handleInput(){
        if (mainMenu.createLobby.buttonTouched()){
            goToLobbyState();
        }
        if (mainMenu.joinLobby.buttonTouched()){
            goToLobbyState();
        }
        if (mainMenu.leaderboards.buttonTouched()){
            goToLeaderboardState();
        }
    };

    //Changes state to lobby state
    private void goToLobbyState(){
        gsm.set(new LobbyState(gsm));
    }

    //Renders the main menu
    public void render(SpriteBatch sb){
        mainMenu.render(SpriteBatch sb);
    };

    //Disposes the textures of
    public void dispose(){
        mainMenu.dispose();
    };
}
