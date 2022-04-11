package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RemainingShipsPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.IBattleshipObserver;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.SelectedShipObserver;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;

public class ShipPlacementState extends AbstractState {


    //Event listener
    private IBattleshipObserver selectedShipObserver;

    //Models to the game
    private GameBoard gameBoard;
    private ShipPlacements shipPlacements;
    private TimerPanel timer;
    private RemainingShipsPanel remainingShipsPanel;
    private List<Pair<IShip, Integer>> remainingShips;

    protected ShipPlacementState(GameStateManager gsm) {
        super(gsm);
        timer = new TimerPanel();
        //timer.start(30); //Starts timer with 30 seconds
        selectedShipObserver = new SelectedShipObserver(this);
        gameBoard = new GameBoard(200,400); //Width and height placeholders
        shipPlacements = new ShipPlacements();
        remainingShipsPanel = new RemainingShipsPanel();
        remainingShipsPanel.addObserver(selectedShipObserver);
    }

    @Override
    public void handleInput(){
        remainingShipsPanel.handleInput();
    };

    @Override
    public void update(float dt){
        handleInput();
        if (timer.runOut()){ //If timer runs out go to ViewMyBoard
            if (remainingShips.isEmpty()){
                goToViewMyBoard();
            } //Not checking opponents ships
            else{ //If the user has any remaining user he loses
                goToFinishedGame();
            }
        }
    };

    //Changes state to view my board state
    private void goToViewMyBoard(){
        gsm.set(new ViewMineBoardState(gsm));
    };

    //Changes state to view my board state
    private void goToFinishedGame(){
        gsm.set(new FinishedGameState(gsm));
    };

    //Collocates a ship in the said space with said orientation
    private void collocateShip(){
        IShip ship = remainingShipsPanel.selectedShipType();
        Coords coords = new Coords(0,0); //Coordinates where to collocate the ship, currently a placeholder
        try {
            shipPlacements.addShip(gameBoard.getWidth(),gameBoard.getHeight(),ship);
            gameBoard.set(coords,GameBoardField.SHIP);
        }
        catch (Exception IllegalArgumentException){
            //Could not position ship in gameBoard
        };
    };

    @Override
    public void render(SpriteBatch sb){
        //timer.render(sb);
    };

    @Override
    public void dispose(){};
}
