package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;
import org.javatuples.Quartet;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.Radar;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.PlayStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Config;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameState;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.GameAPIClient;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.GameBoardObserver;

import java.util.List;

public class PlayState extends AbstractState implements IGameBoardState {
    enum View { MY_BOARD, OPPONENT_BOARD }
    enum WaitingFor {  GAME_START, MY_ACTION, MY_ACTION_RESULT, OPPONENT_ACTION_RESULT }

    //Game API
    private final GameAPIClient gameAPIClient;

    //GUI
    private final PlayStateGUI playStateGUI;

    private GameBoard myGameBoard;
    private GameBoard opponentGameBoard;

    private List<Pair<IShip, Integer>> myUnsunkShips;
    private List<Pair<IShip, Integer>> opponentUnsunkShips;

    //Action manager
    private final List<Pair<IAction, Boolean>> actions;
    private int turnsLeftForRadar;

    private NextTurn turnHolder;
    private View currentBoardView;

    //game state
    private WaitingFor waitingFor;

    private IAction selectedAction;

    protected PlayState(GameStateManager gsm, GameBoard myGameBoard) {
        super(gsm);
        gameAPIClient = new GameAPIClient(gsm.getNetworkClient());

        playStateGUI = new PlayStateGUI();
        playStateGUI.setSwitchButtonEnabled(false); // disabled till game can start
        playStateGUI.setShipPanelEnabled(false); // ships cannot be selected in this phase

        GameBoardObserver gameBoardObserver = new GameBoardObserver(this);
        playStateGUI.addGameBoardObserver(gameBoardObserver);

        actions = Config.actions();
        playStateGUI.setActions(actions);

        this.myGameBoard = myGameBoard;
        this.opponentGameBoard = new GameBoard(myGameBoard.getWidth(), myGameBoard.getHeight());

        myUnsunkShips = Config.remainingShips();
        opponentUnsunkShips = Config.remainingShips();

        turnHolder = NextTurn.OTHERS_TURN;
        waitingFor = WaitingFor.GAME_START;

        showMyBoard();
        playStateGUI.startTimer(Config.TURN_TIMEOUT);
        playStateGUI.setPause(true);
    }

    private void setWaitingFor(boolean thisPlayerWon) {
        switch (turnHolder) {
            case MY_TURN: { waitingFor = WaitingFor.MY_ACTION; break; }
            case OTHERS_TURN: { waitingFor = WaitingFor.OPPONENT_ACTION_RESULT; break; }
            case GAME_OVER: {
                if (thisPlayerWon) {
                    goToWonGame();
                } else {
                    goToLostGame();
                }
                break;
            }
        }
    }

    @Override
    public void handleInput() {
        playStateGUI.handleInput();

        if (waitingFor != WaitingFor.GAME_START && playStateGUI.switchButtonPressed()){
            if (currentBoardView == View.OPPONENT_BOARD){
                showMyBoard();
            } else {
                showOpponentBoard();
            }
        }

        if (waitingFor == WaitingFor.MY_ACTION && selectedAction != null && playStateGUI.confirmButtonPressed()) {
            gameAPIClient.sendAction(selectedAction);
            waitingFor = WaitingFor.MY_ACTION_RESULT;

            if (selectedAction instanceof Radar) {
                actions.set(1, actions.get(1).setAt1(false));
                playStateGUI.setActions(actions);
                turnsLeftForRadar = Config.RADAR_COOLDOWN;
            }
        }
    }

    private void processActionResult(GameState newGameState) {
        if (newGameState == null)
            return;

        turnHolder = newGameState.getNextTurn();
        setWaitingFor(newGameState.thisPlayerWon());

        if (newGameState.isThisPlayerBoard()) {
            myGameBoard = newGameState.getBoard();
            myUnsunkShips = newGameState.getUnsunkShips();
        } else {
            opponentGameBoard = newGameState.getBoard();
            opponentUnsunkShips = newGameState.getUnsunkShips();
        }

        setGUI();

        playStateGUI.startTimer(Config.TURN_TIMEOUT);
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        handleInput();
        playStateGUI.update(dt);

        switch (waitingFor) {
            case GAME_START: {
                Quartet<Boolean, GameBoard, GameBoard, NextTurn> serverInfo = gameAPIClient.receiveCanGameStart();
                if (!serverInfo.getValue0())
                    break;

                myGameBoard = serverInfo.getValue1();
                opponentGameBoard = serverInfo.getValue2();
                turnHolder = serverInfo.getValue3();
                setWaitingFor(false);

                setGUI();

                playStateGUI.setSwitchButtonEnabled(true);
                playStateGUI.setPause(false);
                break;
            }
            case MY_ACTION: {
                if (playStateGUI.runOut()) {
                    waitingFor = WaitingFor.MY_ACTION_RESULT;
                    gameAPIClient.sendTimeout();
                    playStateGUI.startTimer(Config.TURN_TIMEOUT);
                }
                break;
            }
            case MY_ACTION_RESULT: {
                GameState newGameState = gameAPIClient.receiveMyActionResult();
                processActionResult(newGameState);
                break;
            }
            case OPPONENT_ACTION_RESULT: {
                GameState newGameState = gameAPIClient.receiveOpponentActionResult();
                processActionResult(newGameState);

                if (newGameState != null && --turnsLeftForRadar == 0) {
                    actions.set(1, actions.get(1).setAt1(true));
                    playStateGUI.setActions(actions);
                }

                break;
            }
        }
    }

    @Override
    public void gameBoardTouch(Coords coords) {
        selectedAction = playStateGUI.selectedAction();

        if (waitingFor == WaitingFor.MY_ACTION && selectedAction != null) {
            selectedAction.setCoords(coords);
        }
    }

    private void setGUI() {
        boolean userCanPerformAction = currentBoardView == View.OPPONENT_BOARD && waitingFor == WaitingFor.MY_ACTION;
        playStateGUI.setGameBoardPanelEnabled(userCanPerformAction);
        playStateGUI.setConfirmButtonEnabled(userCanPerformAction);

        playStateGUI.setTurnIndicator(turnHolder);
        playStateGUI.setGameBoard(currentBoardView == View.MY_BOARD ? myGameBoard : opponentGameBoard);
        playStateGUI.setShips(currentBoardView == View.MY_BOARD ? myUnsunkShips : opponentUnsunkShips);
    }

    private void showOpponentBoard() {
        currentBoardView = View.OPPONENT_BOARD;
        setGUI();
    }

    private void showMyBoard() {
        currentBoardView = View.MY_BOARD;
        setGUI();
    }

    private void goToWonGame() {
        gsm.set(new FinishedGameState(gsm,true));
    }

    private void goToLostGame() {
        gsm.set(new FinishedGameState(gsm,false));
    }


    @Override
    public void render(SpriteBatch sb) {
        playStateGUI.render(sb);
    }

    @Override
    public void dispose() {
        playStateGUI.dispose();
    }
}
