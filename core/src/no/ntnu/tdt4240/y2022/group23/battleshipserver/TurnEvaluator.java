package no.ntnu.tdt4240.y2022.group23.battleshipserver;

import java.util.Iterator;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardChange;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

public class TurnEvaluator {
    private final GameBoard beforeBoard;
    private final GameBoard afterBoard;
    private final NextTurn nextTurn;

    private void sinkShips(ShipPlacements ships, List<GameBoardChange> changes) {
        for (GameBoardChange change : changes) {
            IShip ship = ships.getShipOnCoords(change.coords);
            if (ship != null && ship.isSunk(afterBoard)) {
                for (Coords coords : ship.getPositions()) {
                    afterBoard.set(coords, GameBoardField.SUNK);
                }
            }
        }
    }

    private boolean hitAny(List<GameBoardChange> changes) {
        for (GameBoardChange change : changes) {
            if (beforeBoard.get(change.coords) != afterBoard.get(change.coords) &&
                    (change.newField == GameBoardField.HIT || change.newField == GameBoardField.SUNK))
                return true;
        }
        return false;
    }

    private void filterUnchanged(List<GameBoardChange> changes) {
        Iterator<GameBoardChange> it = changes.listIterator();
        while (it.hasNext()) {
            GameBoardChange change = it.next();
            if (beforeBoard.get(change.coords) == afterBoard.get(change.coords))
                it.remove();
        }
    }

    public TurnEvaluator(ShipPlacements ships, GameBoard gameBoard, IAction action) {
        beforeBoard = gameBoard;
        List<GameBoardChange> changes = action.affect(ships, beforeBoard);
        afterBoard = new GameBoard(beforeBoard);
        for (GameBoardChange change : changes) {
            afterBoard.set(change.coords, change.newField);
        }

        filterUnchanged(changes);

         if (!hitAny(changes)) {
             nextTurn = NextTurn.OTHERS_TURN;
             return;
         }
         sinkShips(ships, changes);

         nextTurn = ships.allSunk(afterBoard) ? NextTurn.GAME_OVER : NextTurn.MY_TURN;
    }

    public GameBoard boardBeforeTurn(){ return beforeBoard; }
    public GameBoard boardAfterTurn(){ return afterBoard; }

    /**
     * @return information on whether the next turn should be played by this turn's attacker
     * (MY_TURN) or the opponent (OTHERS_TURN) or if the game is over
     */
    public NextTurn nextTurn() { return nextTurn; }
}
