package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import org.javatuples.Pair;

import java.util.List;
import java.util.Map;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.IBattleshipObserver;

public class RemainingShipsPanel implements IRenderable {
    private final static int BONDS_WIDTH = 150;
    private final static int BONDS_HEIGHT = 175;
    private final static int BONDS_OFFSET = 37;


    private IBattleshipObserver observer;
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

    private boolean markedShips[] = {false, false, false, false};

    private int xCord;
    private int yCord;

    public RemainingShipsPanel(int x, int y){
        xCord = x;
        yCord = y;

        panelTex = new Texture("ships_panel/ships_panel.png");
        ship1markedTex = new Texture("ships_panel/panel_1_marked.png");
        ship2markedTex = new Texture("ships_panel/panel_2_marked.png");
        ship3markedTex = new Texture("ships_panel/panel_3_marked.png");
        ship4markedTex = new Texture("ships_panel/panel_4_marked.png");

        ship4bonds = new Rectangle(xCord + BONDS_OFFSET, yCord, BONDS_WIDTH, BONDS_HEIGHT + BONDS_OFFSET * 2);
        ship3bonds = new Rectangle(xCord + BONDS_OFFSET + BONDS_WIDTH, yCord, BONDS_WIDTH, BONDS_HEIGHT + BONDS_OFFSET * 2);
        ship2bonds = new Rectangle(xCord + BONDS_OFFSET + BONDS_WIDTH * 2, yCord, BONDS_WIDTH, BONDS_HEIGHT + BONDS_OFFSET * 2);
        ship1bonds = new Rectangle(xCord + BONDS_OFFSET + BONDS_WIDTH * 3, yCord, BONDS_WIDTH, BONDS_HEIGHT + BONDS_OFFSET * 2);

    }

    public void setPosition(int x, int y) {
        xCord = x;
        yCord = y;
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
        markedShips[0] = false;
        markedShips[1] = false;
        markedShips[2] = false;
        markedShips[3] = false;
    }

    private void markShip(int type){
        resetMarkedShips();
        markedShips[type-1] = true;
    }

    public void setData(List<Pair<IShip, Integer>> remainingShips) {
        throw new UnsupportedOperationException("not implemented");
    }

    public IShip selectedShipType(){
        throw new UnsupportedOperationException("not implemented");
    }

    //Adds observer to the observable object
    public void addObserver(IBattleshipObserver observer){
        this.observer = observer;
    }

    @Override
    public void handleInput() {
        if (collocateShip){ //Place holder, this call should be done if the ship is collocated
            observer.notice();
        }
        if (Gdx.input.justTouched()){
            if(ship1bondsPressed()) {
                markShip(1);
                System.out.print("1 pressed\n");
            }
            if(ship2bondsPressed()) {
                markShip(2);
                System.out.print("2 pressed\n");
            }
            if(ship3bondsPressed()) {
                markShip(3);
                System.out.print("3 pressed\n");
            }
            if(ship4bondsPressed()) {
                markShip(4);
                System.out.print("4 pressed\n");
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        //sb.begin();
        sb.draw(panelTex, xCord, yCord);
        if (markedShips[3]) sb.draw(ship4markedTex, xCord + BONDS_OFFSET - 5, yCord + 31);
        if (markedShips[2]) sb.draw(ship3markedTex, xCord + BONDS_OFFSET + BONDS_WIDTH - 5, yCord + 52);
        if (markedShips[1]) sb.draw(ship2markedTex, xCord + BONDS_OFFSET + BONDS_WIDTH * 2 - 5, yCord + 74);
        if (markedShips[0]) sb.draw(ship1markedTex, xCord + BONDS_OFFSET + BONDS_WIDTH * 3 - 5, yCord + 94);

        //sb.end();
    }

    @Override
    public void dispose() {
        panelTex.dispose();
        ship1markedTex.dispose();
        ship2markedTex.dispose();
        ship3markedTex.dispose();
        ship4markedTex.dispose();
    }
}
