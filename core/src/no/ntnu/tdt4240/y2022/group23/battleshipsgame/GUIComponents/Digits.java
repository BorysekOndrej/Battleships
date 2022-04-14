package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Digits {
    final static public int NUMBER_OF_DIGITS = 10;
    private TextureRegion font;
    private TextureRegion digits[];

    public Digits(){
        font = new TextureRegion(new Texture("fonts/myfont_white"));
        for(int i =0; i < NUMBER_OF_DIGITS; i++){
            digits[i] = new TextureRegion(font, i * font.getRegionWidth() / NUMBER_OF_DIGITS, 0, font.getRegionWidth() / NUMBER_OF_DIGITS, font.getRegionHeight());
        }
    }
}
