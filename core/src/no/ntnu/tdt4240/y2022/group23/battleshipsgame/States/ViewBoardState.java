package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Timer;

import java.util.List;

public class ViewBoardState extends AbstractState {
    protected GameBoard gameBoard;
    protected List<IShip> ships;
    protected Timer timer;
}
