package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

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
        Assumptions.assumeTrue(isInbounds(dimensions, change.coords));

        GameBoard board = new GameBoard(dimensions[0], dimensions[1]);
        board.apply(Collections.singletonList(change));
        Assertions.assertEquals(change.newField, board.get(change.coords));
    }

    @ParameterizedTest
    @MethodSource({"provideDimsAndChange"})
    public void change_affect_no_coords_except_its_own(int[] dimensions, GameBoardChange change) {
        Assumptions.assumeTrue(isInbounds(dimensions, change.coords));

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
}
