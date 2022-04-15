package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class TurnIndicator implements IRenderable {

    private final Texture yourTurnTex;
    private final Texture enemyTurnTex;
    private boolean yourTurn = true;

    int xCord;
    int yCord;

    public TurnIndicator(int x, int y, boolean yourTurn){
        xCord = x;
        yCord = y;
        yourTurnTex = new Texture("play_state/yourTurn.png");
        enemyTurnTex = new Texture("play_state/enemyTurn.png");
        this.yourTurn = yourTurn;
    }

    public boolean isYourTurn(){
        return yourTurn;
    }

    public void setYourTurn(boolean isYourTurn){
        yourTurn = isYourTurn;
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        if (yourTurn) sb.draw(yourTurnTex, xCord, yCord);
        else sb.draw(enemyTurnTex, xCord, yCord);
    }

    @Override
    public void dispose() {
        yourTurnTex.dispose();
        enemyTurnTex.dispose();
    }
}
