package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardChange;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

public class RadarTests {
    private GameBoard board;
    private ShipPlacements placements;

    private static <T> void assertCollectionsEqualInAnyOrder(Collection<T> lt, Collection<T> rt) {
        Assertions.assertEquals(lt.size(), rt.size());
        Assertions.assertTrue(lt.containsAll(rt));
        Assertions.assertTrue(rt.containsAll(lt));
    }

    private static void assertRevealed(List<GameBoardChange> changes, List<Coords> ships) {
        Assertions.assertAll(changes.stream()
                .map(change -> (Executable) () ->
                        Assertions.assertEquals(
                                ships.contains(change.coords) ? GameBoardField.SHIP : GameBoardField.WATER,
                                change.newField,
                                "at " + change.coords.toString()
                        )
                )
                .collect(Collectors.toList())
        );
    }

    @BeforeEach
    public void setup() {
        board = new GameBoard(3, 3);
        placements = new ShipPlacements();
    }

    @Test
    public void hitting_nothing_reveals_water() {
        IAction action = new Radar(new Coords(1, 1));

        List<GameBoardChange> changes = action.affect(placements, board);

        Assertions.assertEquals(5, changes.size());
        assertCollectionsEqualInAnyOrder(
            Arrays.asList(
                new Coords(1, 0), new Coords(0, 1), new Coords(1, 1), new Coords(2, 1),
                new Coords(1, 2)
            ),
            changes.stream().map(change -> change.coords).collect(Collectors.toList())
        );
        assertRevealed(changes, Collections.emptyList());
    }

    @Test
    public void hitting_at_border_reveals_on_board_only() {
        IAction action = new Radar(new Coords(1, 2));

        List<GameBoardChange> changes = action.affect(placements, board);

        Assertions.assertEquals(4, changes.size());
        assertCollectionsEqualInAnyOrder(
                Arrays.asList(
                        new Coords(1, 1), new Coords(0, 2), new Coords(1, 2), new Coords(2, 2)
                ),
                changes.stream().map(change -> change.coords).collect(Collectors.toList())
        );
        assertRevealed(changes, Collections.emptyList());
    }

    @Test
    public void hitting_at_corner_reveals_on_board_only() {
        IAction action = new Radar(new Coords(0, 2));

        List<GameBoardChange> changes = action.affect(placements, board);

        Assertions.assertEquals(3, changes.size());
        assertCollectionsEqualInAnyOrder(
                Arrays.asList(
                        new Coords(0, 1), new Coords(0, 2), new Coords(1, 2)
                ),
                changes.stream().map(change-> change.coords).collect(Collectors.toList())
        );
        assertRevealed(changes, Collections.emptyList());
    }

    @Test
    public void hitting_a_ship_reveals_it() {
        IShip ship = new RectangularShip(new Coords(0, 0), 2, true);
        placements.addShip(board.getWidth(), board.getHeight(), ship);

        IAction action = new Radar(new Coords(1, 0));

        List<GameBoardChange> changes = action.affect(placements, board);

        assertRevealed(changes, ship.getPositions());
    }

    @Test
    public void multiple_different_ship_can_be_detected() {
        IShip ship1 = new RectangularShip(new Coords(0, 0), 2, true);
        IShip ship2 = new RectangularShip(new Coords(1, 3), 1, false);
        placements.addShip(board.getWidth(), board.getHeight(), ship1);
        placements.addShip(board.getWidth(), board.getHeight(), ship2);

        IAction action = new Radar(new Coords(1, 1));

        List<GameBoardChange> changes = action.affect(placements, board);

        assertRevealed(
                changes,
                Stream.concat(ship1.getPositions().stream(), ship2.getPositions().stream()).collect(Collectors.toList())
        );
    }

    @ParameterizedTest
    @EnumSource(GameBoardField.class)
    public void hitting_a_partially_revealed_ship_still_reveals_it(GameBoardField field) {
        IShip ship = new RectangularShip(new Coords(0, 0), 2, true);
        placements.addShip(board.getWidth(), board.getHeight(), ship);

        board.set(new Coords(0, 0), field);
        if (field == GameBoardField.SUNK)
            board.set(new Coords(1, 0), field);

        IAction action = new Radar(new Coords(1, 0));

        List<GameBoardChange> changes = action.affect(placements, board);

        assertRevealed(changes, ship.getPositions());
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
        IAction action = new Radar(coords);

        action.affect(placements, board);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Assertions.assertEquals(original.get(new Coords(x, y)), board.get(new Coords(x, y)));
            }
        }
    }
}
