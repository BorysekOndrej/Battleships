package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import org.javatuples.Quartet;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameState;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.NextTurn;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

public class GameAPIClient {
    private final INetworkClient network;

    /**
     * Creates the API object to communicate with the server during the game.
     */
    public GameAPIClient(INetworkClient network) {
        this.network = network;
    }

    /**
     * Send finalized ship placement to the server.
     * @param placements the finalized ship placements
     */
    public void sendShipPlacement(ShipPlacements placements) {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.SEND_PLACEMENT.name());
        request.put("placements", StringSerializer.toString(placements));
        network.send("/placements", request);
    }

    /**
     * Checks whether everything is ready for the game itself to start.
     * @return quartet where the first attribute is true iff the game can start and if so, the
     *   second attribute shows this player's board, the third attribute shows the opponent's
     *   board and the fourth attribute says who starts the game
     * @throws CommunicationTerminated if communication has been terminated
     *   by the other party
     */
    public Quartet<Boolean, GameBoard, GameBoard, NextTurn> receiveCanGameStart() throws CommunicationTerminated {
        Map<String, String> response = network.receive();
        if (response == null)
            return new Quartet<>(false, null, null, null);

        ServerClientMessage responseType = ResponseCheckers.checkCommunicationTerminated(response);
        ResponseCheckers.checkUnexpectedType(
                Collections.singletonList(ServerClientMessage.GAME_START),
                responseType
        );
        return new Quartet<>(
                true,
                StringSerializer.fromString(response.get("myBoard")),
                StringSerializer.fromString(response.get("opponentBoard")),
                NextTurn.valueOf(response.get("nextTurn"))
        );
    }

    /**
     * Sends action chosen by the user.
     * @param action the chosen action
     */
    public void sendAction(IAction action) {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.SEND_ACTION.name());
        request.put("action", StringSerializer.toString(action));
        network.send("/action", request);
    }

    /**
     * Sends timeout if the user run out of time picking the next move.
     */
    public void sendTimeout() {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.TIMEOUT.name());
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
        NextTurn nextTurn = NextTurn.valueOf(response.get("nextTurn"));
        return new GameState(
                board,
                changedCoords,
                unsunkShips,
                thisPlayersBoard,
                nextTurn
        );
    }

    /**
     * Receives the game state after this player's action.
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
