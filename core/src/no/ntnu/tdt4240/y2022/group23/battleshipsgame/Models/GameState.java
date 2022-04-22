package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import org.javatuples.Pair;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

public class GameState {
    private static final long serialVersionUID = 69420;

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

    private static List<Pair<IShip, Integer>> convert(List<IShip> ships) {
        List<Pair<IShip, Integer>> base = IntStream.of(4, 3, 2, 1)
                .mapToObj(i -> new Pair<>((IShip) new RectangularShip(new Coords(0, 0), i, false), 0))
                .collect(Collectors.toList());
        for (IShip ship : ships) {
            int size = ship.getPositions().size();
            Pair<IShip, Integer> pair = base.get(base.size() - size);
            base.set(base.size() - size, pair.setAt1(pair.getValue1() + 1));
        }
        return base;
    }

    public List<Pair<IShip, Integer>> getUnsunkShips() {
        return convert(unsunkShips);
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
