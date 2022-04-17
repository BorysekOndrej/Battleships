package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Quartet;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.ActionPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.GameBoardPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RemainingShipsPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.GameAPIClient;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.GameBoardObserver;

import java.util.List;

public class ViewBoardState extends AbstractState implements IGameBoardState {
    //Game API
    private GameAPIClient gameAPIClient;

    //Event listener
    private GameBoardObserver gameBoardObserver;

    //Action Management
    protected ActionPanel actionPanel;

    protected List<IShip> ships;

    protected GameBoard myGameBoard;
    protected GameBoard opponentGameBoard;

    protected NextTurn nextTurnHolder;

    protected ShipPlacements shipPlacements;

    protected GameBoardPanel gameBoardPanel;
    protected RemainingShipsPanel remainingShipsPanel;
    protected TimerPanel timer;

    protected boolean actionDone = false;

    protected ViewBoardState(GameStateManager gsm) {
        super(gsm);
        timer = new TimerPanel(0,0); //x and y are placeholders
        gameAPIClient = new GameAPIClient(gsm.getNetworkClient());

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
    public void update(float dt) throws CommunicationTerminated {
        Quartet<Boolean, GameBoard, GameBoard, NextTurn> serverInfo = gameAPIClient.receiveCanGameStart();

        myGameBoard = serverInfo.getValue1();
        opponentGameBoard = serverInfo.getValue2();
        nextTurnHolder = serverInfo.getValue3();

        if (timer.runOut()){
            gameAPIClient.sendTimeout();
        }

        if (actionDone){
            gameAPIClient.receiveMyActionResult();
        }

        if (shipPlacements.allSunk(opponentGameBoard)){
            goToWonGame();
            gameAPIClient.endCommunication();
        }

        else if (shipPlacements.allSunk(myGameBoard)){
            goToLostGame();
            gameAPIClient.endCommunication();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        throw new UnsupportedOperationException("not implemented");
    }

    //Changes state to finished game state as the winner
    private void goToWonGame(){
        gsm.set(new FinishedGameState(gsm,true));
    }

    //Changes state to finished game state as the loser
    private void goToLostGame(){
        gsm.set(new FinishedGameState(gsm,false));
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void gameBoardTouch(Coords coords) {
        //IF IT IS MY TURN -> MAYBE SHOULD BE ON VIEW OPPONENT BOARD STATE
        IAction action = actionPanel.selectedActionType();
        gameAPIClient.sendAction(action);
        actionDone = true;
    }
}
