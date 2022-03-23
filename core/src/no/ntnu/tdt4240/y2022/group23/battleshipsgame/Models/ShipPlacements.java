package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.ISerializable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

/**
 * Stores the information about where are the ships placed on the game board.
 * This class is separated from the GameBoard class to ease information hiding
 * from the opposing player.
 */
public class ShipPlacements implements ISerializable {
    private List<IShip> ships;

    /**
     * Adds a ship to the collection, if it adheres to rules of ship placement:
     * - the ship is fully within the boundaries of the game board
     * - ship cannot overlap any already placed ship
     * @param width width of the game board
     * @param height height of the game board
     * @param ship the ship to be added
     * @throws IllegalArgumentException thrown when ship to be placed does not follow the rules
     */
    public void addShip(int width, int height, IShip ship) throws IllegalArgumentException {
        throw new UnsupportedOperationException("not implemented");
    }

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
