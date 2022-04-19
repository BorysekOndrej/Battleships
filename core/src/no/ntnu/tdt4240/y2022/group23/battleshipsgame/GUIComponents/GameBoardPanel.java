package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Rectangle;


import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.GameBoardObserver;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;

public class GameBoardPanel implements IRenderable {
    private GameBoardObserver observer;

    private static final int FIELD_SIZE = 86;
    private static final int GAMEBOARD_OFFSET = 2;
    private static final int GAMEBOARD_ROWS = 10;

    private final GameBoard gameBoard;

    private Coords markedFieldCoords = null;

    private final Texture gameBoardTex;

    private final Texture unknownField;
    private final Texture waterField;
    private final Texture hitField;
    private final Texture sunkField;
    private final Texture shipField;
    private final Texture markedField;

    //private final Rectangle bonds;

    private int xPos;
    private int yPos;

    public GameBoardPanel(int x, int y){
        xPos = x;
        yPos = y;
        gameBoardTex = new Texture("game_board/gameboard.png");
        unknownField = new Texture("game_board/unknown.png");
        waterField = new Texture("game_board/water.png");
        hitField = new Texture("game_board/hit.png");
        sunkField = new Texture("game_board/sunk.png");
        shipField = new Texture("game_board/ship.png");
        markedField = new Texture("game_board/marked_field.png");

        //bonds = new Rectangle(xCord + GAMEBOARD_OFFSET + FIELD_SIZE, yCord - GAMEBOARD_OFFSET, gameBoardTex.getWidth() - 2*GAMEBOARD_OFFSET - FIELD_SIZE, gameBoardTex.getHeight()- 2*GAMEBOARD_OFFSET - FIELD_SIZE);

        gameBoard = new GameBoard(GAMEBOARD_ROWS, GAMEBOARD_ROWS);
        gameBoard.set(new Coords(0,0), GameBoardField.SUNK);
        gameBoard.set(new Coords(1,0), GameBoardField.SUNK);
        gameBoard.set(new Coords(2,0), GameBoardField.SUNK);
        gameBoard.set(new Coords(3,0), GameBoardField.WATER);
        gameBoard.set(new Coords(4,0), GameBoardField.WATER);
        gameBoard.set(new Coords(5,0), GameBoardField.HIT);
        gameBoard.set(new Coords(0,1), GameBoardField.WATER);
        gameBoard.set(new Coords(4,4), GameBoardField.WATER);

    }

    public void setPosition(int x, int y){
        xPos = x;
        yPos = y;
    }

    public Coords getFieldCoords(int x, int y){
        int xRelative = x - xPos;
        int yRelative = y - (BattleshipsGame.HEIGHT - yPos - gameBoardTex.getHeight());
        Coords result = new Coords((xRelative - GAMEBOARD_OFFSET) / FIELD_SIZE - 1, (yRelative - GAMEBOARD_OFFSET) / FIELD_SIZE - 1);
        if(coordsValid(result)) return result;
        else  {
            //System.out.println("Coords out of bonds");
            return null;
        }
    }

    public boolean coordsValid(Coords coords){
        return (coords.x >= 0 && coords.x < GAMEBOARD_ROWS) && (coords.y >= 0 && coords.y < GAMEBOARD_ROWS);
    }


    public void drawField(GameBoardField field, SpriteBatch sb, int xCord, int yCord){
        switch (field) {
            case UNKNOWN:
                sb.draw(unknownField, xCord, yCord);
                break;
            case WATER:
                sb.draw(waterField, xCord, yCord);
                break;
            case HIT:
                sb.draw(hitField, xCord, yCord);
                break;
            case SUNK:
                sb.draw(sunkField, xCord, yCord);
                break;
            case SHIP:
                sb.draw(shipField, xCord, yCord);
                break;
            default:
        }
    }

    //Adds observer to the observable object
    public void addGameBoardObserver(GameBoardObserver observer){
        this.observer = observer;
    }

    public void drawMarkedField(SpriteBatch sb, Coords coords){
        if(coords != null){
            int xLocus = xPos + GAMEBOARD_OFFSET + FIELD_SIZE * (coords.x + 1) - 2;
            int yLocus = yPos + GAMEBOARD_OFFSET + FIELD_SIZE * (GAMEBOARD_ROWS - 1 - coords.y) - 2;
            sb.draw(markedField, xLocus, yLocus);
        }
    }

    public void handleInput(){
        if(Gdx.input.justTouched()){
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            markedFieldCoords = getFieldCoords(x, y);
            if (markedFieldCoords != null) observer.notice(markedFieldCoords); // System.out.println(markedFieldCoords.toString()); // debugging only
        }
    }

    public void update(float dt){
        handleInput();
    }

    public void render(SpriteBatch sb){
        sb.draw(gameBoardTex, xPos, yPos);

        for(int i = 0; i < GAMEBOARD_ROWS; i++){
            for(int j = 0; j < GAMEBOARD_ROWS; j++){
                GameBoardField actualField = gameBoard.get(new Coords(i, j));
                int xLocus = xPos + GAMEBOARD_OFFSET + FIELD_SIZE * (i + 1);
                int yLocus = yPos + GAMEBOARD_OFFSET + FIELD_SIZE * (GAMEBOARD_ROWS - 1 - j);
                drawField(actualField, sb, xLocus, yLocus);
            }
        }
        drawMarkedField(sb, markedFieldCoords);
    }

    public void dispose(){
        gameBoardTex.dispose();
        unknownField.dispose();
        waterField.dispose();
        hitField.dispose();
        sunkField.dispose();
        shipField.dispose();
        markedField.dispose();
    }
    public int getGameBoardHeight(){
        return gameBoardTex.getHeight();
    }

    public int getGameBoardWidth(){
        return gameBoardTex.getWidth();
    }

    public Coords getMarkedField(){
        return markedFieldCoords;
    }

}
