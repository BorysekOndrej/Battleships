package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Timer;

public class TimerPanel implements IRenderable {
    private Timer timer;
    //private FreeTypeFontGenerator generator;
    private BitmapFont font;
    private int xCord;
    private int yCord;

    public TimerPanel(int x, int y){
        xCord = x;
        yCord = y;
        timer = new Timer();
        /*
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon-Regular.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 24;
        params.color = Color.BLACK;
        font = generator.generateFont(params);

         */
    }


    public void place(float x, float y, float width, float height) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void setData(Timer timer) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void handleInput() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void update(float dt) {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        font.draw(sb, "Test", xCord, yCord);
        sb.end();
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    public boolean runOut() {throw new UnsupportedOperationException("not implemented");}
}
