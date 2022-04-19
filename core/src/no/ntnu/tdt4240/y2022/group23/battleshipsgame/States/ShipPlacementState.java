package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.GameBoardPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RemainingShipsPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Pair;
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

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Quartet;


public class ShipPlacementState extends AbstractState implements IGameBoardState{
    //Game API
    private GameAPIClient gameAPIClient;

    //Event listener
    private GameBoardObserver gameBoardObserver;

    //Models to the game
    private GameBoard gameBoard;
    private ShipPlacements shipPlacements;
    private Timer timer;
    private List<Pair<IShip, Integer>> remainingShips;

    private GameBoardPanel gameBoardPanel;
    private TimerPanel timerPanel;
    private RemainingShipsPanel remainingShipsPanel;

    protected ShipPlacementState(GameStateManager gsm) {
        super(gsm);
        gameAPIClient = new GameAPIClient(gsm.getNetworkClient());
        gameBoardObserver = new GameBoardObserver(this);

        //Model components
        gameBoard = new GameBoard(200,400); //Width and height placeholders
        shipPlacements = new ShipPlacements();
        timer = new Timer();
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
        //timer.start(30); //Starts timer with 30 seconds
    }

    @Override
    public void handleInput(){
        remainingShipsPanel.handleInput();
        gameBoardPanel.handleInput();
        timerPanel.handleInput();
    };

    @Override
    public void update(float dt) throws CommunicationTerminated {
        Quartet<Boolean, GameBoard, GameBoard, NextTurn > gameInitialization = gameAPIClient.receiveCanGameStart();
        handleInput();
        timerPanel.setData(timer);

        if (timerPanel.runOut()){ //If timer runs out go to ViewMyBoard
            if (gameInitialization.getValue0()){
                gameAPIClient.sendShipPlacement(shipPlacements);
                goToViewMyBoard();
            } //Not checking opponents ships
            else{ //If the user has any remaining ships to place, user he loses
                goToFinishedGame();
            }
        }
    };

    //Changes state to view my board state
    private void goToViewMyBoard(){
        gsm.set(new ViewMineBoardState(gsm));
    };

    //Changes state to finished game state
    private void goToFinishedGame(){
        gsm.set(new FinishedGameState(gsm,false));
    }

    //Subtracts a remaining ship from the type specified
    private void subtractRemaining(IShip selectedShip){
        for (Pair<IShip,Integer> pair: remainingShips){
            IShip currentShip = pair.getKey();
            if (selectedShip.getPositions().size() == currentShip.getPositions().size()){
                pair.setValue(pair.getValue() - 1);
                break;
            }
        }
    }

    @Override
    public void gameBoardTouch(Coords coords){
        IShip selectedShip = remainingShipsPanel.selectedShipType();
        try {
            subtractRemaining(selectedShip);

            shipPlacements.addShip(gameBoard.getWidth(),gameBoard.getHeight(),selectedShip);
            remainingShipsPanel.setData(remainingShips);

            gameBoard.set(coords,GameBoardField.SHIP);
            gameBoardPanel.setData(gameBoard);
        }
        catch (Exception IllegalArgumentException){
            //Could not position ship in gameBoard
        }
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
