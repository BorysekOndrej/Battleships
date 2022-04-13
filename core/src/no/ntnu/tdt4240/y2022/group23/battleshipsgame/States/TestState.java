package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.GameBoardPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;

public class TestState extends AbstractState {

    private GameBoardPanel gameBoardPanel;

    public TestState(GameStateManager gsm) {
        super(gsm);
        gameBoardPanel = new GameBoardPanel();
        gameBoardPanel.setPosition((BattleshipsGame.WIDTH- gameBoardPanel.getGameBoardWidth()) / 2,  (BattleshipsGame.WIDTH- gameBoardPanel.getGameBoardWidth()) / 2);
    }

    @Override
    public void handleInput(){
        if(Gdx.input.justTouched()){
            System.out.print(Gdx.input.getX()+ ", " + Gdx.input.getY() + "\n");
        }
    }

    @Override
    public void update(float dt) {
        gameBoardPanel.handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        gameBoardPanel.render(sb);
    }


    @Override
    public void dispose(){};
}

