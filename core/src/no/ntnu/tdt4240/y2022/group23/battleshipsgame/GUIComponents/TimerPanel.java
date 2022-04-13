package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Timer;

public class TimerPanel implements IRenderable {
    private int timeLeft;
    //private FreeTypeFontGenerator generator;
    //private BitmapFont font;
    private int xCord;
    private int yCord;
    private boolean run;
    private long start;

    public TimerPanel(int x, int y){
        xCord = x;
        yCord = y;
        timeLeft = 0;
        run = false;

        //generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon-Regular.otf"));
    }
    /*
    private void initFonts(){
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 24;
        params.color = Color.BLACK;
        font = generator.generateFont(params);
    }
    */

    public void startTimer(int period){
        timeLeft = period;
        run = true;
        long start = System.nanoTime();
    }

    public void place(float x, float y, float width, float height) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void handleInput() {
        throw new UnsupportedOperationException("not implemented");
    }

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
                    System.out.print(String.format("%02d:%02d\n", timeLeft/60, timeLeft%60));//can be simplified to timeLeft in seconds
                    start = finish;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {
        //font.dispose();
    }

    public boolean runOut() {
        return !run;
    }
}
