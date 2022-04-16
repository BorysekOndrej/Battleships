package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.javatuples.Pair;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers.IBattleshipObserver;

public class RemainingShipsPanel implements IRenderable {
    private IBattleshipObserver observer;
    private Boolean collocateShip = false;

    public void place(float x, float y, float width, float height) {
        throw new UnsupportedOperationException("not implemented");
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
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void update(float dt) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void render(SpriteBatch sb) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException("not implemented");
    }
}
