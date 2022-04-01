package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

import java.util.List;

public class ShipPlacementState extends AbstractState {
    private GameBoard gameBoard;
    private List<IShip> ships;
    private TimerPanel timer;

    protected ShipPlacementState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void handleInput() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void update(float dt) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void render(SpriteBatch sb) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("not implemented");
    }
}
