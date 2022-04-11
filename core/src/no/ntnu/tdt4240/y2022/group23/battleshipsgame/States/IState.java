package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public interface IState extends IRenderable {

    void handleInput();

    void update(float dt);

    void render(SpriteBatch sb);

    void dispose();
}
