package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;

public class TurnIndicator implements IRenderable {

    private final Texture yourTurnTex;
    private final Texture enemyTurnTex;
    private NextTurn turnHolder;

    int xPos;
    int yPos;

    public TurnIndicator(int x, int y, NextTurn turnHolder){
        xPos = x;
        yPos = y;
        yourTurnTex = new Texture("play_state/yourTurn.png");
        enemyTurnTex = new Texture("play_state/enemyTurn.png");
        this.turnHolder = this.turnHolder;
    }

    public NextTurn getTurnHolder(){
        return turnHolder;
    }

    public void setTurnHolder(NextTurn isYourTurn){
        turnHolder = isYourTurn;
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        if (turnHolder == NextTurn.MY_TURN) sb.draw(yourTurnTex, xPos, yPos);
        else sb.draw(enemyTurnTex, xPos, yPos);
    }

    @Override
    public void dispose() {
        yourTurnTex.dispose();
        enemyTurnTex.dispose();
    }
}
