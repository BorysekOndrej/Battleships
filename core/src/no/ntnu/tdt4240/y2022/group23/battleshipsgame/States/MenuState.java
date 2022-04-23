package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MenuStateGUI;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.LobbyAPIClient;

public class MenuState extends AbstractState {
    enum WaitingFor { ELO_SENT, ELO_RECEIVED , NOTHING}

    private MenuStateGUI mainMenu;
    private String elo = null;

    //API for elo
    protected LobbyAPIClient lobbyAPIClient;
    private WaitingFor waitingFor;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        mainMenu = new MenuStateGUI();
        lobbyAPIClient = new LobbyAPIClient(gsm.getNetworkClient());
        waitingFor = WaitingFor.ELO_SENT;
    }

    //Handles the input of clicking the button
    public void handleInput(){
        if (mainMenu.practiseButtonPressed()){
            lobbyAPIClient.endCommunication();
            goToPractice();
        }
        if (mainMenu.createLobbyButtonPressed()){
            lobbyAPIClient.endCommunication();
            goToCreateLobby();
        }
        if (mainMenu.joinLobbyButtonPressed()){
            lobbyAPIClient.endCommunication();
            goToJoinLobby();
        }
        if (mainMenu.rankedButtonPressed()){
            lobbyAPIClient.endCommunication();
            goToRanked();
        }
    }

    //Renders the main menu
    @Override
    public void render(SpriteBatch sb) {
        mainMenu.render(sb);
    }

    @Override
    public void update(float dt) throws CommunicationTerminated {
        handleInput();
        switch (waitingFor){
            case ELO_SENT: {
                lobbyAPIClient.sendRequestCurrentELO();
                waitingFor = WaitingFor.ELO_RECEIVED;
                break;
            }
            case ELO_RECEIVED: {
                elo = lobbyAPIClient.receiveELO();
                if (elo != null){
                    mainMenu.setRankingScore(elo);
                    waitingFor = WaitingFor.NOTHING;
                }
                break;
            }
        }
    }

    //Changes state to a practice matchmaking state
    private void goToPractice(){ gsm.set(new MatchmakingLobbyState(gsm,false));}

    //Changes state to a ranked matchmaking state
    private void goToRanked(){ gsm.set(new MatchmakingLobbyState(gsm,true));}

    //Changes state to create lobby state
    private void goToCreateLobby(){ gsm.set(new CreateLobbyState(gsm));}

    //Changes state to join lobby state
    private void goToJoinLobby(){ gsm.set(new JoinLobbyState(gsm));}

    @Override
    public void dispose(){
        mainMenu.dispose();
    }
}
