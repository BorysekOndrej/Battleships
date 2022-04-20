package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.RandOppLobbyStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;

public class MatchmakingLobbyState extends AbstractLobbyState{
    private RandOppLobbyStateGUI randOppLobbyStateGUI;

    protected MatchmakingLobbyState(GameStateManager gsm) {
        super(gsm);
        randOppLobbyStateGUI = new RandOppLobbyStateGUI();
        lobbyAPIClient.sendJoinMatchmakingRequest();
    }

    @Override
    public void handleInput() throws CommunicationTerminated {
        if (randOppLobbyStateGUI.backButtonPressed()){
            lobbyAPIClient.endCommunication();
            goToMenu();
        }
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        handleInput();
        if (lobbyAPIClient.receiveCanPlacementStart()){
            goToShipPlacement();
        }
    }

    @Override
    public void render(SpriteBatch sb){
        randOppLobbyStateGUI.render(sb);
    }

    @Override
    public void dispose(){
        randOppLobbyStateGUI.dispose();
    }
}
