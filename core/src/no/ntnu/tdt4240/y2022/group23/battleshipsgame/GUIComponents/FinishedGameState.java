package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MenuStateGUI.BUTTON_WIDTH;
import static no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MenuStateGUI.BUTTON_HEIGHT;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class FinishedGameState implements IRenderable {

    private final Texture background;
    private final SimpleButton playAgainButton;

    public FinishedGameState(boolean winner){
        playAgainButton = new SimpleButton(BattleshipsGame.WIDTH/2 - BUTTON_WIDTH/2, BattleshipsGame.HEIGHT/2 - BUTTON_HEIGHT*7/2, new Texture("finished_game_menu/play_again.png"));

        if(winner) background = new Texture("finished_game_menu/win_background.png");
        else background = new Texture("finished_game_menu/loss_background.png");
    }

    public boolean playAgainButtonPressed(){
        return playAgainButton.buttonTouched();
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0,0, BattleshipsGame.WIDTH, BattleshipsGame.HEIGHT);
        playAgainButton.render(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        playAgainButton.dispose();
    }
}
