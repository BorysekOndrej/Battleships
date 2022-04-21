package no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.BattleshipsGame;

public class MenuStateGUI implements IRenderable {
    final static public int BUTTON_WIDTH = 409;
    final static public int BUTTON_HEIGHT = 170;

    private final Texture background;
    private final SimpleButton createLobbyButton;
    private final SimpleButton joinLobbyButton;
    private final SimpleButton practiseButton;
    private final SimpleButton rankedButton;

    private final BitmapFont font;
    private String rankingScore = "";

    public MenuStateGUI(){
        background = new Texture("main_menu/main_menu_background.png");

        createLobbyButton = new SimpleButton(BattleshipsGame.WIDTH/2 - BUTTON_WIDTH/2, BattleshipsGame.HEIGHT/2 - BUTTON_HEIGHT/2, new Texture("main_menu/create_lobby.png"));

        joinLobbyButton = new SimpleButton(BattleshipsGame.WIDTH/2 - BUTTON_WIDTH/2, BattleshipsGame.HEIGHT/2 - BUTTON_HEIGHT*2, new Texture("main_menu/join_lobby.png"));

        practiseButton = new SimpleButton(BattleshipsGame.WIDTH/2 - BUTTON_WIDTH/2, BattleshipsGame.HEIGHT/2 - BUTTON_HEIGHT*7/2, new Texture("main_menu/practise.png"));

        rankedButton = new SimpleButton(BattleshipsGame.WIDTH/2 - BUTTON_WIDTH/2, BattleshipsGame.HEIGHT/2 - BUTTON_HEIGHT*5, new Texture("main_menu/ranked.png"));

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(4);
    }

    public void setRankingScore(String rankingScore) {
        this.rankingScore = rankingScore;
    }

    public boolean createLobbyButtonPressed(){
        return createLobbyButton.buttonTouched();
    }

    public boolean joinLobbyButtonPressed(){
        return joinLobbyButton.buttonTouched();
    }

    public boolean practiseButtonPressed(){
        return practiseButton.buttonTouched();
    }

    public boolean rankedButtonPressed(){
        return rankedButton.buttonTouched();
    }


    @Override
    public void handleInput() {}

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(background, 0,0, BattleshipsGame.WIDTH, BattleshipsGame.HEIGHT);
        createLobbyButton.render(sb);
        joinLobbyButton.render(sb);
        practiseButton.render(sb);
        rankedButton.render(sb);
        font.draw(sb, String.format("Your current ranking score: %s", rankingScore), 63, BattleshipsGame.HEIGHT - 63);
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        joinLobbyButton.dispose();
        createLobbyButton.dispose();
        practiseButton.dispose();
        rankedButton.dispose();
        font.dispose();
    }
}

