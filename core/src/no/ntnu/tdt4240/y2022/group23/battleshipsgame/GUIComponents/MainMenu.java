package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class MainMenu implements IRenderable {
    @Override
    public void handleInput() { throw new UnsupportedOperationException("not implemented"); }

    @Override
    public void update(float dt) { throw new UnsupportedOperationException("not implemented");}

    @Override
    public void render(SpriteBatch sb) { throw new UnsupportedOperationException("not implemented");}

    @Override
    public void dispose() { throw new UnsupportedOperationException("not implemented");}

    public boolean createLobbyButtonPressed(){return false;}

    public boolean joinLobbyButtonPressed(){return false;}
}