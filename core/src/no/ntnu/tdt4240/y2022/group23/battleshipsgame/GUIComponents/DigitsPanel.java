package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class DigitsPanel implements IRenderable {
    final static public int FIELD_WIDTH = 156;
    final static public int FIELD_HEIGHT = 176;
    final static public int FIELD_OFFSET = 4;
    private final Texture panel;
    private Rectangle bonds;
    private final int xPos;
    private final int yPos;
    private String code;
    private boolean enabled;

    public DigitsPanel(int x, int y){
        panel = new Texture("lobby_state/digits_panel.png");
        code = "";
        xPos = x;
        yPos = y;
        enabled = true;
        bonds = new Rectangle(xPos + FIELD_OFFSET, yPos + FIELD_OFFSET, panel.getWidth() - FIELD_OFFSET * 2, panel.getHeight() - FIELD_OFFSET * 2);
    }
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public void resetCode(){
        code = "";
    }

    public String getCode(){
        return code;
    }

    public Integer pressedDigit(int x, int y){
        if(bonds.contains(x, BattleshipsGame.HEIGHT - y)){
            int xRel = x - xPos + FIELD_OFFSET;
            int yRel = y - (BattleshipsGame.HEIGHT - yPos - panel.getHeight() + FIELD_OFFSET);
            int result = xRel / FIELD_WIDTH;
            if(yRel/FIELD_HEIGHT == 1) result +=5;
            return result;
        }
        else return null;
    }

    @Override
    public void handleInput() {
        if(enabled){
            if(Gdx.input.justTouched()){
                Integer currentDigit = pressedDigit(Gdx.input.getX(), Gdx.input.getY());
                if(currentDigit != null){
                    code += currentDigit.toString();
                }
                else resetCode();
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(panel, xPos, yPos);
    }

    @Override
    public void dispose() {
        panel.dispose();
    }
}
