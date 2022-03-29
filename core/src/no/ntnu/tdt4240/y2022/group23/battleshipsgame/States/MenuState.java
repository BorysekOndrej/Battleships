package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.ActionButton;

public class MenuState extends AbstractState {
    private ActionButton lobbyButton;

    protected MenuState(GameStateManager gsm) {
        super(gsm);
        lobbyButton = new ActionButton(); //Action is executing goToLobbyState
    }

    //Handles the input of clicking the button
    protected void handleInput(){};

    //Changes state to lobby state
    private void goToLobbyState(){
        gsm.set(new LobbyState(gsm));
    }

    public void render(SpriteBatch sb);

    public void dispose();
}
