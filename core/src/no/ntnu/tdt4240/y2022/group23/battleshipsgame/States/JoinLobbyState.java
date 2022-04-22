package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.JoinLobbyStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;

public class JoinLobbyState extends AbstractLobbyState{
    enum WaitingFor { GAME_ID, JOIN_SUCCESSFUL, PLACEMENT_START }

    private String gameId;
    private final JoinLobbyStateGUI joinLobbyStateGUI;
    private WaitingFor waitingFor;

    protected JoinLobbyState(GameStateManager gsm) {
        super(gsm);
        joinLobbyStateGUI = new JoinLobbyStateGUI();
        waitingFor = WaitingFor.GAME_ID;
    }

    @Override
    public void handleInput() throws CommunicationTerminated {
        joinLobbyStateGUI.handleInput();
        if (joinLobbyStateGUI.backButtonPressed()){
            lobbyAPIClient.endCommunication();
            goToMenu();
        }

        //Read code
        if (joinLobbyStateGUI.getCode() != null && gameId == null){
            gameId = joinLobbyStateGUI.getCode();
//            lobbyAPIClient.sendJoinLobbyRequest(gameId);
        }
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        handleInput();
        joinLobbyStateGUI.update(dt);

        switch (waitingFor) {
            case GAME_ID: {
                if (gameId != null) {
                    lobbyAPIClient.sendJoinLobbyRequest(gameId);
                    waitingFor = WaitingFor.JOIN_SUCCESSFUL;
                }
                break;
            }
            case JOIN_SUCCESSFUL: {
                Boolean joinSuccessful = lobbyAPIClient.wasLobbyJoinSuccessful();
                if (Boolean.TRUE.equals(joinSuccessful)) {
                    waitingFor = WaitingFor.PLACEMENT_START;
                } else if (Boolean.FALSE.equals(joinSuccessful)) {
                    joinLobbyStateGUI.resetState();
                    waitingFor = WaitingFor.GAME_ID;
                }
                break;
            }
            case PLACEMENT_START: {
                if (lobbyAPIClient.receiveCanPlacementStart()) {
                    goToShipPlacement();
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        joinLobbyStateGUI.render(sb);
    }

    @Override
    public void dispose() {
        joinLobbyStateGUI.dispose();
    }
}
