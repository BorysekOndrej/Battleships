package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;

public class MatchmakingLobbyState extends AbstractLobbyState{

    protected MatchmakingLobbyState(GameStateManager gsm) {
        super(gsm);
        lobbyAPIClient.sendJoinMatchmakingRequest();
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        handleInput();
        if (lobbyAPIClient.receiveCanPlacementStart()){
            goToShipPlacement();
        }
    }
}
