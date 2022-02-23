package no.ntnu.tdt4240.y2022.group23.battleshipsgame;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IRenderable {
    public boolean handleInput();
    public void update(float dt);
    public void render(SpriteBatch sb);
    public void dispose();
}
