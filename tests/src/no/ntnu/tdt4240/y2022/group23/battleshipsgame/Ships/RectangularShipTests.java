package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;

public class RectangularShipTests {

    private static <T> void assertCollectionsEqualInAnyOrder(Collection<T> lt, Collection<T> rt) {
        Assertions.assertEquals(lt.size(), rt.size());
        Assertions.assertTrue(lt.containsAll(rt));
        Assertions.assertTrue(rt.containsAll(lt));
    }

    private static Stream<Arguments> provideShipParams() {
        return Stream.of(
                Arguments.of(new Coords(0, 0), 1, true, Collections.singletonList(new Coords(0, 0))),
                Arguments.of(new Coords(10, 11), 1, true, Collections.singletonList(new Coords(10, 11))),
                Arguments.of(new Coords(0, 0), 3, true, Arrays.asList(
                        new Coords(0, 0),
                        new Coords(1, 0),
                        new Coords(2, 0)
                )),
                Arguments.of(new Coords(0, 0), 2, false, Arrays.asList(
                        new Coords(0, 0),
                        new Coords(0, 1)
                )),
                Arguments.of(new Coords(10, 11), 2, true, Arrays.asList(
                        new Coords(10, 11),
                        new Coords(11, 11)
                )),
                Arguments.of(new Coords(66, 1061), 3, false, Arrays.asList(
                        new Coords(66, 1061),
                        new Coords(66, 1062),
                        new Coords(66, 1063)
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("provideShipParams")
    public void ship_has_correct_positions(Coords start, int numOfSquares, boolean horizontal, List<Coords> correct) {
        IShip ship = new RectangularShip(start, numOfSquares, horizontal);
        assertCollectionsEqualInAnyOrder(correct, ship.getPositions());
    }

    private static Stream<Arguments> provideShipSinkingFillers() {
        return Stream.of(
                Arguments.of((Function<Coords, GameBoardField>)
                        coords -> GameBoardField.HIT, true, "setting positions to HIT"),
                Arguments.of((Function<Coords, GameBoardField>)
                        coords -> GameBoardField.SUNK, true, "setting positions to SUNK")
        );
    }

    private static Stream<Arguments> provideNonShipSinkingFillers() {
        return Stream.of(GameBoardField.values())
                .filter(field -> field != GameBoardField.HIT && field != GameBoardField.SUNK)
                .map(field ->
                        Arguments.of((Function<Coords, GameBoardField>)
                                coords -> field, false, "setting positions to " + field.toString()
                        )
                );
    }

    private static Stream<Arguments> provideMixedFillers() {
        return Stream.of(GameBoardField.values())
                .filter(field -> field != GameBoardField.HIT && field != GameBoardField.SUNK)
                .map(field ->
                        Arguments.of((Function<Coords, GameBoardField>)
                                coords -> coords.x == 0 ? field : GameBoardField.HIT, false, "setting all except 0 position to " + field.toString()
                        )
                );
        }

    @ParameterizedTest
    @MethodSource({"provideShipSinkingFillers", "provideNonShipSinkingFillers", "provideMixedFillers"})
    public void ship_sunk_iff_fully_hit_or_sunk(Function<Coords, GameBoardField> filler, boolean correct, String message) {
        IShip ship = new RectangularShip(new Coords(0, 0), 5, true);
        GameBoard board = new GameBoard(5, 5);

        for (Coords coords : ship.getPositions()) {
            board.set(coords, filler.apply(coords));
        }

        Assertions.assertEquals(correct, ship.isSunk(board), message);
    }

    @ParameterizedTest
    @MethodSource({"provideShipSinkingFillers", "provideNonShipSinkingFillers", "provideMixedFillers"})
    public void checking_if_sunk_does_not_change_game_board(Function<Coords, GameBoardField> filler, boolean correct, String message) {
        IShip ship = new RectangularShip(new Coords(0, 0), 5, true);
        GameBoard board = new GameBoard(5, 5);

        for (Coords coords : ship.getPositions()) {
            board.set(coords, filler.apply(coords));
        }

        GameBoard original = new GameBoard(board);
        ship.isSunk(board);

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                Assertions.assertEquals(original.get(new Coords(x, y)), board.get(new Coords(x, y)));
            }
        }
    }

    private static Stream<Triplet<Integer, Boolean, Coords>> provideShipParts() {
        return IntStream.rangeClosed(1, 5)
                .mapToObj(size -> Stream.of(
                        Pair.with(size, true),
                        Pair.with(size, false)
                ))
                .flatMap(Function.identity())
                .flatMap(pair -> Stream.of(
                        Triplet.with(pair.getValue0(), pair.getValue1(), new Coords(7, 5)),
                        Triplet.with(pair.getValue0(), pair.getValue1(), new Coords(1, 3))
                ));
    }

    private static Stream<Arguments> provideShipArguments() {
        return provideShipParts().map(
                triplet -> Arguments.of(
                        triplet.getValue0(),
                        triplet.getValue1(),
                        triplet.getValue2()
                ));
    }

    @ParameterizedTest
    @MethodSource("provideShipArguments")
    public void ship_is_displaced_correctly(int size, boolean horizontal, Coords start) {
        RectangularShip ship = new RectangularShip(start, size, horizontal);
        ship.displace();
        List<Coords> displaced = ship.getPositions();
        Assertions.assertEquals(new Coords(0, 0), displaced.get(0));

        assertCollectionsEqualInAnyOrder(
                IntStream.range(0, size).mapToObj(x -> new Coords(x, 0)).collect(Collectors.toList()),
                displaced
        );
    }

    @ParameterizedTest
    @MethodSource("provideShipArguments")
    public void ship_is_placed_correctly(int size, boolean horizontal, Coords start) {
        RectangularShip ship = new RectangularShip(new Coords(0, 0), size, horizontal);
        ship.placeShip(start);
        List<Coords> placed = ship.getPositions();
        Assertions.assertEquals(start, placed.get(0));

        assertCollectionsEqualInAnyOrder(
                IntStream.range(0, size)
                    .mapToObj(
                        i -> horizontal ?
                                new Coords(start.x + i, start.y) : new Coords(start.x, start.y + i))
                    .collect(Collectors.toList()),
                placed
        );
    }

    @ParameterizedTest
    @MethodSource("provideShipArguments")
    public void ship_is_rotated_correctly(int size, boolean horizontal, Coords start) {
        RectangularShip ship = new RectangularShip(start, size, horizontal);
        List<Coords> original = ship.getPositions();
        for (int i = 0; i < 4; i++) {
            ship.rotateClockwise();
            List<Coords> rotated = ship.getPositions();

            assertCollectionsEqualInAnyOrder(
                    original.stream().map(coords -> new Coords(-(coords.y - start.y) + start.x, coords.x - start.x + start.y)).collect(Collectors.toList()),
                    rotated
            );
            original = rotated;
        }
    }

    private static Stream<Arguments> provideDoubleShipArguments() {
        return provideShipParts()
                .flatMap(ship1 -> provideShipParts().map(ship2 -> Arguments.of(ship1, ship2)));
    }

    @ParameterizedTest
    @MethodSource("provideDoubleShipArguments")
    public void only_same_type_ships_equal(Triplet<Integer, Boolean, Coords> shipParts1, Triplet<Integer, Boolean, Coords> shipParts2) {
        RectangularShip ship1 = new RectangularShip(shipParts1.getValue2(), shipParts1.getValue0(), shipParts1.getValue1());
        RectangularShip ship2 = new RectangularShip(shipParts2.getValue2(), shipParts2.getValue0(), shipParts2.getValue1());

        if (shipParts1.getValue0().equals(shipParts2.getValue0())) {
            Assertions.assertEquals(ship1, ship2);
        } else {
            Assertions.assertNotEquals(ship1, ship2);
        }
    }

    @Test
    public void ship_not_equal_to_null() {
        RectangularShip ship = new RectangularShip(new Coords(1, 3), 3, false);
        Assertions.assertNotEquals(ship, null);
    }
}
