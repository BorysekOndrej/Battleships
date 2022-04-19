package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import static no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MenuStateGUI.BUTTON_WIDTH;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class JoinLobbyStateGUI extends RandOppLobbyStateGUI implements IRenderable {
    private String code = "";
    private final BitmapFont font;
    private final Texture codeTex;
    private final DigitsPanel digitsPanel;
    private final Texture enterCode;
    private boolean codeReady;

    public JoinLobbyStateGUI(){
        super();
        digitsPanel = new DigitsPanel((int)(super.getButtonPos().x) + BUTTON_WIDTH/2 - 776/2, (int)(super.getButtonPos().y + 220));
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(8);
        enterCode = new Texture("lobby_state/enter_code.png");
        codeTex = new Texture("lobby_state/code.png");
        codeReady = false;
    }

    public String getCode(){
        if(codeReady) return code;
        else return null;
    }

    public void resetState(){
        codeReady = false;
        digitsPanel.resetCode();
    }

    @Override
    public void handleInput() {
    }

    @Override
    public void update(float dt) {
        super.setAnimationActive(codeReady);
        super.update(dt);
        handleInput();
        digitsPanel.setEnabled(!codeReady);
        digitsPanel.update(dt);
        code = digitsPanel.getCode();
        codeReady = code.length() == 6;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        sb.begin();
        if(!codeReady) sb.draw(enterCode, super.getButtonPos().x + 20, super.getButtonPos().y + 900);
        sb.draw(codeTex, super.getButtonPos().x + (int)(BUTTON_WIDTH/2) - (int)(codeTex.getWidth()/2), super.getButtonPos().y + 600);
        font.draw(sb, code, super.getButtonPos().x + (int)(BUTTON_WIDTH/2) - (int)(codeTex.getWidth()/2) + 170, super.getButtonPos().y + 750);
        digitsPanel.render(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        super.dispose();
        codeTex.dispose();
        font.dispose();
        digitsPanel.dispose();
    }

    public boolean backButtonPressed(){return super.backButtonPressed();}
}
