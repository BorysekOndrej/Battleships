package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;

import java.io.Serializable;
import java.util.List;

public interface IShip extends Serializable {
    List<Coords> getPositions();
    void setPositions(List<Coords> positions);
    boolean isSunk(GameBoard board);


    /**
     * Shifts the ship's position so that it does not betray its position nor orientation:
     * - the first coordinate of {@link #getPositions()} after a call to this method is (0, 0)
     *   and the remaining are relative to it,
     * - the ship is oriented horizontally.
     */
    void displace();

    void rotateClockwise();

    IShip copy();
}
