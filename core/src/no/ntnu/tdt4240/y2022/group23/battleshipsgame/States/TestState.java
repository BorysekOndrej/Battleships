package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.ActionPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.GameBoardPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RemainingShipsPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.SimpleButton;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TurnIndicator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;

public class TestState extends AbstractState {

    private final Texture background;

    private GameBoardPanel gameBoardPanel;
    private RemainingShipsPanel shipsPanel;
    private TimerPanel timer;
    private TurnIndicator turnIndicator;
    private ActionPanel actionPanel;

    private final SimpleButton confirmButton;
    private final SimpleButton switchButton;

    public TestState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("play_state/play_state_background.png");
        gameBoardPanel = new GameBoardPanel(63, 63);
        turnIndicator = new TurnIndicator(63, BattleshipsGame.HEIGHT - 164 - 63, true);
        //gameBoardPanel.setPosition((BattleshipsGame.WIDTH- gameBoardPanel.getGameBoardWidth()) / 2,  (BattleshipsGame.WIDTH- gameBoardPanel.getGameBoardWidth()) / 2);
        shipsPanel = new RemainingShipsPanel(63, 63 * 2 + 950);
        //shipsPanel.setPosition(37, 1100);
        timer = new TimerPanel(BattleshipsGame.WIDTH - 262 - 63, BattleshipsGame.HEIGHT - 164 - 63);
        timer.startTimer(20);
        actionPanel = new ActionPanel(63, 63 * 2 + 38 + 950 + 250);

        confirmButton = new SimpleButton(63 + 38 + 650 , 63 * 2 + 950 + 106 + 38, new Texture("play_state/confirm.png"));

        switchButton = new SimpleButton(63 + 38 + 650 , 63 * 2 + 950, new Texture("play_state/switch.png"));
    }



    @Override
    public void handleInput(){}

    @Override
    public void update(float dt) {
        gameBoardPanel.update(dt);
        shipsPanel.update(dt);
        timer.update(dt);
        actionPanel.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0,0, BattleshipsGame.WIDTH, BattleshipsGame.HEIGHT);
        turnIndicator.render(sb);
        gameBoardPanel.render(sb);
        shipsPanel.render(sb);
        timer.render(sb);
        actionPanel.render(sb);
        confirmButton.render(sb);
        switchButton.render(sb);
        sb.end();
    }


    @Override
    public void dispose(){
        gameBoardPanel.dispose();
        shipsPanel.dispose();
        timer.dispose();
        turnIndicator.dispose();
        actionPanel.dispose();
        confirmButton.dispose();
        switchButton.dispose();
    }
}

