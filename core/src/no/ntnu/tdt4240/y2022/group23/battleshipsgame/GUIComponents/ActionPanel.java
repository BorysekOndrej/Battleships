package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.AbstractAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

public class ActionPanel implements IRenderable {
    private Texture  panel;
    private ActionButton singleShot;
    private ActionButton radar;
    private ActionButton nuke;
    private ActionButton surr;
    private final Texture buttonMarked;
    //private final Texture unknown;

    private boolean markedActions[] = {false, false, false, false};

    int xPos;
    int yPos;

    public ActionPanel(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;
        panel = new Texture("action_panel/action_panel.png");
        singleShot = new ActionButton(new Texture("action_panel/attack_button.png"), xPos + 10 + 26, yPos + 10 +10);
        radar = new ActionButton(new Texture("action_panel/radar_button.png"), xPos + 10 + 26 * 2 + 125, yPos + 10 +10);
        nuke = new ActionButton(new Texture("action_panel/nuke_button.png"), xPos + 10 + 26 * 3 + 125 * 2, yPos + 10 +10);
        surr = new ActionButton(new Texture("action_panel/surr_button.png"), xPos + 10 + 26 * 4 + 125 * 3, yPos + 10 +10);
        buttonMarked = new Texture("action_panel/button_marked.png");
        //unknown = new Texture("action_panel/unknown_button.png");
    }

    private void resetMarkedActions(){
        markedActions[0] = false;
        markedActions[1] = false;
        markedActions[2] = false;
        markedActions[3] = false;
    }

    private void markAction(int type){
        resetMarkedActions();
        markedActions[type] = true;
    }

    public AbstractAction selectedAction(){
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void handleInput() {
        if(singleShot.buttonTouched()){
            markAction(0);
            //singleShot.setActive(false);
        }
        if (radar.buttonTouched()) {
            markAction(1);
            //radar.setActive(false);
        }
        if(nuke.buttonTouched()){
            markAction(2);
        }
        if (surr.buttonTouched()) {
            markAction(3);
            surr.setActive(false);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(panel, xPos, yPos);
        singleShot.render(sb);
        radar.render(sb);
        nuke.render(sb);
        surr.render(sb);
        //sb.draw(unknown, xPos + 10 + 26 * 3 + 125 * 2, yPos + 10 +10);
        //sb.draw(unknown, xPos + 10 + 26 * 4 + 125 * 3, yPos + 10 +10);

        if (markedActions[0]) sb.draw(buttonMarked, singleShot.getXPos(), singleShot.getYPos());
        if (markedActions[1]) sb.draw(buttonMarked, radar.getXPos(), radar.getYPos());
        if (markedActions[2]) sb.draw(buttonMarked, nuke.getXPos(), nuke.getYPos());
        if (markedActions[3]) sb.draw(buttonMarked, surr.getXPos(), surr.getYPos());
    }

    @Override
    public void dispose() {
        panel.dispose();
        singleShot.dispose();
        radar.dispose();
    }
}
