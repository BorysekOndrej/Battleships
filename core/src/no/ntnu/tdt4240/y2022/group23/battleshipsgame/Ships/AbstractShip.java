package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class AbstractShip implements IShip {
    private List<Vector2> positions;


    @Override
    public List<Vector2> getPositions() {
        return positions;
    }
}
