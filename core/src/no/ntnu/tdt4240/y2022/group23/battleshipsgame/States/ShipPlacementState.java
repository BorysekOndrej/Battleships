package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.GameBoardPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RemainingShipsPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Timer;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.GameAPIClient;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.GameBoardObserver;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;
import org.javatuples.Quartet;


public class ShipPlacementState extends AbstractState implements IGameBoardState{
    //Game API
    private GameAPIClient gameAPIClient;

    //Event listener
    private GameBoardObserver gameBoardObserver;

    //Models to the game
    private GameBoard gameBoard;
    private ShipPlacements shipPlacements;
    private List<Pair<IShip, Integer>> remainingShips;

    private GameBoardPanel gameBoardPanel;
    private TimerPanel timerPanel;
    private RemainingShipsPanel remainingShipsPanel;

    private IShip selectedShip = null;
    private GameBoard gameBoardWithNewShip = null;

    protected ShipPlacementState(GameStateManager gsm) {
        super(gsm);
        gameAPIClient = new GameAPIClient(gsm.getNetworkClient());
        gameBoardObserver = new GameBoardObserver(this);

        //Model components
        gameBoard = new GameBoard(200,400); //Width and height placeholders
        shipPlacements = new ShipPlacements();
        remainingShips = new ArrayList<>();

        //Create remaining ships list
        //4 square ship
        remainingShips.add(new Pair<>(new RectangularShip(new Coords(0,0),4,true),4));
        //3 square ship
        remainingShips.add(new Pair<>(new RectangularShip(new Coords(0,0),3,true),3));
        //2 square ship
        remainingShips.add(new Pair<>(new RectangularShip(new Coords(0,0),2,true),2));
        //1 square ship
        remainingShips.add(new Pair<>(new RectangularShip(new Coords(0,0),1,true),1));

        //GUI Components
        timerPanel = new TimerPanel(BattleshipsGame.WIDTH - 262 - 37, 125 + 37); //x and y are placeholders
        gameBoardPanel = new GameBoardPanel(200, 400); //x and y are placeholders
        remainingShipsPanel = new RemainingShipsPanel(37, 1100);

        gameBoardPanel.addGameBoardObserver(gameBoardObserver);
        timerPanel.startTimer(30);//Starts timer with 30 seconds
        //Should be the timer
    }

    @Override
    public void handleInput(){
        //Button confirmation -> collocateShip();

        if (!timerPanel.runOut()){ //If the user has time still
            if (Gdx.input.justTouched() && selectedShip != null){
                rotateShip();
            }
            remainingShipsPanel.handleInput();
            gameBoardPanel.handleInput();
            timerPanel.handleInput();
        }
    };

    @Override
    public void update(float dt) throws CommunicationTerminated {
        Quartet<Boolean, GameBoard, GameBoard, NextTurn > gameInitialization = gameAPIClient.receiveCanGameStart();
        handleInput();

        if (timerPanel.runOut()){ //If timer runs out go to ViewMyBoard
            if (hasShipRemainings()){
                goToFinishedGame();
                gameAPIClient.endCommunication(); //Catch from the other user (?)
            }
            else if (gameInitialization.getValue0()){
                gameAPIClient.sendShipPlacement(shipPlacements);
                goToViewMyBoard();
            }
        }
    }

    //Changes state to view my board state
    private void goToViewMyBoard(){
        gsm.set(new ViewMineBoardState(gsm));
    };

    //Changes state to finished game state
    private void goToFinishedGame(){
        gsm.set(new FinishedGameState(gsm,false));
    }

    //Checks if user has any remaining ship
    private boolean hasShipRemainings(){
        for (Pair<IShip,Integer> pair: remainingShips){
            if (pair.getValue1() != 0){
                return true;
            }
        }
        return false;
    }

    //Subtracts a remaining ship from the type specified
    private void subtractRemaining(IShip selectedShip){
        for (int i = 0; i <= remainingShips.size() ; i++){
            Pair<IShip,Integer> currentPair = remainingShips.get(i);
            IShip currentShip = currentPair.getValue0();
            if (selectedShip.getPositions().size() == currentShip.getPositions().size()){ //Same type
                remainingShips.set(i,currentPair.setAt1(currentPair.getValue1() - 1));
                break;
            }
        }
    }

    @Override
    public void gameBoardTouch(Coords coords){
        selectedShip = remainingShipsPanel.selectedShipType();
        //Change coords of ship to actual coords
        //selectedShip.setPositions();

        if (selectedShip != null){
            gameBoardWithNewShip = new GameBoard(gameBoard.getWidth(),gameBoard.getHeight(),shipPlacements,selectedShip);
            gameBoardPanel.setData(gameBoardWithNewShip);
        }
    }

    //Adds ship to ship placement
    private void collocateShip(){
        if (selectedShip != null){
            try {
                shipPlacements.addShip(gameBoard.getWidth(),gameBoard.getHeight(),selectedShip);

                subtractRemaining(selectedShip);
                remainingShipsPanel.setData(remainingShips);

                gameBoard = new GameBoard(gameBoardWithNewShip);
                selectedShip = null;
                gameBoardWithNewShip = null;
                gameBoardPanel.setData(gameBoard);
            }
            catch (Exception IllegalArgumentException){
                //Could not position ship in ship placement
                selectedShip = null;
                gameBoardWithNewShip = null;
                gameBoardPanel.setData(gameBoard); //Eliminates the showing of red
            }
        }
    }

    //Rotates selected ship
    private void rotateShip(){
        selectedShip.rotateClockwise();
        gameBoardWithNewShip = new GameBoard(gameBoard.getWidth(),gameBoard.getHeight(),shipPlacements,selectedShip);
        gameBoardPanel.setData(gameBoardWithNewShip);
    }

    @Override
    public void render(SpriteBatch sb){
        gameBoardPanel.render(sb);
        remainingShipsPanel.render(sb);
        timerPanel.render(sb);
    };

    @Override
    public void dispose(){
        gameBoardPanel.dispose();
        remainingShipsPanel.dispose();
        timerPanel.dispose();
    };
}
