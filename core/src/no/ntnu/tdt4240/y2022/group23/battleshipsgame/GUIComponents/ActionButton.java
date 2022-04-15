package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.AbstractAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class ActionButton implements IRenderable {
    AbstractAction action;
    private final Texture activeTex;
    private final Texture inactiveTex;
    private boolean active;
    private SimpleButton button;

    public ActionButton(Texture tex, int xPos, int yPos){
        activeTex = tex;
        button = new SimpleButton(xPos, yPos, activeTex);
        inactiveTex = new Texture("action_panel/button_inactive.png");
        active = true;
    }

    public int getXPos(){
        return button.getPosX();
    }

    public int getYPos(){
        return button.getPosY();
    }

    public boolean buttonTouched(){
        return button.buttonTouched();
    }

    public boolean isActive(){
        return active;
    }

    public void setActive(boolean isActive){
        active = isActive;
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        button.render(sb);
        if(!active) sb.draw(inactiveTex, button.getPosX(), button.getPosY());
    }

    @Override
    public void dispose() {
        activeTex.dispose();
        inactiveTex.dispose();
        button.dispose();
    }
}
