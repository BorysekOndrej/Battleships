package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;

import java.util.List;


public class ViewMineBoardState extends ViewBoardState {
    List<IShip> ships;

    protected ViewMineBoardState(GameStateManager gsm) {
        super(gsm);
    }
}
