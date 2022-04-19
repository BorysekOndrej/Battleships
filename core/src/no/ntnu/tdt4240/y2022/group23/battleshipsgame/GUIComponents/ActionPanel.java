package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.AbstractAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.Radar;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.SingleShot;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;

public class ActionPanel implements IRenderable {
    final static public int OFFSET = 10;
    final static public int GAP = 26;
    final static public int FIELD_WIDTH = 125;


    private final Texture  panel;
    private final ActionButton singleShot;
    private final ActionButton radar;
    private final ActionButton nuke;
    private final ActionButton surr;
    private final Texture buttonMarked;

    private Integer markedActions = null;
    private List<Pair<AbstractAction, Boolean>> actions;

    int xPos;
    int yPos;

    public ActionPanel(int xPos, int yPos){
        this.xPos = xPos;
        this.yPos = yPos;

        //debugging default list
        actions = new ArrayList<>();
        actions.add(new Pair<>(new SingleShot(new Coords(1, 1)), true));
        actions.add(new Pair<>(new Radar(new Coords(2, 1), 1),true));
        actions.add(new Pair<>(new SingleShot(new Coords(1, 1)), false));
        actions.add(new Pair<>(new SingleShot(new Coords(1, 1)), false));


        panel = new Texture("action_panel/action_panel.png");
        singleShot = new ActionButton(new Texture("action_panel/attack_button.png"), xPos + OFFSET + GAP, yPos + OFFSET * 2);
        radar = new ActionButton(new Texture("action_panel/radar_button.png"), xPos + OFFSET + GAP * 2 + FIELD_WIDTH, yPos + OFFSET * 2);
        nuke = new ActionButton(new Texture("action_panel/nuke_button.png"), xPos + OFFSET + GAP * 3 + FIELD_WIDTH * 2, yPos + OFFSET * 2);
        surr = new ActionButton(new Texture("action_panel/surr_button.png"), xPos + OFFSET + GAP * 4 + FIELD_WIDTH * 3, yPos + OFFSET * 2);
        buttonMarked = new Texture("action_panel/button_marked.png");
    }


    private void markAction(int nr){
        markedActions = nr;
    }

    public AbstractAction selectedAction(){
        if(markedActions == null) return null;
        return this.actions.get(markedActions).getValue0();
    }

    //Below Boolean in argument stand for if action is active
    public void setData(List<Pair<AbstractAction, Boolean>> actions) {
        this.actions = actions;
    }

    @Override
    public void handleInput() {
        if(singleShot.buttonTouched()){
            markAction(0);
        }
        if (radar.buttonTouched()) {
            markAction(1);
        }
        if(nuke.buttonTouched()){
            markAction(2);
        }
        if (surr.buttonTouched()) {
            markAction(3);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        singleShot.setActive(actions.get(0).getValue1());
        radar.setActive(actions.get(1).getValue1());
        nuke.setActive(actions.get(2).getValue1());
        surr.setActive(actions.get(3).getValue1());

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(panel, xPos, yPos);

        singleShot.render(sb);
        radar.render(sb);
        nuke.render(sb);
        surr.render(sb);

        if(markedActions != null){
            switch(markedActions){
                case 0:
                    sb.draw(buttonMarked, singleShot.getXPos(), singleShot.getYPos());
                    break;
                case 1:
                    sb.draw(buttonMarked, radar.getXPos(), radar.getYPos());
                    break;
                case 2:
                    sb.draw(buttonMarked, nuke.getXPos(), nuke.getYPos());
                    break;
                case 3:
                    sb.draw(buttonMarked, surr.getXPos(), surr.getYPos());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void dispose() {
        panel.dispose();
        singleShot.dispose();
        radar.dispose();
        nuke.dispose();
        surr.dispose();
        buttonMarked.dispose();
    }
}
