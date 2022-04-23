package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.LobbyAPIClient;

public abstract class AbstractLobbyState extends AbstractState {
    protected LobbyAPIClient lobbyAPIClient;

    protected AbstractLobbyState(GameStateManager gsm) {
        super(gsm);
        lobbyAPIClient = new LobbyAPIClient(gsm.getNetworkClient());
    }

    //Changes state to ship placement state
    protected void goToShipPlacement(){ gsm.set(new ShipPlacementState(gsm)); }

    //Changes state to menu state
    protected void goToMenu(){
        gsm.set(new MenuState(gsm));
    };
}
