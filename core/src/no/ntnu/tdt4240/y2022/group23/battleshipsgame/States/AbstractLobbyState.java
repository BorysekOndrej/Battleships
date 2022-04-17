package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.Lobby;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.LobbyAPIClient;

public abstract class AbstractLobbyState extends AbstractState {
    private Lobby lobby;

    protected LobbyAPIClient lobbyAPIClient;

    protected AbstractLobbyState(GameStateManager gsm) {
        super(gsm);
        lobby = new Lobby();
        lobbyAPIClient = new LobbyAPIClient(gsm.getNetworkClient());
    }

    @Override
    public void handleInput() throws CommunicationTerminated {
        if (lobby.backButtonPressed()){
            lobbyAPIClient.endCommunication();
            goToMenu();
        }
    };

    //Changes state to ship placement state
    protected void goToShipPlacement(){ gsm.set(new ShipPlacementState(gsm)); }

    //Changes state to menu state
    protected void goToMenu(){
        gsm.set(new MenuState(gsm));
    };

    @Override
    public void render(SpriteBatch sb){
        lobby.render(sb);
    }

    @Override
    public void dispose(){
        lobby.dispose();
    };
}
