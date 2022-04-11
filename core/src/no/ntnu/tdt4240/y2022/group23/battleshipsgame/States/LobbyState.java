package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LobbyState extends AbstractState {
    private boolean opponentFound = false;

    protected LobbyState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void update(float dt){
        if (opponentFound){ //Wait until opponent is found, then change to ship placement
            goToShipPlacement();
        }
    };

    @Override
    public void handleInput(){
        if (Gdx.input.justTouched()){ //Place holder given change to variable
            opponentFound();
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

    @Override
    public void render(SpriteBatch sb){
        opponentFound = true;
    };

    @Override
    public void dispose(){};
}
