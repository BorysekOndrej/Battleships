package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MenuStateGUI;

public class MenuState extends AbstractState {
    MenuStateGUI mainMenu;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        mainMenu = new MenuStateGUI();
    }

    //Handles the input of clicking the button
    public void handleInput(){
        if (mainMenu.matchmakingButtonPressed()){
            goToMatchmaking();
        }
        if (mainMenu.createLobbyButtonPressed()){
            goToCreateLobby();
        }
        if (mainMenu.joinLobbyButtonPressed()){
            goToJoinLobby();
        }
        if (mainMenu.leaderBoardButtonPressed()){
            gsm.set(new TestState(gsm));//temporarily added for debuging
        }
    }

    //Renders the main menu
    @Override
    public void render(SpriteBatch sb) {
        mainMenu.render(sb);
    }

    //Changes state to create lobby state
    private void goToMatchmaking(){ gsm.set(new MatchmakingLobbyState(gsm));}

    //Changes state to create lobby state
    private void goToCreateLobby(){ gsm.set(new CreateLobbyState(gsm));}

    //Changes state to create lobby state
    private void goToJoinLobby(){ gsm.set(new JoinLobbyState(gsm));}

    @Override
    public void dispose(){
        mainMenu.dispose();
    }
}
