package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.ISerialializable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

/**
 * Stores the information about where are the ships placed on the game board.
 * This class is separated from the GameBoard class to ease information hiding
 * from the opposing player.
 */
public class ShipPlacements implements ISerialializable {
    private List<IShip> ships;

    public boolean hasShipOnCoords(Coords coords) {
        return getShipOnCoords(coords) != null;
    }

    public IShip getShipOnCoords(Coords coords) {
        throw new UnsupportedOperationException("not implemented");
    }

    public boolean allSunk(GameBoard board) {
        throw new UnsupportedOperationException("not implemented");
    }
}
