package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class LobbyState extends AbstractState {
    private static boolean OPPONENT = FALSE;

    protected LobbyState(GameStateManager gsm) {
        super(gsm);
    }

    public void update(float dt){
        if (OPPONENT == TRUE){ //Wait until opponent is found, then change to ship placement
            goToShipPlacement();
        }
    }

    protected void handleInput(){
        if (Gdx.input.justTouched()){ //Place holder given change to
            OPPONENT = TRUE;
        }
    };

    //Changes state to ship placement state
    private void goToShipPlacement(){
        //Connect both players
        gsm.set(new ShipPlacementState(gsm));
    }

    public void render(SpriteBatch sb);

    public void dispose();
}
