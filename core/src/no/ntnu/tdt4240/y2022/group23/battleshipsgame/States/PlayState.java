package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;
import org.javatuples.Quartet;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.AbstractAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.Radar;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.SingleShot;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.PlayStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameState;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.GameAPIClient;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.GameBoardObserver;

import java.util.ArrayList;
import java.util.List;

public class PlayState extends AbstractState implements IGameBoardState {
    //Game API
    private GameAPIClient gameAPIClient;
    private Quartet<Boolean, GameBoard, GameBoard, NextTurn> serverInfo;

    //GUI
    private PlayStateGUI playStateGUI;

    //Event listener
    private GameBoardObserver gameBoardObserver;

    private List<IShip> ships;

    //Action manager
    private List<Pair<AbstractAction, Boolean>> actions;
    private int turnsLeftForRadar;

    private GameBoard myGameBoard;
    private GameBoard opponentGameBoard;

    private NextTurn turnHolder;
    private boolean isMyBoardShowing = true;

    //Server booleans
    private boolean canGameStart = false;
    private boolean actionSent = false;
    private boolean timeOutSent = false;
    private boolean myActionResultReceived = false;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        gameAPIClient = new GameAPIClient(gsm.getNetworkClient());
        //serverInfo = new Quartet<>(false,null,null,null); //MAYBE needed

        playStateGUI = new PlayStateGUI();

        gameBoardObserver = new GameBoardObserver(this);
        playStateGUI.addGameBoardObserver(gameBoardObserver);

        actions = new ArrayList<>();

        //Adding the shot
        actions.add(new Pair<>(new SingleShot(new Coords(0,0)),true));
        //Adding the radar
        actions.add(new Pair<>(new Radar(new Coords(0,0)),true));
    }

    @Override
    public void handleInput() {
        playStateGUI.handleInput();
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        playStateGUI.update(dt);

        if (!canGameStart){ //Game can not start
            serverInfo = gameAPIClient.receiveCanGameStart();
            if (serverInfo.getValue0()){
                canGameStart = true;
            }
        }

        else { //Game has started
            myGameBoard = serverInfo.getValue1();
            opponentGameBoard = serverInfo.getValue2();
            turnHolder = serverInfo.getValue3();

            playStateGUI.setTurnIndicator(turnHolder);

            if (turnHolder == NextTurn.MY_TURN){
                playStateGUI.setEnabled(true);
                if (isMyBoardShowing){ //You can not touch your game board
                    playStateGUI.setGameBoardPanelEnabled(false);
                }
            }
            else if (turnHolder == NextTurn.OTHERS_TURN){
                playStateGUI.setEnabled(false);
            }
            else{} //What to do if game over?

            if (playStateGUI.switchButtonPressed()){
                if (isMyBoardShowing){
                    showEnemyBoard();
                }
                else{
                    showMyBoard();
                }
            }

            //If timer runs out and has no timeOut or action signal has been sent
            if (playStateGUI.runOut() && !(timeOutSent || actionSent)){
                timeOutSent = true;
                gameAPIClient.sendTimeout();
            }

            //If action or timeOut signal has been set and the action result has not been received
            if ((actionSent || timeOutSent) && !myActionResultReceived){
                GameState newGameState = gameAPIClient.receiveMyActionResult();
                if (newGameState != null){
                    myActionResultReceived = true;

                    opponentGameBoard = newGameState.getBoard();
                    turnHolder = newGameState.getNextTurn();
                    ships = newGameState.getUnsunkShips(); //And then what?
                    //What about changed coords?
                    //playStateGUI.setShips(newGameState.getUnsunkShips());

                    if (newGameState.thisPlayerWon()){ //Could be undefined, how to catch?
                        goToWonGame();
                        gameAPIClient.endCommunication();
                    }
                }
            }

            //If the result of my action was received
            if (myActionResultReceived){
                GameState newGameState = gameAPIClient.receiveOpponentActionResult();
                if (newGameState != null){
                    myGameBoard = newGameState.getBoard();
                    turnHolder = newGameState.getNextTurn();
                    ships = newGameState.getUnsunkShips(); //And then what?
                    //What about changed coords?
                    //playStateGUI.setShips(newGameState.getUnsunkShips());

                    if (turnHolder == NextTurn.GAME_OVER){ //Is this the way of catching the possible undefine?
                        if (!newGameState.thisPlayerWon()){
                            goToLostGame();
                            gameAPIClient.endCommunication();
                        }   
                    }

                    if (turnsLeftForRadar == 0){ //Radar is ready
                        actions.set(1,new Pair<>(new Radar(new Coords(0,0)),true));
                    }
                    else if (turnsLeftForRadar>0){ //1 less turn for radar to be ready
                        turnsLeftForRadar--;
                    }

                    //Reset the server booleans
                    myActionResultReceived = false;
                    timeOutSent = false;
                    actionSent = false;
                }
            }
        }
    }

    @Override
    public void gameBoardTouch(Coords coords) {
        IAction action = playStateGUI.selectedAction();
        if ((!actionSent && !timeOutSent) && action != null){
            action.setCoords(coords);
            gameAPIClient.sendAction(action);
            actionSent = true;
            if (action.equals(new Radar(coords))){ //Action is a radar todo: check equals
                actions.set(1,new Pair<>(new Radar(new Coords(0,0)),false));
                turnsLeftForRadar = 5;
            }
        }
    }

    private void showEnemyBoard(){
        playStateGUI.setGameBoard(opponentGameBoard);
        isMyBoardShowing = false;
    }

    private void showMyBoard(){
        playStateGUI.setGameBoard(myGameBoard);
        isMyBoardShowing = true;
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
    public void render(SpriteBatch sb) {
        playStateGUI.render(sb);
    }

    @Override
    public void dispose() {
        playStateGUI.dispose();
    }
}
