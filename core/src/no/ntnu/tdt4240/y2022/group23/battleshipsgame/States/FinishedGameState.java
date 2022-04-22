package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.FinishedGameStateGUI;

public class FinishedGameState extends AbstractState {
    private FinishedGameStateGUI finishedGameStateGUI;

    protected FinishedGameState(GameStateManager gsm, boolean isPlayerWinner) {
        super(gsm);
        finishedGameStateGUI = new FinishedGameStateGUI(isPlayerWinner);
    }

    //Changes state to menu state
    private void goToMenu(){
        gsm.set(new MenuState(gsm));
    };

    @Override
    public void handleInput() {
        if (finishedGameStateGUI.playAgainButtonPressed()){
            goToMenu();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        finishedGameStateGUI.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        finishedGameStateGUI.render(sb);
    }

    @Override
    public void dispose() {
        finishedGameStateGUI.dispose();
    }
}
