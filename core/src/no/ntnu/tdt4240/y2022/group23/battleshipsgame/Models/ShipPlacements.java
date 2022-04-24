package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

/**
 * Stores the information about where are the ships placed on the game board.
 * This class is separated from the GameBoard class to ease information hiding
 * from the opposing player.
 */
public class ShipPlacements implements Serializable {
    private static final long serialVersionUID = 69;

    private ArrayList<IShip> ships = new ArrayList<>();

    public boolean canAdd(int width, int height, IShip ship) {
        List<Coords> shipsFields = ship.getPositions();
        for (Coords coords: shipsFields) {
            if (!(0 <= coords.x && coords.x < width && 0 <= coords.y && coords.y < height)){
                return false;
            }

            for (int dx = -1; dx <= 1; dx++){
                for (int dy = -1; dy <= 1; dy++) {
                    if (hasShipOnCoords(new Coords(coords.x + dx, coords.y + dy))){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Adds a ship to the collection, if it adheres to rules of ship placement:
     * - the ship is fully within the boundaries of the game board
     * - ship cannot overlap or touch any already placed ship, not even by corners
     * @param width width of the game board
     * @param height height of the game board
     * @param ship the ship to be added
     * @throws IllegalArgumentException thrown when ship to be placed does not follow the rules
     */
    public void addShip(int width, int height, IShip ship) throws IllegalArgumentException {
        if (!canAdd(width, height, ship))
            throw new IllegalArgumentException("Ship does not fit the board or collides with another ship");

        ships.add(ship);
    }

    public boolean hasShipOnCoords(Coords coords) {
        return getShipOnCoords(coords) != null;
    }

    public IShip getShipOnCoords(Coords coords) {
        for (IShip ship: ships) {
            if (ship.getPositions().contains(coords)) {
                return ship;
            }
        }
        return null;
    }

    public boolean allSunk(GameBoard board) {
        for (IShip ship: ships) {
            for (Coords coords: ship.getPositions()){
                if (board.get(coords) != GameBoardField.SUNK){
                    return false;
                }
            }
        }
        return true;
    }

    public List<IShip> getUnsunkShipsDisplaced(GameBoard board) {
        return ships.stream()
                .filter(ship -> !ship.isSunk(board))
                .map(ship -> { IShip newShip = ship.copy(); newShip.displace(); return newShip; })
                .collect(Collectors.toList());
    }
}
