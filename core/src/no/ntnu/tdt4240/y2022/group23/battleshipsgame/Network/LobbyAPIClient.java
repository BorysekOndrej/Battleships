package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LobbyAPIClient {
    private final INetworkClient network;

    public LobbyAPIClient(INetworkClient network) {
       this.network = network;
    }

    /**
     * Sends a request to find a random opponent. (Casual mode)
     */
    public void sendJoinCasualMatchmakingRequest() {
        sendGenericJoinMatchmakingRequest(true);
    }

    /**
     * Sends a request to find a random opponent. (Ranked mode)
     */
    public void sendJoinRankedMatchmakingRequest() {
        sendGenericJoinMatchmakingRequest(false);
    }

    private void sendGenericJoinMatchmakingRequest(boolean casual) {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.JOIN_MATCHMAKING.name());
        request.put("privateLobby", Boolean.toString(casual));
        network.send("/join_matchmaking", request);
    }

    /**
     * Sends a request to create a private lobby.
     * The answer with the game id can be retrieved by
     * {@link #receiveGameId()}.
     */
    public void sendCreateLobbyRequest() {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.CREATE_LOBBY.name());
        request.put("privateLobby", Boolean.toString(true));
        network.send("/create_lobby", request);
    }

    /**
     * Possibly receives game id.
     * @return game id if received already, or null if not
     * @throws CommunicationTerminated if communication has been terminated
     *   by the other party
     */
    public String receiveGameId() throws CommunicationTerminated {
        Map<String, String> response = network.receive();
        if (response == null)
            return null;

        ServerClientMessage responseType = ResponseCheckers.checkCommunicationTerminated(response);
        ResponseCheckers.checkUnexpectedType(
                Collections.singletonList(ServerClientMessage.JOINED_LOBBY_WITH_ID),
                responseType
        );

        return response.get("id");
    }

    /**
     * Requests joining the game with the given id. Use {@link #wasLobbyJoinSuccessful()} to see
     * if the join was successful.
     * @param maybeId the id of the game to join
     */
    public void sendJoinLobbyRequest(String maybeId) {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.JOIN_LOBBY.toString());
        request.put("id", maybeId);
        network.send("/join_lobby", request);
    }

    /**
     * Let's client know it if was possible to join the lobby as requested by
     * {@link #sendJoinLobbyRequest(String)}.
     * @return null if no response from the server was received yet, true iff join was successful
     * @throws CommunicationTerminated if communication has been terminated
     *      by the other party
     */
    public Boolean wasLobbyJoinSuccessful() throws CommunicationTerminated {
        Map<String, String> response = network.receive();
        if (response == null)
            return null;

        ServerClientMessage responseType = ResponseCheckers.checkCommunicationTerminated(response);
        ResponseCheckers.checkUnexpectedType(
                Arrays.asList(
                        ServerClientMessage.JOINED_LOBBY_WITH_ID,
                        ServerClientMessage.NO_SUCH_LOBBY
                ),
                responseType
        );
        return responseType == ServerClientMessage.JOINED_LOBBY_WITH_ID;
    }

    /**
     * Checks whether everything is ready for the ship placement to commence.
     * @return true iff the ship placement phase can start
     * @throws CommunicationTerminated if communication has been terminated
     *   by the other party
     */
    public boolean receiveCanPlacementStart() throws CommunicationTerminated {
        Map<String, String> response = network.receive();
        if (response == null)
            return false;

        ServerClientMessage responseType = ResponseCheckers.checkCommunicationTerminated(response);
        ResponseCheckers.checkUnexpectedType(
                Collections.singletonList(ServerClientMessage.PLACEMENT_START),
                responseType
        );

        return true;
    }

    /**
     * Used to terminate the communication with the server.
     */
    public void endCommunication() {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.END_COMMUNICATION.name());

        network.send("/terminate", request);
    }

    public void sendRequestCurrentELO() {
        Map<String, String> request = new HashMap<>();
        request.put("type", ClientServerMessage.REQUEST_ELO_UPDATE.name());
        network.send("/elo", request);
    }

    public String receiveELO() throws CommunicationTerminated {
        Map<String, String> response = network.receive();
        if (response == null) {
            return null;
        }

        ServerClientMessage responseType = ResponseCheckers.checkCommunicationTerminated(response);
        ResponseCheckers.checkUnexpectedType(
                Collections.singletonList(ServerClientMessage.ELO_UPDATE),
                responseType
        );

        return response.get("elo");
    }
}
