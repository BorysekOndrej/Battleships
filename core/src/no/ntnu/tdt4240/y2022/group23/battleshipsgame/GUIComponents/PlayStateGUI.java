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

public class PlayStateGUI implements IRenderable {
    final static public int BIG_GAP = 63;
    final static public int SMALL_GAP = 38;
    final static public int GAME_BOARD_SIZE = 950;
    final static public int ACTION_PANEL_WIDTH = 650;
    final static public int ACTION_PANEL_HEIGHT = 164;
    final static public int SHIPS_PANEL_HEIGHT = 250;
    final static public int BUTTON_HEIGHT = 106;
    final static public int BUTTON_WIDTH = 262;

    private final Texture background;

    private final GameBoardPanel gameBoardPanel;
    private final RemainingShipsPanel shipsPanel;
    private final TimerPanel timer;
    private boolean turnIndicatorEnabled;
    private final Texture placeShips;
    private final TurnIndicator turnIndicator;
    private final ActionPanel actionPanel;

    private final SimpleButton confirmButton;
    private final SimpleButton switchButton;
    private final Texture inactiveButton;

    public PlayStateGUI() {
        background = new Texture("play_state/play_state_background.png");
        gameBoardPanel = new GameBoardPanel(BIG_GAP, BIG_GAP);
        placeShips = new Texture("play_state/placeYourShips.png");
        turnIndicator = new TurnIndicator(BIG_GAP, BattleshipsGame.HEIGHT - ACTION_PANEL_HEIGHT - BIG_GAP, false);
        shipsPanel = new RemainingShipsPanel(BIG_GAP, BIG_GAP * 2 + GAME_BOARD_SIZE);
        timer = new TimerPanel(BattleshipsGame.WIDTH - BUTTON_WIDTH - BIG_GAP, BattleshipsGame.HEIGHT - ACTION_PANEL_HEIGHT - BIG_GAP);
        timer.startTimer(20);
        actionPanel = new ActionPanel(BIG_GAP, BIG_GAP * 2 + SMALL_GAP + GAME_BOARD_SIZE + SHIPS_PANEL_HEIGHT);

        confirmButton = new SimpleButton(BIG_GAP + SMALL_GAP + ACTION_PANEL_WIDTH , BIG_GAP * 2 + GAME_BOARD_SIZE + BUTTON_HEIGHT + SMALL_GAP, new Texture("play_state/confirm.png"));
        switchButton = new SimpleButton(BIG_GAP + SMALL_GAP + ACTION_PANEL_WIDTH , BIG_GAP * 2 + GAME_BOARD_SIZE, new Texture("play_state/switch.png"));
        inactiveButton = new Texture("play_state/inactive_button.png");
        turnIndicatorEnabled = false;
    }

    ///// turnIndicator methods
    public void setTurnIndicatorEnabled(boolean enabled){
        turnIndicatorEnabled = enabled;
    }

    public void setTurnIndicator(boolean yourTurn){
        turnIndicator.setYourTurn(yourTurn);
    }

    ///// actionPanel methods
    public AbstractAction selectedAction(){
        return actionPanel.selectedAction();
    }

    //Below Boolean in argument stands for if action is active
    public void setActionsData(List<Pair<AbstractAction, Boolean>> actions) {
        actionPanel.setData(actions);
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

    ///// confirmButton and switchButton methods
    public void setConfirmButtonEnabled(boolean enabled){
        confirmButton.setEnabled(enabled);
    }

    public void setSwitchButtonEnabled(boolean enabled){
        switchButton.setEnabled(enabled);
    }

    public boolean confirmButtonPressed(){
        return confirmButton.buttonTouched();
    }

    public boolean switchButtonPressed(){
        return switchButton.buttonTouched();
    }

    ///// gameBoardPanel methods
    public void setData(GameBoard board){
        gameBoardPanel.setData(board);
    }

    //TODO: observer with getMarkedField()


    
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
        if(turnIndicatorEnabled) turnIndicator.render(sb);
        else sb.draw(placeShips, BIG_GAP, BattleshipsGame.HEIGHT - ACTION_PANEL_HEIGHT - BIG_GAP);

        gameBoardPanel.render(sb);
        shipsPanel.render(sb);
        timer.render(sb);
        actionPanel.render(sb);
        confirmButton.render(sb);
        if(!confirmButton.isEnabled()) sb.draw(inactiveButton, BIG_GAP + SMALL_GAP + ACTION_PANEL_WIDTH , BIG_GAP * 2 + GAME_BOARD_SIZE + BUTTON_HEIGHT + SMALL_GAP);
        switchButton.render(sb);
        if(!switchButton.isEnabled()) sb.draw(inactiveButton, BIG_GAP + SMALL_GAP + ACTION_PANEL_WIDTH , BIG_GAP * 2 + GAME_BOARD_SIZE);
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
