package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;

public abstract class AbstractShip implements IShip {
    private List<Coords> positions;

    @Override
    public List<Coords> getPositions() {
        return positions;
    }

    @Override
    public boolean isSunk(GameBoard board) {
        throw new UnsupportedOperationException("not implemented");
    }
}
