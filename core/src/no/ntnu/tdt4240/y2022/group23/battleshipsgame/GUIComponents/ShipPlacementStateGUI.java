package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.AbstractAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

public class ShipPlacementStateGUI implements IRenderable {
    final static public int BIG_GAP = 63;
    final static public int SMALL_GAP = 38;
    final static public int GAME_BOARD_SIZE = 950;
    final static public int ACTION_PANEL_WIDTH = 650;
    final static public int ACTION_PANEL_HEIGHT = 164;
    final static public int SHIPS_PANEL_HEIGHT = 250;
    final static public int BUTTON_HEIGHT = 106;
    final static public int BUTTON_WIDTH = 262;

    private final Texture background;

    public final GameBoardPanel gameBoardPanel;
    public final RemainingShipsPanel shipsPanel;
    public final TimerPanel timer;
    public final Texture placeShips;

    public final SimpleButton confirmButton;
    public final Texture inactiveButton;


    public ShipPlacementStateGUI(){
        background = new Texture("play_state/play_state_background.png");
        gameBoardPanel = new GameBoardPanel(BIG_GAP, BIG_GAP);
        placeShips = new Texture("play_state/placeYourShips.png");
        shipsPanel = new RemainingShipsPanel(BIG_GAP, BIG_GAP * 2 + GAME_BOARD_SIZE);
        timer = new TimerPanel(BattleshipsGame.WIDTH - BUTTON_WIDTH - BIG_GAP, BattleshipsGame.HEIGHT - ACTION_PANEL_HEIGHT - BIG_GAP);
        timer.startTimer(20);

        confirmButton = new SimpleButton(BIG_GAP + SMALL_GAP + ACTION_PANEL_WIDTH , BIG_GAP * 2 + GAME_BOARD_SIZE + BUTTON_HEIGHT + SMALL_GAP, new Texture("play_state/confirm.png"));
        inactiveButton = new Texture("play_state/inactive_button.png");
    }

    ///// timerPanel methods
    public void startTimer(int periodInSec){
        timer.startTimer(periodInSec);
    }

    public boolean runOut(){
        return timer.runOut();
    }


    ///// shipsPanel methods

    // if disabled interaction with the user is disabled; ships cannot be marked
    public void setEnabled(boolean enabled){
        shipsPanel.setEnabled(enabled);
    }

    public void setData(List<Pair<IShip, Integer>> remainingShips) {
        shipsPanel.setData(remainingShips);
    }

    // returns null if no ship is selected
    public IShip selectedShipType(){
        return shipsPanel.selectedShipType();
    }

    ///// confirmButton methods
    public void setConfirmButtonEnabled(boolean enabled){
        confirmButton.setEnabled(enabled);
    }

    public boolean confirmButtonPressed(){
        return confirmButton.buttonTouched();
    }

    ///// gameBoardPanel methods
    public void setData(GameBoard board){
        gameBoardPanel.setData(board);
    }

    public void setGameBoardPanelEnabled(boolean enabled){
        gameBoardPanel.setEnabled(enabled);
    }


    @Override
    public void handleInput(){}

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
        sb.draw(placeShips, BIG_GAP, BattleshipsGame.HEIGHT - ACTION_PANEL_HEIGHT - BIG_GAP);

        gameBoardPanel.render(sb);
        shipsPanel.render(sb);
        timer.render(sb);
        confirmButton.render(sb);
        if(!confirmButton.isEnabled()) sb.draw(inactiveButton, BIG_GAP + SMALL_GAP + ACTION_PANEL_WIDTH , BIG_GAP * 2 + GAME_BOARD_SIZE + BUTTON_HEIGHT + SMALL_GAP);
        sb.end();
    }

    @Override
    public void dispose(){
        gameBoardPanel.dispose();
        shipsPanel.dispose();
        timer.dispose();
        confirmButton.dispose();
    }
}
