package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network;

public class LobbyAPIClient {
    /**
     * Sends a request to find a random opponent.
     */
    public void sendJoinMatchmakingRequest() {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Sends a request to create a private lobby.
     * The answer with the game id can be retrieved by
     * {@link #receiveGameId()}.
     */
    public void sendCreateLobbyRequest() {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Possibly receives game id.
     * @return game id if received already, or null if not
     * @throws CommunicationTerminated if communication has been terminated
     *   by the other party
     */
    public String receiveGameId() throws CommunicationTerminated {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Requests joining the game with the given id.
     * @param id the id of the game to join
     */
    public void sendJoinLobbyRequest(String id) {
        throw new UnsupportedOperationException("not implemented");
    }

    /**
     * Checks whether everything is ready for the ship placement to commence.
     * @return true iff the ship placement phase can start
     * @throws CommunicationTerminated if communication has been terminated
     *   by the other party
     */
    public boolean receiveCanPlacementStart() throws CommunicationTerminated {
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
