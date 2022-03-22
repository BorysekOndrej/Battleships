package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.ISerializable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardChange;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;

public interface IAction extends ISerializable {

    /**
     * Obtains the action's effect on the board passed.
     * @param ships the true placements of the ships
     * @param gameBoard the player's view of the game board state
     * @return a list of changes the action makes to the gameBoard
     */
    List<GameBoardChange> affect(ShipPlacements ships, GameBoard gameBoard);
}
