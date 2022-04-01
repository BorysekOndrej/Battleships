package no.ntnu.tdt4240.y2022.group23.battleshipsgame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IRenderable {
    void handleInput();
    void update(float dt);
    void render(SpriteBatch sb);
    void dispose();
}
