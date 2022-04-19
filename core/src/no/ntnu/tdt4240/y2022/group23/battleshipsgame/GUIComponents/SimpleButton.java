package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class SimpleButton implements IRenderable {

    private Texture texture;
    private final Rectangle buttonBounds;
    private final int xPos;
    private final int yPos;
    private boolean enabled;

    public SimpleButton(int xPos, int yPos, Texture texture){
        this.texture = texture;
        this.xPos = xPos;
        this.yPos = yPos;
        buttonBounds = new Rectangle(xPos, yPos, texture.getWidth(), texture.getHeight());
        enabled = true;
    }
    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public boolean buttonContains() {
        return (buttonBounds.contains(Gdx.input.getX(), BattleshipsGame.HEIGHT - Gdx.input.getY()));
    }

    public boolean buttonTouched() {
        return (Gdx.input.justTouched() && enabled && buttonBounds.contains(Gdx.input.getX(), BattleshipsGame.HEIGHT - Gdx.input.getY()));
    }

    public Texture getTexture() {
        return texture;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }

    public Rectangle getButtonBounds() {
        return buttonBounds;
    }

    @Override
    public void handleInput() {}

    @Override
    public void update(float dt) {}

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(texture, xPos, yPos);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public void setNewTexture(Texture tex){
        texture = tex;
    }
}
