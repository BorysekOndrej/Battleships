package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

public class RemainingShipsPanel implements IRenderable {
    private final static int BONDS_WIDTH = 150;
    private final static int BONDS_HEIGHT = 175;
    private final static int BONDS_OFFSET = 37;

    private Boolean collocateShip = false;

    private final Texture panelTex;
    private final Texture ship1markedTex;
    private final Texture ship2markedTex;
    private final Texture ship3markedTex;
    private final Texture ship4markedTex;

    private final Rectangle ship1bonds;
    private final Rectangle ship2bonds;
    private final Rectangle ship3bonds;
    private final Rectangle ship4bonds;

    private BitmapFont font;

    private Integer markedShips = null;
    private List<Pair<IShip, Integer>> remainingShips;

    private int xPos;
    private int yPos;

    public RemainingShipsPanel(int x, int y){
        xPos = x;
        yPos = y;

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(3);

        //debugging default list
        remainingShips = new ArrayList<>();
        remainingShips.add(new Pair<>(new RectangularShip(new Coords(1, 1),8, false), 8));
        remainingShips.add(new Pair<>(new RectangularShip(new Coords(2, 1),3, false), 2));
        remainingShips.add(new Pair<>(new RectangularShip(new Coords(3, 1),2, false), 3));
        remainingShips.add(new Pair<>(new RectangularShip(new Coords(4, 1),1, false), 4));

        panelTex = new Texture("ships_panel/ships_panel.png");
        ship1markedTex = new Texture("ships_panel/panel_1_marked.png");
        ship2markedTex = new Texture("ships_panel/panel_2_marked.png");
        ship3markedTex = new Texture("ships_panel/panel_3_marked.png");
        ship4markedTex = new Texture("ships_panel/panel_4_marked.png");

        ship4bonds = new Rectangle(xPos + BONDS_OFFSET, yPos, BONDS_WIDTH, BONDS_HEIGHT + BONDS_OFFSET * 2);
        ship3bonds = new Rectangle(xPos + BONDS_OFFSET + BONDS_WIDTH, yPos, BONDS_WIDTH, BONDS_HEIGHT + BONDS_OFFSET * 2);
        ship2bonds = new Rectangle(xPos + BONDS_OFFSET + BONDS_WIDTH * 2, yPos, BONDS_WIDTH, BONDS_HEIGHT + BONDS_OFFSET * 2);
        ship1bonds = new Rectangle(xPos + BONDS_OFFSET + BONDS_WIDTH * 3, yPos, BONDS_WIDTH, BONDS_HEIGHT + BONDS_OFFSET * 2);
    }

    private boolean ship1bondsPressed(){
        return ship1bonds.contains(Gdx.input.getX(), BattleshipsGame.HEIGHT - Gdx.input.getY());
    }

    private boolean ship2bondsPressed(){
        return ship2bonds.contains(Gdx.input.getX(), BattleshipsGame.HEIGHT - Gdx.input.getY());
    }

    private boolean ship3bondsPressed(){
        return ship3bonds.contains(Gdx.input.getX(), BattleshipsGame.HEIGHT - Gdx.input.getY());
    }

    private boolean ship4bondsPressed(){
        return ship4bonds.contains(Gdx.input.getX(), BattleshipsGame.HEIGHT - Gdx.input.getY());
    }

    private void resetMarkedShips(){
        markedShips = null;
    }

    private void markShip(int nr){
        markedShips = nr;
    }

    public void setData(List<Pair<IShip, Integer>> remainingShips) {
        this.remainingShips = remainingShips;
    }

    public IShip selectedShipType(){
        if(markedShips == null) return null;
        return this.remainingShips.get(markedShips).getValue0();
    }

    @Override
    public void handleInput() {
        if (Gdx.input.justTouched()){
            if(ship1bondsPressed()) {
                markShip(0);
            }
            if(ship2bondsPressed()) {
                markShip(1);
            }
            if(ship3bondsPressed()) {
                markShip(2);
            }
            if(ship4bondsPressed()) {
                markShip(3);
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(panelTex, xPos, yPos);
        font.draw(sb,String.format("x%d", remainingShips.get(0).getValue1()), xPos + BONDS_OFFSET+60, yPos + 147);
        font.draw(sb,String.format("x%d",remainingShips.get(1).getValue1()), xPos + BONDS_OFFSET+60 + BONDS_WIDTH, yPos + 147);
        font.draw(sb,String.format("x%d",remainingShips.get(2).getValue1()), xPos + BONDS_OFFSET+60 + BONDS_WIDTH * 2, yPos + 147);
        font.draw(sb,String.format("x%d",remainingShips.get(3).getValue1()), xPos + BONDS_OFFSET+60 + BONDS_WIDTH * 3, yPos + 147);

        if(markedShips != null) {
            switch (markedShips) {
                case 3:
                    sb.draw(ship4markedTex, xPos + BONDS_OFFSET - 5, yPos + 31);
                    break;
                case 2:
                    sb.draw(ship3markedTex, xPos + BONDS_OFFSET + BONDS_WIDTH - 5, yPos + 52);
                    break;
                case 1:
                    sb.draw(ship2markedTex, xPos + BONDS_OFFSET + BONDS_WIDTH * 2 - 5, yPos + 74);
                    break;
                case 0:
                    sb.draw(ship1markedTex, xPos + BONDS_OFFSET + BONDS_WIDTH * 3 - 5, yPos + 96);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void dispose() {
        panelTex.dispose();
        ship1markedTex.dispose();
        ship2markedTex.dispose();
        ship3markedTex.dispose();
        ship4markedTex.dispose();
        font.dispose();
    }
}
