package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

public class GameState {
    private final GameBoard board;
    private final List<Coords> changedCoords;
    private final List<IShip> unsunkShips;
    private final boolean thisPlayerBoard;
    private final NextTurn nextTurn;

    public GameState(
            GameBoard board,
            List<Coords> changedCoords,
            List<IShip> unsunkShips,
            boolean thisPlayerBoard,
            NextTurn nextTurn
    ) {
        this.board = board;
        this.changedCoords = changedCoords;
        this.unsunkShips = unsunkShips;
        this.thisPlayerBoard = thisPlayerBoard;
        this.nextTurn = nextTurn;
    }

    public GameBoard getBoard() {
        return board;
    }

    public List<Coords> getChangedCoords() {
        return changedCoords;
    }

    public List<IShip> getUnsunkShips() {
        return unsunkShips;
    }

    public NextTurn getNextTurn() {
        return nextTurn;
    }

    public boolean isThisPlayerBoard() {
        return thisPlayerBoard;
    }

    /**
     * If isGameOver() is not true, the value returned form this function is undefined.
     * @return true iff this player is the winner of the game
     */
    public boolean thisPlayerWon() {
        return !thisPlayerBoard;
    }
}
