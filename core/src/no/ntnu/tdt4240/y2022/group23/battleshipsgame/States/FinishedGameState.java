package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.FinishedGame;

public class FinishedGameState extends AbstractState {

    private FinishedGame finishedGame;

    protected FinishedGameState(GameStateManager gsm) {
        super(gsm);
        finishedGame = new FinishedGame();
    }

    //Changes state to menu state
    private void goToMenu(){
        gsm.set(new MenuState(gsm));
    };

    @Override
    public void handleInput() {
        if (finishedGame.backButtonPressed()){
            goToMenu();
        }
    }

    @Override
    public void update(float dt) {
        finishedGame.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        finishedGame.render(sb);
    }

    @Override
    public void dispose() {
        finishedGame.dispose();
    }
}
