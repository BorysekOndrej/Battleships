package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.CreateLobbyStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.FinishedGameStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.JoinLobbyStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RandOppLobbyStateGUI;

public class LobbyState extends AbstractState {
    private boolean opponentFound = false;
    //private RandOppLobbyStateGUI randOppLobbyGUI;
    //private CreateLobbyStateGUI createLobbyStateGUI;
    //private JoinLobbyStateGUI joinLobbyStateGUI;
    private FinishedGameStateGUI finishedGameState;

    protected LobbyState(GameStateManager gsm) {
        super(gsm);
        finishedGameState = new FinishedGameStateGUI(false);
        //joinLobbyStateGUI = new JoinLobbyStateGUI();
        //createLobbyStateGUI = new CreateLobbyStateGUI("123456");
    }

    @Override
    public void update(float dt){
        handleInput();
        finishedGameState.update(dt);
        /*
        if (opponentFound){ //Wait until opponent is found, then change to ship placement
            goToShipPlacement();
        }*/
    };

    @Override
    public void handleInput(){
        /*
        if (Gdx.input.justTouched()){ //Place holder given change to variable
            opponentFound();
        }

         */
        if (finishedGameState.playAgainButtonPressed()){
            goToMenu();
        }
    };

    //Signals that an opponent has been found
    public void opponentFound(){
        opponentFound = true;
    }

    //Changes state to ship placement state
    private void goToShipPlacement(){
        //Connect both players
        gsm.set(new ShipPlacementState(gsm));
    };

    //Changes state to menu state
    private void goToMenu(){
        gsm.set(new MenuState(gsm));
    };

    @Override
    public void render(SpriteBatch sb){
        finishedGameState.render(sb);
        opponentFound = true;
    };

    @Override
    public void dispose(){finishedGameState.dispose();};
}
