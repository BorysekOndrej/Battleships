package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;

import java.io.Serializable;
import java.util.List;

public interface IShip extends Serializable {
    List<Coords> getPositions();
    boolean isSunk(GameBoard board);
}
