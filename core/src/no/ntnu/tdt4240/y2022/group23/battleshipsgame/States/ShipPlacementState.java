package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.ShipPlacementStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Config;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.GameAPIClient;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.GameBoardObserver;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;


public class ShipPlacementState extends AbstractState implements IGameBoardState{
    //Game API
    private GameAPIClient gameAPIClient;

    //Event listener
    private GameBoardObserver gameBoardObserver;

    //Models to the game
    private GameBoard gameBoard;
    private ShipPlacements shipPlacements;
    private List<Pair<IShip, Integer>> remainingShips;

    private ShipPlacementStateGUI shipPlacementStateGUI;

    private IShip selectedShip = null;
    private GameBoard gameBoardWithNewShip = null;
    private boolean shipPlaced = false;
    private Coords lastTouchedCoords = null;

    protected ShipPlacementState(GameStateManager gsm) {
        super(gsm);
        gameAPIClient = new GameAPIClient(gsm.getNetworkClient());
        gameBoardObserver = new GameBoardObserver(this);

        //Model components
        shipPlacements = new ShipPlacements();
        gameBoard = new GameBoard(Config.GAME_BOARD_WIDTH, Config.GAME_BOARD_HEIGHT);

        //Create remaining ships list
        remainingShips = Config.remainingShips();

        //GUI Components
        shipPlacementStateGUI = new ShipPlacementStateGUI();
        shipPlacementStateGUI.setShips(remainingShips);

        shipPlacementStateGUI.addGameBoardObserver(gameBoardObserver);
        shipPlacementStateGUI.startTimer(Config.SHIP_PLACEMENT_TIMEOUT);
    }

    @Override
    public void handleInput() {
        if (!shipPlacementStateGUI.runOut()) { //If the user has time still
            shipPlacementStateGUI.handleInput();
            if (shipPlacementStateGUI.confirmButtonPressed()) {
                collocateShip();
            }
        }
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        shipPlacementStateGUI.update(dt);
        handleInput();

        if (!hasShipRemainings()){
            gameAPIClient.sendShipPlacement(shipPlacements);
            goToPlayState();
        }

        if (shipPlacementStateGUI.runOut()){ //If timer runs out go to ViewMyBoard
            if (hasShipRemainings()){
                try{
                    gameAPIClient.endCommunication(); //Catch from the other user (?)
                }
                catch (CommunicationTerminated exception){
                    goToFinishedGame();
                }
            }
        }
    }

    //Changes state to view my board state
    private void goToPlayState(){
        gsm.set(new PlayState(gsm, gameBoard));
    }

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
            if (selectedShip.equals(currentShip)){ // same type
                remainingShips.set(i,currentPair.setAt1(currentPair.getValue1() - 1));
                break;
            }
        }
    }

    //Checks if the user has any ship of that type remaining
    private boolean haveShipLeft(IShip selectedShip){
        for (int i = 0; i <= remainingShips.size() ; i++){
            Pair<IShip,Integer> currentPair = remainingShips.get(i);
            IShip currentShip = currentPair.getValue0();
            if (selectedShip.getPositions().size() == currentShip.getPositions().size()){ //Same type
                return (currentPair.getValue1() != 0);
            }
        }
        return false;
    }

    @Override
    public void gameBoardTouch(Coords coords){
        selectedShip = shipPlacementStateGUI.selectedShipType();

        if (selectedShip != null){
            if (haveShipLeft(selectedShip)){ //If there are remaining ships of that type
                if(coords.equals(lastTouchedCoords)){
                    selectedShip.rotateClockwise();
                }
                else{
                    selectedShip.placeShip(coords);
                    lastTouchedCoords = new Coords(coords.x,coords.y);
                }
                shipPlaced = true;
                gameBoardWithNewShip = new GameBoard(gameBoard.getWidth(),gameBoard.getHeight(),shipPlacements,selectedShip);
                shipPlacementStateGUI.setGameBoard(gameBoardWithNewShip);
            }
        }
    }

    //Adds ship to ship placement
    private void collocateShip(){
        if (shipPlaced){
            try {
                IShip placedShip = selectedShip.copy();
                shipPlacements.addShip(gameBoard.getWidth(),gameBoard.getHeight(),placedShip);

                subtractRemaining(selectedShip);
                shipPlacementStateGUI.setShips(remainingShips);

                gameBoard = new GameBoard(gameBoardWithNewShip);
                selectedShip = null;
                gameBoardWithNewShip = null;
                lastTouchedCoords = null;
                shipPlaced = false;
                shipPlacementStateGUI.setGameBoard(gameBoard);
            }
            catch (Exception IllegalArgumentException){
                //Could not position ship in ship placement
                selectedShip = null;
                gameBoardWithNewShip = null;
                shipPlaced = false;
                lastTouchedCoords = null;
                shipPlacementStateGUI.setGameBoard(gameBoard); //Eliminates the showing of red
            }
        }
    }

    @Override
    public void render(SpriteBatch sb){
        shipPlacementStateGUI.render(sb);
    }

    @Override
    public void dispose(){
        shipPlacementStateGUI.dispose();
    }
}
