package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MenuStateGUI.BUTTON_WIDTH;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class CreateLobbyStateGUI extends RandOppLobbyStateGUI implements IRenderable {

    private String code = ""; //Empty string could be weird (?)
    private final BitmapFont font;
    private final Texture codeTex;

    public CreateLobbyStateGUI(){
        super();
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(8);
        codeTex = new Texture("lobby_state/code.png");
    }

    public void setCode(String code){
        this.code = code;
    }

    @Override
    public void handleInput() {
        super.handleInput();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        sb.begin();
        sb.draw(codeTex, super.getButtonPos().x + (int)(BUTTON_WIDTH/2) - (int)(codeTex.getWidth()/2), super.getButtonPos().y + 600);
        font.draw(sb, code, super.getButtonPos().x + (int)(BUTTON_WIDTH/2) - (int)(codeTex.getWidth()/2) + 170, super.getButtonPos().y + 750);
        sb.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        codeTex.dispose();
        font.dispose();
    }

    public boolean backButtonPressed(){
        return super.backButtonPressed();
    }
}
