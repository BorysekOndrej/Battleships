package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;

public class CreateLobbyState extends AbstractLobbyState{
    private String gameId;

    protected CreateLobbyState(GameStateManager gsm) {
        super(gsm);
        lobbyAPIClient.sendCreateLobbyRequest();
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        handleInput();
        if (gameId == null){
            gameId = lobbyAPIClient.receiveGameId();
        }
        else if (lobbyAPIClient.receiveCanPlacementStart()){
            goToShipPlacement();
        }
    }
}
