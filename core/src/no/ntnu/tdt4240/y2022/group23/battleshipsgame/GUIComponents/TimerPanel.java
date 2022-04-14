package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;

public class TimerPanel implements IRenderable {
    private int timeLeft;
    private Texture panel;
    private BitmapFont font;
    private int xCord;
    private int yCord;
    private boolean run;
    private long start;

    public TimerPanel(int x, int y){
        xCord = x;
        yCord = y;
        timeLeft = 0;
        run = false;
        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(6);
        panel = new Texture("timer_panel/timer_panel.png");
    }

    public void startTimer(int period){
        timeLeft = period;
        run = true;
        font.setColor(Color.BLACK);
    }

    @Override
    public void handleInput() {}

    @Override
    public void update(float dt) {
        if(run){
            long finish = System.nanoTime();
            if((finish - start) >= 1000000000 ){
                if(timeLeft==1){
                    run = false;
                    System.out.print("FINISHED\n");
                }
                else{
                    timeLeft--;
                    if(timeLeft<11) font.setColor(Color.RED);
                    //System.out.print(String.format("%02d:%02d\n", timeLeft/60, timeLeft%60));//can be simplified to timeLeft in seconds
                    start = finish;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.draw(panel, xCord, yCord);
        font.draw(sb, String.format("%02d:%02d\n", timeLeft/60, timeLeft%60), xCord + 20, yCord + 95);
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
