package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.IRenderable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Network.CommunicationTerminated;

public interface IState{

    void handleInput() throws CommunicationTerminated;

    void update(float dt) throws CommunicationTerminated;

    void render(SpriteBatch sb);

    void dispose();
}
