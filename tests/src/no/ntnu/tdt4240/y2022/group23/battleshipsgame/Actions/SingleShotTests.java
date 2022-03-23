package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardChange;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

public class SingleShotTests {
    private GameBoard board;
    private ShipPlacements placements;

    @BeforeEach
    public void setup() {
        board = new GameBoard(2, 2);
        placements = new ShipPlacements();

        placements.addShip(
                board.getWidth(),
                board.getHeight(),
                new RectangularShip(new Coords(0, 0), 2, true)
        );
    }

    @Test
    public void not_hitting_a_ship_reveals_water() {
        IAction action = new SingleShot(new Coords(1, 1));

        List<GameBoardChange> changes = action.affect(placements, board);

        Assertions.assertEquals(1, changes.size());
        GameBoardChange change = changes.get(0);
        Assertions.assertAll(
                () -> Assertions.assertEquals(new Coords(1, 1), change.coords),
                () -> Assertions.assertEquals(GameBoardField.WATER, change.newField)
        );
    }

    @Test
    public void hitting_a_ship_hits() {
        IAction action = new SingleShot(new Coords(1, 0));

        List<GameBoardChange> changes = action.affect(placements, board);

        Assertions.assertEquals(1, changes.size());
        GameBoardChange change = changes.get(0);
        Assertions.assertAll(
                () -> Assertions.assertEquals(new Coords(1, 0), change.coords),
                () -> Assertions.assertEquals(GameBoardField.HIT, change.newField)
        );
    }

    @Test
    public void hitting_a_partially_hit_ship_hits_or_sinks() {
        board.set(new Coords(1, 0), GameBoardField.HIT);

        IAction action = new SingleShot(new Coords(0, 0));

        List<GameBoardChange> changes = action.affect(placements, board);

        Assertions.assertEquals(1, changes.size());
        GameBoardChange change = changes.get(0);
        Assertions.assertAll(
                () -> Assertions.assertEquals(new Coords(0, 0), change.coords),
                () -> Assertions.assertTrue(change.newField == GameBoardField.HIT || change.newField == GameBoardField.SUNK)
        );
    }

    private static Stream<Coords> provideCoords() {
        return Stream.of(
                new Coords(0, 0),
                new Coords(1, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provideCoords")
    public void action_does_not_affect_game_board(Coords coords) {
        board.set(new Coords(1, 0), GameBoardField.HIT);

        GameBoard original = new GameBoard(board);
        IAction action = new SingleShot(coords);

        action.affect(placements, board);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Assertions.assertEquals(original.get(new Coords(x, y)), board.get(new Coords(x, y)));
            }
        }
    }
}
