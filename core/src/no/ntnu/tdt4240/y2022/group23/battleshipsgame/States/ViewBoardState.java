package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.ActionPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.GameBoardPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RemainingShipsPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.GameBoardObserver;

import java.util.List;

public class ViewBoardState extends AbstractState implements IGameBoardState {
    //Event listener
    private GameBoardObserver gameBoardObserver;

    //Action Management
    protected ActionPanel actionPanel;

    protected List<IShip> ships;

    protected GameBoard myGameBoard;
    protected GameBoard opponentGameBoard;

    protected ShipPlacements shipPlacements;

    protected GameBoardPanel gameBoardPanel;
    protected RemainingShipsPanel remainingShipsPanel;
    protected TimerPanel timer;

    protected ViewBoardState(GameStateManager gsm) {
        super(gsm);
        timer = new TimerPanel();
        //Either retrieve the gameBoard from past iterations, or use the one from GameState
        //in that case it would be good to store it in the gsm
        Triplet<boolean, GameBoard, GameBoard> serverInfo;
        gameBoardPanel.addGameBoardObserver(gameBoardObserver);
        gameBoardObserver = new GameBoardObserver(this);
    }

    @Override
    public void handleInput() {
        gameBoardPanel.handleInput();
        remainingShipsPanel.handleInput();
        timer.handleInput();
    }

    @Override
    public void update(float dt) {
        if (shipPlacements.allSunk(gameBoard)){
            goToFinishedGame();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        throw new UnsupportedOperationException("not implemented");
    }

    //Changes state to finished game state
    private void goToFinishedGame(){
        //Show winner... how?
        gsm.set(new FinishedGameState(gsm));
    };

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void gameBoardTouch(Coords coords) {
        //IF IT IS MY TURN -> MAYBE SHOULD BE ON VIEW OPPONENT BOARD STATE
        IAction action = actionPanel.selectedActionType();
        action.affect(shipPlacements,gameBoard);
    }
}
