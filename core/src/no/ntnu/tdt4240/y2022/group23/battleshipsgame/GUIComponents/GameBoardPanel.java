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
    private static final int GAME_BOARD_OFFSET = 2;
    private static final int GAME_BOARD_ROWS = 10;

    private GameBoard gameBoard;

    private Coords markedFieldCoords = null;

    private final Texture gameBoardTex;

    private final Texture unknownField;
    private final Texture waterField;
    private final Texture hitField;
    private final Texture sunkField;
    private final Texture shipField;
    private final Texture collideField;
    private final Texture markedField;

    private final int xPos;
    private final int yPos;

    private boolean enabled;

    public GameBoardPanel(int x, int y){
        xPos = x;
        yPos = y;
        gameBoardTex = new Texture("game_board/gameboard.png");
        unknownField = new Texture("game_board/unknown.png");
        waterField = new Texture("game_board/water.png");
        hitField = new Texture("game_board/hit.png");
        sunkField = new Texture("game_board/sunk.png");
        shipField = new Texture("game_board/ship.png");
        collideField = new Texture("game_board/collide.png");
        markedField = new Texture("game_board/marked_field.png");
        enabled = true;

        //DEBUGGING ONLY
        gameBoard = new GameBoard(GAME_BOARD_ROWS, GAME_BOARD_ROWS);
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public void setData(GameBoard board){
        this.gameBoard = board;
    }

    public Coords getFieldCoords(int x, int y){
        int xRelative = x - xPos;
        int yRelative = y - (BattleshipsGame.HEIGHT - yPos - gameBoardTex.getHeight());
        Coords result = new Coords((xRelative - GAME_BOARD_OFFSET) / FIELD_SIZE - 1, (yRelative - GAME_BOARD_OFFSET) / FIELD_SIZE - 1);
        if(coordsValid(result)) return result;
        else  {
            return null;
        }
    }

    private boolean coordsValid(Coords coords){
        return (coords.x >= 0 && coords.x < GAME_BOARD_ROWS) && (coords.y >= 0 && coords.y < GAME_BOARD_ROWS);
    }

    private void drawField(GameBoardField field, SpriteBatch sb, int xCord, int yCord){
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
            case COLLIDE:
                sb.draw(collideField, xCord, yCord);
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
            int xLocus = xPos + GAME_BOARD_OFFSET + FIELD_SIZE * (coords.x + 1) - 2;
            int yLocus = yPos + GAME_BOARD_OFFSET + FIELD_SIZE * (GAME_BOARD_ROWS - 1 - coords.y) - 2;
            sb.draw(markedField, xLocus, yLocus);
        }
    }

    public void handleInput(){
        if(enabled){
            if(Gdx.input.justTouched()){
                int x = Gdx.input.getX();
                int y = Gdx.input.getY();
                markedFieldCoords = getFieldCoords(x, y);
                if (markedFieldCoords != null) observer.notice(markedFieldCoords);
            }
        }

    }

    public void update(float dt){
    }

    public void render(SpriteBatch sb){
        sb.draw(gameBoardTex, xPos, yPos);

        for(int i = 0; i < GAME_BOARD_ROWS; i++){
            for(int j = 0; j < GAME_BOARD_ROWS; j++){
                GameBoardField actualField = gameBoard.get(new Coords(i, j));
                int xLocus = xPos + GAME_BOARD_OFFSET + FIELD_SIZE * (i + 1);
                int yLocus = yPos + GAME_BOARD_OFFSET + FIELD_SIZE * (GAME_BOARD_ROWS - 1 - j);
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
        collideField.dispose();
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
