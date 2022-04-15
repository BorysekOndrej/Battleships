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
    private final int posX;
    private final int posY;

    public SimpleButton(int posX, int posY, Texture texture){
        this.texture = texture;
        this.posX = posX;
        this.posY = posY;
        buttonBounds = new Rectangle(posX, posY, texture.getWidth(), texture.getHeight());
    }

    public boolean buttonTouched() {
        return (Gdx.input.justTouched() && buttonBounds.contains(Gdx.input.getX(), BattleshipsGame.HEIGHT - Gdx.input.getY()));
    }

    public Texture getTexture() {
        return texture;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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
        sb.draw(texture, posX, posY);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    public void setNewTexture(Texture tex){
        texture = tex;
    }
}