package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import com.sun.org.apache.xpath.internal.Arg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;
import sun.jvm.hotspot.utilities.Assert;

public class ShipPlacementsTests {
    private static Stream<Arguments> provideShipsWithInboundsAnswers() {
        return Stream.of( // assuming board 5x5
                Arguments.of(new RectangularShip(new Coords(0, 0), 2, true), true),
                Arguments.of(new RectangularShip(new Coords(0, 0), 5, true), true),
                Arguments.of(new RectangularShip(new Coords(0, 0), 6, true), false),
                Arguments.of(new RectangularShip(new Coords(4, 0), 2, true), false),
                Arguments.of(new RectangularShip(new Coords(0, 0), 5, false), true),
                Arguments.of(new RectangularShip(new Coords(0, 4), 2, false), false)
        );
    }

    private static Stream<Arguments> provideShipsWithOverlapAnswers() {
        return Stream.of( // assuming board 5x5, overlapping RectangularShip(new Coords(2, 2), 3, false)
                Arguments.of(new RectangularShip(new Coords(0, 0), 5, false), true, "ok placement"),
                Arguments.of(new RectangularShip(new Coords(1, 0), 4, true), true, "ok placement"),
                Arguments.of(new RectangularShip(new Coords(0, 2), 4, true), false, "ships overlap"),
                Arguments.of(new RectangularShip(new Coords(1, 4), 2, true), false, "ships overlap"),
                Arguments.of(new RectangularShip(new Coords(2, 0), 5, false), false, "ships overlap"),
                Arguments.of(new RectangularShip(new Coords(0, 1), 5, true), false, "ships share edge"),
                Arguments.of(new RectangularShip(new Coords(1, 2), 1, false), false, "ships share edge"),
                Arguments.of(new RectangularShip(new Coords(0, 1), 2, true), false, "ships share corner"),
                Arguments.of(new RectangularShip(new Coords(3, 1), 1, false), false, "ships share corner")
        );
    }

    @ParameterizedTest
    @MethodSource("provideShipsWithInboundsAnswers")
    public void single_ship_can_be_placed_iff_in_bounds(IShip ship, boolean canBePlaced) {
        ShipPlacements placements = new ShipPlacements();
        if (canBePlaced) {
            Assertions.assertDoesNotThrow(() -> placements.addShip(5, 5, ship));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> placements.addShip(5, 5, ship));
        }
    }

    @ParameterizedTest
    @MethodSource("provideShipsWithOverlapAnswers")
    public void second_ship_can_be_placed_iff_not_overlapping(IShip ship, boolean canBePlaced) {
        ShipPlacements placements = new ShipPlacements();
        placements.addShip(5, 5, new RectangularShip(new Coords(2, 2), 3, false));

        if (canBePlaced) {
            Assertions.assertDoesNotThrow(() -> placements.addShip(5, 5, ship));
        } else {
            Assertions.assertThrows(IllegalArgumentException.class, () -> placements.addShip(5, 5, ship));
        }
    }

    private static List<List<IShip>> provideShips() {
        return Arrays.asList(
                Collections.singletonList(
                        new RectangularShip(new Coords(0, 0), 5, true)
                ),
                Arrays.asList(
                        new RectangularShip(new Coords(0, 0), 5, true),
                        new RectangularShip(new Coords(0, 2), 5, true),
                        new RectangularShip(new Coords(0, 4), 5, true)
                ),
                Arrays.asList(
                        new RectangularShip(new Coords(0, 0), 2, false),
                        new RectangularShip(new Coords(2, 0), 3, false),
                        new RectangularShip(new Coords(4, 0), 4, false),
                        new RectangularShip(new Coords(0, 4), 3, true)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideShips")
    public void ships_can_be_retrieved_from_where_they_have_been_placed(List<IShip> ships) {
        ShipPlacements placements = new ShipPlacements();

        for (IShip ship : ships) {
            placements.addShip(5, 5, ship);
        }

        for (IShip ship : ships) {
            for (Coords coords : ship.getPositions()) {
                Assertions.assertTrue(placements.hasShipOnCoords(coords));
                Assertions.assertEquals(ship, placements.getShipOnCoords(coords));
            }
        }
    }

    private static Stream<Arguments> provideShipsWithEmptyCoords() {
        List<List<IShip>> ships = provideShips();
        List<List<Coords>> emptyCoords = Arrays.asList(
            IntStream.range(0, 5)
                    .mapToObj(x -> IntStream.range(1, 5)
                            .mapToObj(y -> new Coords(x, y)))
                    .flatMap(Function.identity())
                    .collect(Collectors.toList()),
            Stream.of(1, 3)
                .flatMap(y -> IntStream.range(0, 5)
                            .mapToObj(x -> new Coords(x, y)))
                .collect(Collectors.toList()),
            Arrays.asList(
                    new Coords(1, 0), new Coords(3, 0), new Coords(1, 1),
                    new Coords(3, 1), new Coords(0, 2), new Coords(1, 2),
                    new Coords(3, 2), new Coords(0, 3), new Coords(1, 3),
                    new Coords(2, 3), new Coords(3, 3), new Coords(3, 4),
                    new Coords(4, 4)
            )
        );
        return IntStream.range(0, ships.size())
                .mapToObj(i -> Arguments.of(ships.get(i), emptyCoords.get(i)));
    }

    @ParameterizedTest
    @MethodSource("provideShipsWithEmptyCoords")
    public void no_ship_can_be_retrieved_where_none_has_been_placed(List<IShip> ships, List<Coords> emptyCoords) {
        ShipPlacements placements = new ShipPlacements();

        for (IShip ship : ships) {
            placements.addShip(5, 5, ship);
        }

        for (Coords empty : emptyCoords) {
            Assertions.assertFalse(placements.hasShipOnCoords(empty));
            Assertions.assertNull(placements.getShipOnCoords(empty));
        }
    }

    private static Stream<Arguments> provideShipsWithGameBoards() {
        List<IShip> ships = Arrays.asList(
                new RectangularShip(new Coords(0, 0), 5, true),
                new RectangularShip(new Coords(0, 2), 5, true),
                new RectangularShip(new Coords(0, 4), 5, true)
        );
        GameBoard board = new GameBoard(5, 5);
        IntStream.range(0, 5)
                .forEach(x -> Stream.of(0, 2)
                        .forEach(y -> board.set(new Coords(x, y), GameBoardField.SUNK)));

        return Stream.concat(Stream.of(Arguments.of(ships, new GameBoard(5, 5), false)),
                Arrays.stream(GameBoardField.values())
                        .map(field -> {
                            GameBoard copy = new GameBoard(board);
                            for (int x = 0; x < 5; x++)
                                copy.set(new Coords(x, 4), field);
                            return Arguments.of(ships, copy, field == GameBoardField.SUNK);
                        }));
    }

    @ParameterizedTest
    @MethodSource("provideShipsWithGameBoards")
    public void all_ship_are_sunk_iff_all_ship_are_sunk(List<IShip> ships, GameBoard board, boolean areAllSunk) {
        ShipPlacements placements = new ShipPlacements();

        for (IShip ship : ships) {
            placements.addShip(5, 5, ship);
        }

        Assertions.assertEquals(areAllSunk, placements.allSunk(board));
    }
}
