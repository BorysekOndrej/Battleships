package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.ISerialializable;

import java.util.List;

/**
 * Stores the information about the state of the game available to the player at given moment.
 */
public class GameBoard implements ISerialializable {
    private List<List<GameBoardField>> board;

    public GameBoard(int width, int height) { throw new UnsupportedOperationException("not implemented"); }
    public GameBoard(GameBoard other) { throw new UnsupportedOperationException("not implemented"); }

    public GameBoardField get(Coords coords) { return board.get(coords.x).get(coords.y); }
    public void set(Coords coords, GameBoardField field) { board.get(coords.x).set(coords.y, field); }
}
