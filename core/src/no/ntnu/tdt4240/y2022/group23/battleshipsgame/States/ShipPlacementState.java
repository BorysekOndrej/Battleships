package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RemainingShipsPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

import java.awt.Button;
import java.util.List;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ShipPlacementState extends AbstractState {
    //Models to the game
    private GameBoard gameBoard;
    private ShipPlacements shipPlacements;
    private TimerPanel timer;
    private int remainingShips;
    private List<Integer> remainingShipsKinds; //Contains the amount of ships remaining on each type
    private ShipPlacementPanel shipPlacementPanel; //Displays the remaining ships to place

    //Selected ship
    private static boolean isShipSelected = FALSE; //Shows if a ship is currently selected
    private static boolean orientationCurrentShip = FALSE;
    private int squaresCurrentShip;
    private Coords coordsCurrentShip;

    protected ShipPlacementState(GameStateManager gsm) {
        timer = new TimerPanel();
        timer.start(30); //Starts timer with 30 seconds
        gameBoard = new GameBoard(400,200); //Height and width placeholders
        shipPlacements = new ShipPlacements();
        super(gsm);
    }

    @Override
    public void handleInput(){
    };

    //Updates the state every dt
    @Override
    public void update(float dt){
        if (timer.runOut()){ //If timer runs out go to ViewMyBoard
            if (remainingShips == 0){
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

    //Selects the ship in number x
    public Integer selectShip(int number){
        return remainingShipsKinds.get(number);
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
        timer.render(sb);
    };

    @Override
    public void dispose(){};
}
