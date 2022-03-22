package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
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
}
