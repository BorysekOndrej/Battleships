package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.ActionPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.GameBoardPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.PlayStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RemainingShipsPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.ShipPlacementStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.SimpleButton;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TurnIndicator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;

public class TestState extends AbstractState {
    private PlayStateGUI playState;
    //private ShipPlacementStateGUI shipPlacementStateGUI;

    public TestState(GameStateManager gsm) {
        super(gsm);
        playState = new PlayStateGUI();
        playState.setGameBoardPanelEnabled(false);

    }

    @Override
    public void handleInput(){}

    @Override
    public void update(float dt) {
        playState.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        playState.render(sb);
    }

    @Override
    public void dispose(){
        playState.dispose();
    }
}

