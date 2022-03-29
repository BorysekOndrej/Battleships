package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LobbyState extends AbstractState {
    private static boolean OPPONENT = FALSE;

    protected LobbyState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void update(float dt){
        if (OPPONENT == TRUE){ //Wait until opponent is found, then change to ship placement
            goToShipPlacement();
        }
    };

    @Override
    public void handleInput(){
        if (Gdx.input.justTouched()){ //Place holder given change to
            OPPONENT = TRUE;
        }
    };

    //Changes state to ship placement state
    private void goToShipPlacement(){
        //Connect both players
        gsm.set(new ShipPlacementState(gsm));
    };

    @Override
    public void render(SpriteBatch sb){
        OPPONENT = TRUE;
    };

    @Override
    public void dispose(){};
}
