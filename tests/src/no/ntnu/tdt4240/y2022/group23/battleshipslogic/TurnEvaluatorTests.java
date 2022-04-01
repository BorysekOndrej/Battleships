package no.ntnu.tdt4240.y2022.group23.battleshipslogic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.Radar;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.SingleShot;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

public class TurnEvaluatorTests {
    private GameBoard board;
    private ShipPlacements placements;

    @BeforeEach
    public void setup() {
        board = new GameBoard(5, 5);
        placements = new ShipPlacements();

        IShip ship = new RectangularShip(new Coords(0, 0), 5, true);
        placements.addShip(board.getWidth(), board.getHeight(), ship);
    }

    @Test
    public void when_shot_misses_other_player_turn_comes() {
        Coords hitCoords = new Coords(2, 2);
        IAction action = new SingleShot(hitCoords);

        TurnEvaluator turnEvaluator = new TurnEvaluator(placements, board, action);

        Assertions.assertEquals(GameBoardField.WATER, turnEvaluator.boardAfterTurn().get(hitCoords));
        Assertions.assertEquals(NextTurn.OTHERS_TURN, turnEvaluator.nextTurn());
    }

    @Test
    public void when_shot_hits_same_player_turn_comes() {
        Coords hitCoords = new Coords(0, 0);
        IAction action = new SingleShot(hitCoords);

        TurnEvaluator turnEvaluator = new TurnEvaluator(placements, board, action);

        Assertions.assertEquals(GameBoardField.HIT, turnEvaluator.boardAfterTurn().get(hitCoords));
        Assertions.assertEquals(NextTurn.MY_TURN, turnEvaluator.nextTurn());
    }

    @Test
    public void when_all_ships_are_sunk_game_ends() {
        for (int x = 0; x < board.getWidth() - 1; x++) {
            board.set(new Coords(x, 0), GameBoardField.HIT);
        }

        Coords hitCoords = new Coords(4, 0);
        IAction action = new SingleShot(hitCoords);

        TurnEvaluator turnEvaluator = new TurnEvaluator(placements, board, action);

        Assertions.assertAll(
                IntStream.range(0, board.getWidth())
                .mapToObj(x -> () -> Assertions.assertEquals(
                        GameBoardField.SUNK,
                        turnEvaluator.boardAfterTurn().get(new Coords(x, 0))
                ))
        );
        Assertions.assertEquals(NextTurn.GAME_OVER, turnEvaluator.nextTurn());
    }

    @Test
    public void when_ship_sinks_but_not_all_same_player_turn_comes() {
        for (int x = 0; x < board.getWidth() - 1; x++) {
            board.set(new Coords(x, 0), GameBoardField.HIT);
        }
        IShip ship = new RectangularShip(new Coords(0, 4), 5, true);
        placements.addShip(board.getWidth(), board.getHeight(), ship);

        Coords hitCoords = new Coords(4, 0);
        IAction action = new SingleShot(hitCoords);

        TurnEvaluator turnEvaluator = new TurnEvaluator(placements, board, action);

        Assertions.assertAll(
                IntStream.range(0, board.getWidth())
                        .mapToObj(x -> () -> Assertions.assertEquals(
                                GameBoardField.SUNK,
                                turnEvaluator.boardAfterTurn().get(new Coords(x, 0))
                        ))
        );
        Assertions.assertEquals(NextTurn.MY_TURN, turnEvaluator.nextTurn());
    }

    @Test
    public void if_radar_finds_ship_others_turn_comes() {
        Coords hitCoords = new Coords(2, 0);
        IAction action = new Radar(hitCoords);

        TurnEvaluator turnEvaluator = new TurnEvaluator(placements, board, action);

        GameBoard afterBoard = turnEvaluator.boardAfterTurn();
        Assertions.assertAll(
                () -> Assertions.assertEquals(GameBoardField.SHIP, afterBoard.get(new Coords(hitCoords.x - 1, hitCoords.y))),
                () -> Assertions.assertEquals(GameBoardField.SHIP, afterBoard.get(hitCoords)),
                () -> Assertions.assertEquals(GameBoardField.SHIP, afterBoard.get(new Coords(hitCoords.x + 1, hitCoords.y))),
                () -> Assertions.assertEquals(GameBoardField.WATER, afterBoard.get(new Coords(hitCoords.x, hitCoords.y + 1)))
        );
        Assertions.assertEquals(NextTurn.OTHERS_TURN, turnEvaluator.nextTurn());
    }

    @Test
    public void if_radar_does_not_find_ship_others_turn_comes() {
        Coords hitCoords = new Coords(2, 2);
        IAction action = new Radar(hitCoords);

        TurnEvaluator turnEvaluator = new TurnEvaluator(placements, board, action);

        GameBoard afterBoard = turnEvaluator.boardAfterTurn();
        Assertions.assertAll(
                Stream.of(Arrays.asList(0, -1), Arrays.asList(-1, 0), Arrays.asList(0, 0), Arrays.asList(1, 0), Arrays.asList(0, 1))
                .map(change -> () -> Assertions.assertEquals(
                        GameBoardField.WATER,
                        afterBoard.get(new Coords(hitCoords.x + change.get(0), hitCoords.y + change.get(1)))
                ))
        );
        Assertions.assertEquals(NextTurn.OTHERS_TURN, turnEvaluator.nextTurn());
    }
}
