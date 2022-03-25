package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import org.javatuples.Triplet;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

public class GameAPIClient {
    private final String id;

    /**
     * Creates the API object to communicate with the server during the game.
     * @param id the game id received from the server during the Lobby phase.
     */
    public GameAPIClient(String id) {
        this.id = id;
    }

    /**
     * Send finalized ship placement to the server.
     * @param placements the finalized ship placements
     */
    public void sendShipPlacement(ShipPlacements placements) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Checks whether everything is ready for the game itself to start.
     * @return true iff the game itself can start
     * @throws CommunicationTerminated if communication has been terminated
     *   by the other party
     */
    public boolean receiveCanGameStart() throws CommunicationTerminated {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Sends action chosen by the user.
     * @param action the chosen action
     */
    public void sendAction(IAction action) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Sends timeout if the user run out of time picking the next move.
     */
    public void sendTimeout() {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Receives the game state after this player's action.
     * @return triplet composed of opponent's board, its affected coordinates and the list of yet
     *      unsunk ships (passed as classes to avoid leaking of their placement), or null if server
     *      did not respond yet
     * @throws CommunicationTerminated if communication has been terminated
     *      by the other party
     */
    public Triplet<GameBoard, List<Coords>, List<Class<IShip>>>
    receiveMyActionResult() throws CommunicationTerminated {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Receives the game state after opponent's action.
     * @return triplet composed of this player's board, its affected coordinates and the list of yet
     *      unsunk ships (passed as classes to avoid leaking of their placement), or null if server
     *      did not respond yet
     * @throws CommunicationTerminated if communication has been terminated
     *      by the other party
     */
    public Triplet<GameBoard, List<Coords>, List<Class<IShip>>>
    receiveOpponentActionResult() throws CommunicationTerminated {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Used to terminate the communication with the server.
     * @throws CommunicationTerminated to allow reacting the same way as when
     * the communication is terminated by the other party
     */
    public void endCommunication() throws CommunicationTerminated {
        throw new UnsupportedOperationException("not implemented");
    }
}
