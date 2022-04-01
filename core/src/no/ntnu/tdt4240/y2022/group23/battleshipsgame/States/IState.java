package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public interface IState extends IRenderable {

    //Handles the input of the user
    void handleInput();

    //Updates the state upon each unit of time
    void update(float dt);

    //Renders the state textures
    void render(SpriteBatch sb);

    //Dispose the state textures upon usage
    void dispose();
}
