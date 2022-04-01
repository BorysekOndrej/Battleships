package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

import java.util.List;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class ShipPlacementState extends AbstractState {
    //Models to the game
    private GameBoard gameBoard;
    private ShipPlacements shipPlacements;
    private List<IShip> ships;
    private TimerPanel timer;

    //Selected ship
    private static boolean orientationCurrentShip = FALSE;
    private int squaresCurrentShip;

    protected ShipPlacementState(GameStateManager gsm) {
        timer = new TimerPanel();
        timer.start(30); //Starts timer with 30 seconds
        gameBoard = new GameBoard(400,200); //Height and width placeholders
        shipPlacements = new ShipPlacements();
        super(gsm);
    }

    protected void handleInput(){};

    //Updates the state every dt
    public void update(float dt){
        if (ships.size() == 4){ //If already added 4 ships go to my board
            goToViewMyBoard();
        }
    }

    //Changes state to view my board state
    private void goToViewMyBoard(){
        gsm.set(new ViewMineBoardState(gsm));
    }

    //Selects the ship in number x
    private void selectShip(int number){
    }

    //Colocates a ship in the said space with said orientation
    private void colocateShip(Coords coords,int squares,boolean horizontal){
        RectangularShip ship = new RectangularShip(coords,squares,horizontal);
        GameBoardField shipSpace = GameBoardField.SHIP;
        gameBoard.set(coords,shipSpace);
    }

    public void render(SpriteBatch sb){
        timer.render(SprieBatch sb);
    };

    public void dispose();
}
