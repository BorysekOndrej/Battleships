package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.Rectangle;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.CollocateShipObserver;

public class GameBoardPanel implements IRenderable {
    private CollocateShipObserver observer;
    private static final int FIELD_SIZE = 86;
    private static final int GAMEBORAD_OFFSET = 2;
    private static final int GAMEBORAD_ROWS = 10;


    private GameBoard gameBoard;

    private Texture gameBoardTex;

    private Texture unknownField;
    private Texture waterField;
    private Texture hitField;
    private Texture sunkField;
    private Texture shipField;

    private int xCord;
    private int yCord;

    private Rectangle bonds;

    public GameBoardPanel(int x, int y){
        gameBoardTex = new Texture("game_board/gameboard.png");
        unknownField = new Texture("game_board/unknown.png");
        waterField = new Texture("game_board/water.png");
        hitField = new Texture("game_board/hit.png");
        sunkField = new Texture("game_board/sunk.png");
        shipField = new Texture("game_board/ship.png");

        xCord = x;
        yCord = y;

        bonds = new Rectangle(xCord, yCord, gameBoardTex.getWidth(), gameBoardTex.getHeight());

        gameBoard = new GameBoard(GAMEBORAD_ROWS, GAMEBORAD_ROWS);
        gameBoard.set(new Coords(0,0), GameBoardField.SUNK);
        gameBoard.set(new Coords(1,0), GameBoardField.SUNK);
        gameBoard.set(new Coords(2,0), GameBoardField.SUNK);
        gameBoard.set(new Coords(3,0), GameBoardField.WATER);
        gameBoard.set(new Coords(4,0), GameBoardField.WATER);
        gameBoard.set(new Coords(5,0), GameBoardField.HIT);
        gameBoard.set(new Coords(0,1), GameBoardField.WATER);
        gameBoard.set(new Coords(4,4), GameBoardField.WATER);
    }

    //Args: int x: Gdx.input.getX();
    //      int y: Gdx.input.getY();
    public Coords getFieldCoords(int x, int y){
        int xRelative = x - xCord;
        int yRelative = y - (1920 - yCord - gameBoardTex.getHeight());
        Coords result = new Coords((int) ((xRelative - GAMEBORAD_OFFSET) / FIELD_SIZE - 1), (int) -((yRelative - GAMEBORAD_OFFSET) / FIELD_SIZE - 1));
        if(coordsValid(result)) return result;
        else  {
            System.out.println("Coords out of bonds");
            return null;
        }
    }

    public boolean coordsValid(Coords coords){
        return (coords.x >= 0 && coords.x < GAMEBORAD_ROWS) && (coords.y >= 0 && coords.y < GAMEBORAD_ROWS);
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
    public void addCollocateObserver(CollocateShipObserver observer){
        this.observer = observer;
    }

    public void handleInput(){
        if(Gdx.input.justTouched() /*&& bonds.contains(Gdx.input.getX() - xCord, Gdx.input.getY() - yCord)*/){
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            Coords res = getFieldCoords(x, y);
            if (res != null) observer.notice(res); //System.out.println(res.toString());
        }
    }

    public void update(float dt){
        handleInput();
    }

    public void render(SpriteBatch sb){
        sb.begin();
        sb.draw(gameBoardTex, xCord, yCord);

        for(int i = 0; i < GAMEBORAD_ROWS; i++){
            for(int j = 0; j < GAMEBORAD_ROWS; j++){
                GameBoardField actualField = gameBoard.get(new Coords(i, j));
                int xLocus = xCord + GAMEBORAD_OFFSET + FIELD_SIZE * (i + 1);
                int yLocus = yCord + GAMEBORAD_OFFSET + FIELD_SIZE * (GAMEBORAD_ROWS - 1 - j);
                drawField(actualField, sb, xLocus, yLocus);
            }
        }
        sb.end();
    }

    public void dispose(){
        gameBoardTex.dispose();
        unknownField.dispose();
        waterField.dispose();
        hitField.dispose();
        sunkField.dispose();
        shipField.dispose();
    }
}
