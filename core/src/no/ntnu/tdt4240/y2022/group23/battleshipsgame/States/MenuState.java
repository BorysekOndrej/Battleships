package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MainMenu;

public class MenuState extends AbstractState {
    MainMenu mainMenu;

    protected MenuState(GameStateManager gsm) {
        super(gsm);
        mainMenu = new MainMenu();
    }

    //Handles the input of clicking the button
    public void handleInput(){
        if (mainMenu.createLobby.buttonTouched()){
            goToLobbyState();
        }
        if (mainMenu.joinLobby.buttonTouched()){
            goToLobbyState();
        }
    }

    //Renders the main menu
    @Override
    public void render(SpriteBatch sb) {
        //mainMenu.render(sb);
    }

    //Changes state to lobby state
    private void goToLobbyState(){
        gsm.set(new LobbyState(gsm));
    }

    //Disposes the textures
    @Override
    public void dispose(){
        //mainMenu.dispose();
    }
}
