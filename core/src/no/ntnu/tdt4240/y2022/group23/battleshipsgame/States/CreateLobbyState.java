package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.CreateLobbyStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;

public class CreateLobbyState extends AbstractLobbyState{
    private CreateLobbyStateGUI createLobbyStateGUI;
    private String gameId;

    protected CreateLobbyState(GameStateManager gsm) {
        super(gsm);
        lobbyAPIClient.sendCreateLobbyRequest();
        createLobbyStateGUI = new CreateLobbyStateGUI();
    }

    @Override
    public void handleInput() throws CommunicationTerminated {
        createLobbyStateGUI.handleInput();
        if (createLobbyStateGUI.backButtonPressed()){
            lobbyAPIClient.endCommunication();
            goToMenu();
        }
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        handleInput();
        createLobbyStateGUI.update(dt);

        if (gameId == null){
            gameId = lobbyAPIClient.receiveGameId();
            if (gameId != null){
                createLobbyStateGUI.setCode(gameId);
            }
        }
        else if (lobbyAPIClient.receiveCanPlacementStart()) {
            goToShipPlacement();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        createLobbyStateGUI.render(sb);
    }

    @Override
    public void dispose() {
        createLobbyStateGUI.dispose();
    }
}
