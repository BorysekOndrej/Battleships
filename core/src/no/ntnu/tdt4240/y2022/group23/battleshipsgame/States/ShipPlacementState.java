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
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShipPlacementState extends AbstractState {
    //Event listener
    private IBattleshipObserver selectedShipObserver;

    //Models to the game
    private GameBoard gameBoard;
    private ShipPlacements shipPlacements;
    private TimerPanel timer;
    private RemainingShipsPanel remainingShipsPanel;
    private int remainingShips;
    private List<Integer> remainingShipsKinds; //Contains the amount of ships remaining on each type

    //Selected ship
    private static boolean isShipSelected = FALSE; //Shows if a ship is currently selected
    private static boolean orientationCurrentShip = FALSE;
    private int squaresCurrentShip;
    private Coords coordsCurrentShip;
    private IShip selectedShip;

    protected ShipPlacementState(GameStateManager gsm) {
        super(gsm);
        timer = new TimerPanel();
        //timer.start(30); //Starts timer with 30 seconds
        selectedShipObserver = new SelectedShipObserver(this);
        gameBoard = new GameBoard(400,200); //Height and width placeholders
        shipPlacements = new ShipPlacements();
        remainingShipsPanel = new RemainingShipsPanel();
        remainingShipsPanel.addObserver(selectedShipObserver);
    }

    @Override
    public void handleInput(){
        remainingShipsPanel.handleInput();
    };

    //Updates the state every dt
    @Override
    public void update(float dt){
        handleInput();
        if (timer.runOut()){ //If timer runs out go to ViewMyBoard
            if (remainingShips == 0){
                goToViewMyBoard();
            } //Not checking opponents ships
            else{ //If the user has any remaining user he loses
                goToFinishedGame();
            }
        }
    };

    //Sets the variable selected ship to the selected by the user
    public void selectShip(){
        selectedShip = remainingShipsPanel.selectedShipType();
        if (selectedShip != null){
            isShipSelected = TRUE;
        }
    }

    //Changes state to view my board state
    private void goToViewMyBoard(){
        gsm.set(new ViewMineBoardState(gsm));
    };

    //Changes state to view my board state
    private void goToFinishedGame(){
        gsm.set(new FinishedGameState(gsm));
    };

    //Collocates a ship in the said space with said orientation
    private void collocateShip(Coords coords,int squares,boolean horizontal){
        RectangularShip ship = new RectangularShip(coords,squares,horizontal);
        try {
            shipPlacements.addShip(gameBoard.getWidth(),gameBoard.getHeight(),ship);
            gameBoard.set(coords,GameBoardField.SHIP);
        }
        catch (Exception IllegalArgumentException){
            int k = 0; //Could not position ship in gameBoard
        };
    };

    @Override
    public void render(SpriteBatch sb){
        //timer.render(sb);
    };

    @Override
    public void dispose(){};
}
