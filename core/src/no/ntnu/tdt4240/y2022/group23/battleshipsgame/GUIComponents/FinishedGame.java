package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class FinishedGame implements IRenderable {

    public boolean backButtonPressed(){return false;}

    @Override
    public void handleInput() { throw new UnsupportedOperationException("not implemented"); }

    @Override
    public void update(float dt) { throw new UnsupportedOperationException("not implemented");}

    @Override
    public void render(SpriteBatch sb) { throw new UnsupportedOperationException("not implemented");}

    @Override
    public void dispose() { throw new UnsupportedOperationException("not implemented");}
}
