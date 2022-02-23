package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface IState {
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
}
