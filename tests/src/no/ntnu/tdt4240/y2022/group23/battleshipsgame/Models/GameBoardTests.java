package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

public class GameBoardTests {

    private static Stream<int[]> provideDimensions() {
        int[][] arrayDims = {{1, 1}, {2, 3}, {67, 13}, {100, 300}};
        return Arrays.stream(arrayDims);
    }

    private static Stream<Coords> provideCoords() {
        int[][] arrayCoords = {{0, 0}, {1, 2}, {0, 12}, {66, 0}, {55, 9}};
        return Arrays.stream(arrayCoords).map(arrayCoord -> new Coords(arrayCoord[0], arrayCoord[1]));
    }

    private static Stream<GameBoardField> provideGameBoardFields() {
        return Arrays.stream(GameBoardField.values());
    }

    private static Stream<GameBoardChange> provideGameBoardChanges() {
        return provideCoords().flatMap(coords -> provideGameBoardFields().map(field -> new GameBoardChange(coords, field)));
    }

    private static Stream<Arguments> provideDimsAndChange() {
        return provideDimensions().flatMap(dims -> provideGameBoardChanges()
                                                        .filter(change -> isInbounds(dims, change.coords))
                                                        .map(change -> Arguments.of(dims, change)));
    }

    private static boolean isInbounds(int[] dimensions, Coords coords) {
        return 0 <= coords.x && coords.x < dimensions[0] && 0 <= coords.y && coords.y < dimensions[1];
    }

    @ParameterizedTest
    @MethodSource("provideDimensions")
    public void new_board_contains_unknown_only(int[] dims) {
        GameBoard board = new GameBoard(dims[0], dims[1]);

        for (int i = 0; i < dims[0]; i++) {
            for (int j = 0; j < dims[1]; j++) {
                Assertions.assertEquals(GameBoardField.UNKNOWN, board.get(new Coords(i, j)));
            }
        }
    }

    @Test
    public void copied_board_is_same_as_original() {
        GameBoardField[] fieldValues = GameBoardField.values();
        GameBoard board = new GameBoard(fieldValues.length, fieldValues.length);

        for (int i = 0; i < fieldValues.length; i++) {
            board.set(new Coords(i, i), fieldValues[i]);
        }

        GameBoard copy = new GameBoard(board);

        for (int i = 0; i < fieldValues.length; i++) {
            for (int j = 0; j < fieldValues.length; j++) {
                Assertions.assertEquals(board.get(new Coords(i, j)), copy.get(new Coords(i, j)));
            }
        }
    }

    @Test
    public void change_in_copy_does_not_affect_original() {
        GameBoard original = new GameBoard(5, 5);
        GameBoard copy = new GameBoard(original);
        Assertions.assertNotEquals(GameBoardField.HIT, original.get(new Coords(0, 0)));

        copy.set(new Coords(0, 0), GameBoardField.HIT);
        Assertions.assertNotEquals(GameBoardField.HIT, original.get(new Coords(0, 0)));
    }

    @ParameterizedTest
    @MethodSource({"provideDimsAndChange"})
    public void change_changes_its_coords(int[] dimensions, GameBoardChange change) {
        GameBoard board = new GameBoard(dimensions[0], dimensions[1]);
        board.apply(Collections.singletonList(change));
        Assertions.assertEquals(change.newField, board.get(change.coords));
    }

    @ParameterizedTest
    @MethodSource({"provideDimsAndChange"})
    public void change_affect_no_coords_except_its_own(int[] dimensions, GameBoardChange change) {
        GameBoard board = new GameBoard(dimensions[0], dimensions[1]);
        GameBoard original = new GameBoard(board);
        board.apply(Collections.singletonList(change));

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                if (i == change.coords.x && j == change.coords.y)
                    continue;
                Assertions.assertEquals(original.get(new Coords(i, j)), board.get(new Coords(i, j)));
            }
        }
    }

    private static Stream<Arguments> provideDoubleChangeWithResult() {
        return Stream.of( // disallow WATER <-> SHIP/HIT/SUNK
                Arguments.of(GameBoardField.UNKNOWN, GameBoardField.UNKNOWN, GameBoardField.UNKNOWN),
                Arguments.of(GameBoardField.UNKNOWN, GameBoardField.WATER, GameBoardField.WATER),
                Arguments.of(GameBoardField.UNKNOWN, GameBoardField.HIT, GameBoardField.HIT),
                Arguments.of(GameBoardField.UNKNOWN, GameBoardField.SUNK, GameBoardField.SUNK),
                Arguments.of(GameBoardField.UNKNOWN, GameBoardField.SHIP, GameBoardField.SHIP),
                Arguments.of(GameBoardField.WATER, GameBoardField.UNKNOWN, GameBoardField.WATER),
                Arguments.of(GameBoardField.WATER, GameBoardField.WATER, GameBoardField.WATER),
                Arguments.of(GameBoardField.HIT, GameBoardField.UNKNOWN, GameBoardField.HIT),
                Arguments.of(GameBoardField.HIT, GameBoardField.HIT, GameBoardField.HIT),
                Arguments.of(GameBoardField.HIT, GameBoardField.SUNK, GameBoardField.SUNK),
                Arguments.of(GameBoardField.HIT, GameBoardField.SHIP, GameBoardField.HIT),
                Arguments.of(GameBoardField.SUNK, GameBoardField.UNKNOWN, GameBoardField.SUNK),
                Arguments.of(GameBoardField.SUNK, GameBoardField.HIT, GameBoardField.SUNK),
                Arguments.of(GameBoardField.SUNK, GameBoardField.SUNK, GameBoardField.SUNK),
                Arguments.of(GameBoardField.SUNK, GameBoardField.SHIP, GameBoardField.SHIP),
                Arguments.of(GameBoardField.SHIP, GameBoardField.UNKNOWN, GameBoardField.SHIP),
                Arguments.of(GameBoardField.SHIP, GameBoardField.HIT, GameBoardField.HIT),
                Arguments.of(GameBoardField.SHIP, GameBoardField.SUNK, GameBoardField.SUNK),
                Arguments.of(GameBoardField.SHIP, GameBoardField.SHIP, GameBoardField.SHIP)
        );
    }

    @ParameterizedTest
    @MethodSource("provideDoubleChangeWithResult")
    public void changing_twice_the_same_field_in_the_same_apply(GameBoardField fst, GameBoardField snd, GameBoardField result) {
        GameBoard board = new GameBoard(1, 1);

        board.apply(Arrays.asList(
                new GameBoardChange(new Coords(0, 0), fst),
                new GameBoardChange(new Coords(0, 0), snd)
        ));

        Assertions.assertEquals(result, board.get(new Coords(0, 0)));
    }

    @ParameterizedTest
    @MethodSource("provideDoubleChangeWithResult")
    public void changing_twice_the_same_field_in_different_applies(GameBoardField fst, GameBoardField snd, GameBoardField result) {
        GameBoard board = new GameBoard(1, 1);

        board.apply(Collections.singletonList(new GameBoardChange(new Coords(0, 0), fst)));
        board.apply(Collections.singletonList(new GameBoardChange(new Coords(0, 0), snd)));

        Assertions.assertEquals(result, board.get(new Coords(0, 0)));
    }


    private static Stream<ShipPlacements> provideShipPlacements() {
        ShipPlacements placements1 = new ShipPlacements();

        placements1.addShip(5, 5, new RectangularShip(new Coords(0, 0), 2, false));
        placements1.addShip(5, 5, new RectangularShip(new Coords(2, 0), 3, false));
        placements1.addShip(5, 5, new RectangularShip(new Coords(0, 4), 3, true));

        return Stream.of(placements1);
    }

    private static Stream<Arguments> provideShipPlacementsWithFirstShip() {
        return provideShipPlacements()
                .map(placements -> Arguments.of(
                        placements,
                        new RectangularShip(new Coords(0, 0), 2, false)
                ));
    }

    private static Stream<Arguments> provideShipPlacementsWithExtraShip() {
        return provideShipPlacements()
                .flatMap(placements -> Stream.of(
                        Arguments.of(
                            placements,
                            new RectangularShip(new Coords(4, 0), 4, false)
                        ),
                        Arguments.of(
                            placements,
                            new RectangularShip(new Coords(4, 0), 4, true)
                        ),
                        Arguments.of(
                                placements,
                                new RectangularShip(new Coords(1, 0), 4, true)
                        ),
                        Arguments.of(
                                placements,
                                new RectangularShip(new Coords(3, 0), 2, true)
                        )
                ));
    }

    @ParameterizedTest
    @MethodSource("provideShipPlacements")
    public void ships_are_revealed_from_unknown_board(ShipPlacements placements) {
        GameBoard board = new GameBoard(5, 5);

        board.reveal(placements);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Coords coords = new Coords(x, y);
                if (placements.hasShipOnCoords(coords)) {
                    Assertions.assertEquals(GameBoardField.SHIP, board.get(coords));
                } else {
                    Assertions.assertEquals(GameBoardField.WATER, board.get(coords));
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideShipPlacementsWithFirstShip")
    public void ships_are_revealed_from_partially_revealed_board(ShipPlacements placements, IShip firstShip) {
        GameBoard board = new GameBoard(5, 5);

        for (Coords pos : firstShip.getPositions()) {
            board.set(pos, GameBoardField.SHIP);
        }

        board.reveal(placements);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Coords coords = new Coords(x, y);
                if (placements.hasShipOnCoords(coords)) {
                    Assertions.assertEquals(GameBoardField.SHIP, board.get(coords));
                } else {
                    Assertions.assertEquals(GameBoardField.WATER, board.get(coords));
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideShipPlacementsWithFirstShip")
    public void ships_are_revealed_but_not_unsunk(ShipPlacements placements, IShip firstShip) {
        GameBoard board = new GameBoard(5, 5);

        for (Coords pos : firstShip.getPositions()) {
            board.set(pos, GameBoardField.SUNK);
        }

        board.reveal(placements);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Coords coords = new Coords(x, y);
                if (firstShip.getPositions().contains(coords)) {
                    Assertions.assertEquals(GameBoardField.SUNK, board.get(coords));
                } else if (placements.hasShipOnCoords(coords)) {
                    Assertions.assertEquals(GameBoardField.SHIP, board.get(coords));
                } else {
                    Assertions.assertEquals(GameBoardField.WATER, board.get(coords));
                }
            }
        }
    }

    @ParameterizedTest
    @MethodSource("provideShipPlacementsWithExtraShip")
    public void ships_with_extra_ship_fitting_are_revealed(ShipPlacements placements, IShip extraShip) {
        GameBoard board = new GameBoard(5, 5, placements, extraShip);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Coords coords = new Coords(x, y);
                if (extraShip.getPositions().contains(coords)) {
                    if (placements.canAdd(5, 5, extraShip)) {
                        Assertions.assertEquals(GameBoardField.SHIP, board.get(coords));
                    } else {
                        Assertions.assertEquals(GameBoardField.COLLIDE, board.get(coords));
                    }
                } else if (placements.hasShipOnCoords(coords)) {
                    Assertions.assertEquals(GameBoardField.SHIP, board.get(coords));
                } else {
                    Assertions.assertEquals(GameBoardField.UNKNOWN, board.get(coords));
                }
            }
        }
    }
}
