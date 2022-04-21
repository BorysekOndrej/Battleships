package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.GameAPIClient;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.INetworkClient;

// Game state manager is inspired by the YT series linked in the assignment 01.
// https://www.youtube.com/watch?v=rzBVTPaUUDg
// It's also the same O. Borysek submitted as part of assignment 01.


public class GameStateManager {

    private Stack<IState> states;
    private INetworkClient networkClient;

    public GameStateManager(INetworkClient networkClient){
        states = new Stack<>();
        this.networkClient = networkClient;
    }

    public INetworkClient getNetworkClient(){
        return networkClient;
    }

    public void push(IState state){
        states.push(state);
    }

    public void pop(){
        states.pop();
    }

    public void set(IState state){
        states.pop();
        states.push(state);
    }

    public void update(float dt){
        try {
            states.peek().update(dt);
        } catch (CommunicationTerminated communicationTerminated) {
            communicationTerminated.printStackTrace();
            set(new MenuState(this));
        }
    }

    public void render(SpriteBatch sb){ states.peek().render(sb); }
}
