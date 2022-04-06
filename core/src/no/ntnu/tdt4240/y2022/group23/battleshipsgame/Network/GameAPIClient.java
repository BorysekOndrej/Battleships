package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameState;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

public class GameAPIClient {
    private final INetworkClient network;
    private final String id;

    /**
     * Creates the API object to communicate with the server during the game.
     * @param id the game id received from the server during the Lobby phase.
     */
    public GameAPIClient(INetworkClient network, String id) {
        this.network = network;
        this.id = id;
    }

    /**
     * Send finalized ship placement to the server.
     * @param placements the finalized ship placements
     */
    public void sendShipPlacement(ShipPlacements placements) {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.SEND_PLACEMENT.name());
        request.put("id", id);
        request.put("placements", StringSerializer.toString(placements));
        network.send("/placements", request);
    }

    /**
     * Checks whether everything is ready for the game itself to start.
     * @return true iff the game itself can start
     * @throws CommunicationTerminated if communication has been terminated
     *   by the other party
     */
    public boolean receiveCanGameStart() throws CommunicationTerminated {
        Map<String, String> response = network.receive();
        if (response == null)
            return false;

        ServerClientMessage responseType = ResponseCheckers.checkCommunicationTerminated(response);
        ResponseCheckers.checkUnexpectedType(
                Collections.singletonList(ServerClientMessage.GAME_START),
                responseType
        );
        return true;
    }

    /**
     * Sends action chosen by the user.
     * @param action the chosen action
     */
    public void sendAction(IAction action) {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.SEND_ACTION.name());
        request.put("id", id);
        request.put("action", StringSerializer.toString(action));
        network.send("/action", request);
    }

    /**
     * Sends timeout if the user run out of time picking the next move.
     */
    public void sendTimeout() {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.TIMEOUT.name());
        request.put("id", id);
        network.send("/timeout", request);
    }

    private GameState getNewGameState(boolean thisPlayersBoard) throws CommunicationTerminated {
        Map<String, String> response = network.receive();
        if (response == null)
            return null;

        ServerClientMessage responseType = ResponseCheckers.checkCommunicationTerminated(response);
        ResponseCheckers.checkUnexpectedType(
                Arrays.asList(ServerClientMessage.ACTION_PERFORMED, ServerClientMessage.GAME_OVER),
                responseType
        );

        GameBoard board = StringSerializer.fromString(response.get("board"));
        List<Coords> changedCoords = StringSerializer.fromString(response.get("changedCoords"));
        List<IShip> unsunkShips = StringSerializer.fromString(response.get("unsunkShips"));
        boolean gameOver = responseType == ServerClientMessage.GAME_OVER;
        return new GameState(
                board,
                changedCoords,
                unsunkShips,
                thisPlayersBoard,
                gameOver
        );
    }

    /**
     * Receives the game state after this player's action.
     * IT IS STILL NECESSARY TO CALL THIS FUNCTION EVEN IF USER'S ACTION WAS A TIMEOUT!
     * (i.e. previously called API method was {@link #sendTimeout()}).
     * @return game state of the opponent's board, or null if server did not respond yet
     * @throws CommunicationTerminated if communication has been terminated
     *      by the other party
     */
    public GameState receiveMyActionResult() throws CommunicationTerminated {
        return getNewGameState(false);
    }

    /**
     * Receives the game state after opponent's action.
     * @return game state of the opponent's board, or null if server did not respond yet
     * @throws CommunicationTerminated if communication has been terminated
     *      by the other party
     */
    public GameState receiveOpponentActionResult() throws CommunicationTerminated {
        return getNewGameState(true);
    }

    /**
     * Used to terminate the communication with the server.
     * @throws CommunicationTerminated to allow reacting the same way as when
     * the communication is terminated by the other party
     */
    public void endCommunication() throws CommunicationTerminated {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.END_COMMUNICATION.name());

        network.send("/terminate", request);
        throw new CommunicationTerminated("this user terminated communication");
    }
}
