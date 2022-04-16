package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;

public class JoinLobbyState extends AbstractLobbyState{
    private String gameId;
    private Boolean lobbySuccessfullyJoined;

    protected JoinLobbyState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void handleInput() throws CommunicationTerminated {
        super.handleInput();

        //Read code
        if (true){ //PLACEHOLDER FOR USER CLICKING BUTTON TO JOIN LOBBY
            if (gameId == null){
                gameId = lobbyAPIClient.receiveGameId();
            }
        }
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        handleInput();
        if (lobbySuccessfullyJoined == null){
            lobbySuccessfullyJoined = lobbyAPIClient.wasLobbyJoinSuccessful();
        }
        else if (lobbySuccessfullyJoined){
            goToShipPlacement();
        }
        else{
            lobbySuccessfullyJoined = null;
            gameId = null;
            //Show message that the connection was unsuccessful
        }
    }
}
