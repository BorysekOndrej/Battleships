package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.GameBoardPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RemainingShipsPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;

public class TestState extends AbstractState {

    private final Texture background;
    private GameBoardPanel gameBoardPanel;
    private RemainingShipsPanel shipsPanel;
    private TimerPanel timer;

    public TestState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("play_state/play_state_background.png");
        gameBoardPanel = new GameBoardPanel(63, 63);
        //gameBoardPanel.setPosition((BattleshipsGame.WIDTH- gameBoardPanel.getGameBoardWidth()) / 2,  (BattleshipsGame.WIDTH- gameBoardPanel.getGameBoardWidth()) / 2);
        shipsPanel = new RemainingShipsPanel(63, 1100);
        //shipsPanel.setPosition(37, 1100);
        timer = new TimerPanel(BattleshipsGame.WIDTH - 262 - 63, BattleshipsGame.HEIGHT - 125 - 63);
        timer.startTimer(20);
    }



    @Override
    public void handleInput(){
        if(Gdx.input.justTouched()){
            System.out.print(Gdx.input.getX()+ ", " + Gdx.input.getY() + "\n");
        }
    }

    @Override
    public void update(float dt) {
        gameBoardPanel.update(dt);
        shipsPanel.update(dt);
        timer.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0,0, BattleshipsGame.WIDTH, BattleshipsGame.HEIGHT);
        gameBoardPanel.render(sb);
        shipsPanel.render(sb);
        timer.render(sb);
        sb.end();
    }


    @Override
    public void dispose(){
        gameBoardPanel.dispose();
        shipsPanel.dispose();
    }
}

