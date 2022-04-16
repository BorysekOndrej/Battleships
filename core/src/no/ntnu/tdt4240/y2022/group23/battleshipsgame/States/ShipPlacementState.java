package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.GameBoardPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RemainingShipsPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Pair;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.GameBoardObserver;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class ShipPlacementState extends AbstractState implements IGameBoardState{

    //Event listener
    private GameBoardObserver gameBoardObserver;

    //Models to the game
    private GameBoard gameBoard;
    private GameBoardPanel gameBoardPanel;
    private ShipPlacements shipPlacements;
    private TimerPanel timer;
    private RemainingShipsPanel remainingShipsPanel;
    private List<Pair<IShip, Integer>> remainingShips;

    protected ShipPlacementState(GameStateManager gsm) {
        super(gsm);
        timer = new TimerPanel();
        //timer.start(30); //Starts timer with 30 seconds
        gameBoardObserver = new GameBoardObserver(this);
        gameBoard = new GameBoard(200,400); //Width and height placeholders
        gameBoardPanel = new GameBoardPanel();
        gameBoardPanel.addGameBoardObserver(gameBoardObserver);
        shipPlacements = new ShipPlacements();
        remainingShipsPanel = new RemainingShipsPanel();
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
    }

    @Override
    public void handleInput(){
        remainingShipsPanel.handleInput();
        gameBoardPanel.handleInput();
        timer.handleInput();
    };

    @Override
    public void update(float dt){
        handleInput();
        if (timer.runOut()){ //If timer runs out go to ViewMyBoard
            if (remainingShips.isEmpty()){
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
        gsm.set(new FinishedGameState(gsm));
    }

    //Subtracts a remaining ship from the type specified
    private void subtractRemaining(int shipType){
        for (Pair<IShip,Integer> pair: remainingShips){
            IShip ship = pair.getKey();
            if (ship.getType() == shipType){
                pair.setValue(pair.getValue() - 1);
                break;
            }
        }
    }

    @Override
    public void gameBoardTouch(Coords coords){
        IShip ship = remainingShipsPanel.selectedShipType();
        try {
            int shipType = ship.getType();
            subtractRemaining(shipType);

            shipPlacements.addShip(gameBoard.getWidth(),gameBoard.getHeight(),ship);
            remainingShipsPanel.setData(remainingShips);

            gameBoard.set(coords,GameBoardField.SHIP);

            //Need to add to GameBoardPanel
        }
        catch (Exception IllegalArgumentException){
            //Could not position ship in gameBoard
        }
    }

    @Override
    public void render(SpriteBatch sb){
        //timer.render(sb);
    };

    @Override
    public void dispose(){};
}
