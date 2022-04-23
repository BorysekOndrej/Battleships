package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class TimerPanel implements IRenderable {
    private int timeLeft;
    private final Texture panel;
    private final BitmapFont font;
    private final int xPos;
    private final int yPos;
    private boolean run;
    private boolean pause;
    private float currentPeriod;

    public TimerPanel(int x, int y){
        xPos = x;
        yPos = y;
        timeLeft = 0;
        run = false;
        pause = false;
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(6);
        panel = new Texture("timer_panel/timer_panel.png");
    }

    public void setPause(boolean isPaused){
        this.pause = isPaused;
    }

    public void startTimer(int period){
        timeLeft = period;
        run = true;
        font.setColor(Color.BLACK);
    }

    @Override
    public void handleInput() {
        /* Debug
        if(Gdx.input.justTouched()){
            setPause(!pause);
        }

         */
    }

    @Override
    public void update(float dt) {
        if(run){
            if(!pause) currentPeriod += dt;
            if(currentPeriod >= 1f ){
                if(timeLeft==1){
                    run = false;
                    System.out.print("FINISHED\n");
                }
                else{
                    timeLeft--;
                    if(timeLeft<11) font.setColor(Color.RED);
                    //System.out.print(String.format("%02d:%02d\n", timeLeft/60, timeLeft%60));//can be simplified to timeLeft in seconds
                    currentPeriod =0;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(panel, xPos, yPos);
        font.draw(sb, String.format("%02d:%02d\n", timeLeft/60, timeLeft%60), xPos + 20, yPos + 115);
    }

    @Override
    public void dispose() {
        font.dispose();
        panel.dispose();
    }

    public boolean runOut() {
        return !run;
    }
}
