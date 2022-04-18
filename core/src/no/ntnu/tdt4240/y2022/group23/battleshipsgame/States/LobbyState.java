package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.Lobby;

public class LobbyState extends AbstractState {
    private boolean opponentFound = false;
    private Lobby lobby;

    protected LobbyState(GameStateManager gsm) {
        super(gsm);
        lobby = new Lobby();
    }

    @Override
    public void update(float dt){
        handleInput();
        lobby.update(dt);
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
        if (lobby.backButtonPressed()){
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
        lobby.render(sb);
        opponentFound = true;
    };

    @Override
    public void dispose(){};
}
