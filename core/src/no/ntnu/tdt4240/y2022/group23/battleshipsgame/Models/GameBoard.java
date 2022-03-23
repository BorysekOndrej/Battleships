package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores the information about the state of the game available to the player at given moment.
 */
public class GameBoard implements Serializable {
    private List<List<GameBoardField>> board;

    public GameBoard(int width, int height) {
        board = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            ArrayList<GameBoardField> newCol = new ArrayList<>(Collections.nCopies(height, GameBoardField.UNKNOWN));
            board.add(newCol);
        }
    }
    public GameBoard(GameBoard other) {
        // we can't easily clone GameBoard, because we need deepclone
        // we can't easily clone board, because List doesn't have clone, only ArrayList does have it

        board = new ArrayList<>();
        for (int x = 0; x < other.board.size(); x++) {
            ArrayList<GameBoardField> newCol = new ArrayList<>();
            for (int y = 0; y < other.board.get(0).size(); y++) {
                newCol.add(other.get(new Coords(x, y)));
            }
            board.add(newCol);
        }
    }

    public GameBoardField get(Coords coords) { return board.get(coords.x).get(coords.y); }
    public void set(Coords coords, GameBoardField field) { board.get(coords.x).set(coords.y, field); }

    public void apply(List<GameBoardChange> changes) {
        for (GameBoardChange change : changes) {
            set(change.coords, change.newField);
        }
    }
}
