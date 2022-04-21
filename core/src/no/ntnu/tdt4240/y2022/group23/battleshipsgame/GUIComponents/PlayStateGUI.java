package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.AbstractAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.GameBoardObserver;

public class PlayStateGUI extends ShipPlacementStateGUI implements IRenderable {
    private final TurnIndicator turnIndicator;
    private final ActionPanel actionPanel;
    private final SimpleButton switchButton;

    public PlayStateGUI() {
        super();
        turnIndicator = new TurnIndicator(BIG_GAP, BattleshipsGame.HEIGHT - ACTION_PANEL_HEIGHT - BIG_GAP, NextTurn.MY_TURN);
        actionPanel = new ActionPanel(BIG_GAP, BIG_GAP * 2 + SMALL_GAP + GAME_BOARD_SIZE + SHIPS_PANEL_HEIGHT);
        switchButton = new SimpleButton(BIG_GAP + SMALL_GAP + ACTION_PANEL_WIDTH , BIG_GAP * 2 + GAME_BOARD_SIZE, new Texture("play_state/switch.png"));
    }

    ///// turnIndicator methods
    public void setTurnIndicator(NextTurn turnHolder){
        turnIndicator.setTurnHolder(turnHolder);
    }

    ///// actionPanel methods
    public AbstractAction selectedAction(){
        return actionPanel.selectedAction();
    }

    //Below Boolean in argument stands for if action is active
    public void setActions(List<Pair<AbstractAction, Boolean>> actions) {
        actionPanel.setData(actions);
    }

    ///// timerPanel methods
    public void startTimer(int periodInSec){
        super.timer.startTimer(periodInSec);
    }

    public boolean runOut(){
        return super.timer.runOut();
    }

    ///// shipsPanel methods

    // if disabled interaction with the user is disabled; ships cannot be marked
    public void setShipPanelEnabled(boolean enabled){
        super.shipsPanel.setEnabled(enabled);
    }

    public void setShips(List<Pair<IShip, Integer>> remainingShips) {
        super.shipsPanel.setData(remainingShips);
    }

    // returns null if no ship is selected
    public IShip selectedShipType(){
        return super.shipsPanel.selectedShipType();
    }

    ///// confirmButton and switchButton methods
    public void setConfirmButtonEnabled(boolean enabled){
        super.confirmButton.setEnabled(enabled);
    }

    public void setSwitchButtonEnabled(boolean enabled){
        switchButton.setEnabled(enabled);
    }

    public boolean confirmButtonPressed(){
        return super.confirmButton.buttonTouched();
    }

    public boolean switchButtonPressed(){
        return switchButton.buttonTouched();
    }

    ///// gameBoardPanel methods
    public void setGameBoard(GameBoard board){
        super.gameBoardPanel.setData(board);
    }

    public void setGameBoardPanelEnabled(boolean enabled){
        super.gameBoardPanel.setEnabled(enabled);
    }

    public void addGameBoardObserver(GameBoardObserver gameBoardObserver){
        gameBoardPanel.addGameBoardObserver(gameBoardObserver);
    }

    @Override
    public void handleInput(){}

    @Override
    public void update(float dt) {
        super.update(dt);
        actionPanel.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        sb.begin();
        turnIndicator.render(sb);
        actionPanel.render(sb);
        switchButton.render(sb);
        if(!switchButton.isEnabled()) sb.draw(inactiveButton, BIG_GAP + SMALL_GAP + ACTION_PANEL_WIDTH , BIG_GAP * 2 + GAME_BOARD_SIZE);
        sb.end();
    }

    @Override
    public void dispose(){
        super.dispose();
        turnIndicator.dispose();
        actionPanel.dispose();
        switchButton.dispose();
    }
}
