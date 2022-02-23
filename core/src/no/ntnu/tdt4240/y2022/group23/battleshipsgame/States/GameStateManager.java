package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;

// Game state manager is inspired by the YT series linked in the assignment 01.
// https://www.youtube.com/watch?v=rzBVTPaUUDg
// It's also the same O. Borysek submitted as part of assignment 01.


public class GameStateManager {
    private Stack<AbstractState> states;

    public GameStateManager(){
        states = new Stack<>();
    }

    public void push(AbstractState state){
        states.push(state);
    }

    public void pop(){
        states.pop();
    }

    public void set(AbstractState state){
        states.pop();
        states.push(state);
    }

    public void update(float dt){
        states.peek().update(dt);
    }
    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }
}
