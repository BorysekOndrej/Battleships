package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.JoinLobbyStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;

public class JoinLobbyState extends AbstractLobbyState{
    private String gameId;
    private JoinLobbyStateGUI joinLobbyStateGUI;

    protected JoinLobbyState(GameStateManager gsm) {
        super(gsm);
        joinLobbyStateGUI = new JoinLobbyStateGUI();
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
            lobbyAPIClient.sendJoinLobbyRequest(gameId);
        }
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        handleInput();
        joinLobbyStateGUI.update(dt);

        if (gameId != null) {
            Boolean lobbySuccessfullyJoined = lobbyAPIClient.wasLobbyJoinSuccessful();
            if (Boolean.TRUE.equals(lobbySuccessfullyJoined)) {
                goToShipPlacement();
            } else if (Boolean.FALSE.equals(lobbySuccessfullyJoined)) {
                gameId = null;
                joinLobbyStateGUI.resetState();
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
