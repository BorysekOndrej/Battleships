package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MainMenu;

public class MenuState extends AbstractState {
    MainMenu mainMenu;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        mainMenu = new MainMenu();
    }

    //Handles the input of clicking the button
    public void handleInput(){
        if(Gdx.input.justTouched()){
            System.out.print(Gdx.input.getX()+ ", " + Gdx.input.getY() + "\n");
        }
        if (mainMenu.createLobbyButtonPressed()){
            goToLobbyState();
        }
        if (mainMenu.joinLobbyButtonPressed()){
            goToLobbyState();
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

    //Changes state to lobby state
    private void goToLobbyState(){
        gsm.set(new LobbyState(gsm));
    }

    @Override
    public void dispose(){
        //mainMenu.dispose();
    }
}
