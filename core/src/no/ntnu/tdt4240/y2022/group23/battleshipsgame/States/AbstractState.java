package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;

// Game state manager is inspired by the YT series linked in the assignment 01.
// https://www.youtube.com/watch?v=rzBVTPaUUDg
// It's also the same O. Borysek submitted as part of assignment 01.

public abstract class AbstractState implements IState {
    protected OrthographicCamera cam;
    protected Vector3 mouse;
    protected GameStateManager gsm;

    protected AbstractState(GameStateManager gsm){
        this.gsm = gsm;
        cam = new OrthographicCamera();
        mouse = new Vector3();
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {handleInput();};
}
