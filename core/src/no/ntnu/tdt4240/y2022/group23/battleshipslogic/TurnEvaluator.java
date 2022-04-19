package no.ntnu.tdt4240.y2022.group23.battleshipslogic;

import java.util.List;
import java.util.stream.Collectors;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardChange;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;

public class TurnEvaluator {
    private final GameBoard beforeBoard;
    private final GameBoard afterBoard;
    private final List<GameBoardChange> changedCoords;
    private final NextTurn nextTurn;

    private void sinkShips(ShipPlacements ships, List<GameBoardChange> changes) {
        changes.stream()
            .map(change -> ships.getShipOnCoords(change.coords))
            .distinct()
            .filter(ship -> ship != null && ship.isSunk(afterBoard))
            .forEach(ship -> {
                for (Coords coords : ship.getPositions())
                    afterBoard.set(coords, GameBoardField.SUNK);
            });
    }

    private boolean hitAny(List<GameBoardChange> changes) {
        return changes.stream()
                .anyMatch(change -> change.newField == GameBoardField.HIT
                                 || change.newField == GameBoardField.SUNK);
    }

    private List<GameBoardChange> filterUnchanged(List<GameBoardChange> changes) {
        return changes.stream()
            .filter(change -> beforeBoard.get(change.coords) != afterBoard.get(change.coords))
            .collect(Collectors.toList());
    }

    public TurnEvaluator(ShipPlacements ships, GameBoard gameBoard, IAction action) {
        beforeBoard = gameBoard;
        List<GameBoardChange> changes = action.affect(ships, beforeBoard);
        afterBoard = new GameBoard(beforeBoard);
        afterBoard.apply(changes);

        changedCoords = filterUnchanged(changes);

         if (!hitAny(changedCoords)) {
             nextTurn = NextTurn.OTHERS_TURN;
             return;
         }
         sinkShips(ships, changedCoords);

         nextTurn = ships.allSunk(afterBoard) ? NextTurn.GAME_OVER : NextTurn.MY_TURN;
    }

    public GameBoard boardBeforeTurn(){ return beforeBoard; }
    public GameBoard boardAfterTurn(){ return afterBoard; }

    /**
     * @return information on whether the next turn should be played by this turn's attacker
     * (MY_TURN) or the opponent (OTHERS_TURN) or if the game is over
     */
    public NextTurn nextTurn() { return nextTurn; }

    public List<GameBoardChange> getChangedCoords() { return changedCoords; }
}
