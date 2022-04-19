package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import static no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MenuStateGUI.BUTTON_WIDTH;
import static no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.MenuStateGUI.BUTTON_HEIGHT;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class RandOppLobbyStateGUI implements IRenderable {
    private final Texture background;
    private final SimpleButton returnButton;

    private final Array<TextureRegion> frames;
    private final float maxFrameTime = 0.7f;
    private float currentFrameTime;
    private final int frameCount = 4;
    private int frame = 0;
    private boolean animationActive;

    public RandOppLobbyStateGUI(){
        background = new Texture("lobby_state/lobby_state_background.png");
        returnButton = new SimpleButton(BattleshipsGame.WIDTH/2 - BUTTON_WIDTH/2, BattleshipsGame.HEIGHT/2 - BUTTON_HEIGHT*7/2, new Texture("lobby_state/return.png"));

        animationActive = true;
        TextureRegion region = new TextureRegion(new Texture("lobby_state/waiting_animation2.png"));
        frames = new Array<>();
        int frameWidth = region.getRegionWidth() / frameCount;
        for(int i = 0; i < frameCount; i++){
            frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
        }
    }

    public void setAnimationActive(boolean active){
        animationActive = active;
    }

    public Vector2 getButtonPos(){
        return new Vector2(returnButton.getXPos(), returnButton.getYPos());
    }

    private TextureRegion getFrame(){
        return frames.get(frame);
    }

    @Override
    public void handleInput() {}

    @Override
    public void update(float dt) {
        handleInput();
        if(animationActive){
            currentFrameTime += dt;
            if(currentFrameTime > maxFrameTime){
                frame++;
                currentFrameTime = 0;
            }
            if(frame >= frameCount) frame = 0;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0,0, BattleshipsGame.WIDTH, BattleshipsGame.HEIGHT);
        if(animationActive) sb.draw(getFrame(), returnButton.getXPos() + 20, returnButton.getYPos() + 900);
        returnButton.render(sb);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        returnButton.dispose();
    }

    public boolean backButtonPressed(){return returnButton.buttonTouched();}
}
