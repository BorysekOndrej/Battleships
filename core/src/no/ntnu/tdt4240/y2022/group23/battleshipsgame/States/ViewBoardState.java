package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.GUIComponents.TimerPanel;

import java.util.List;

public class ViewBoardState extends AbstractState {
    protected GameBoard gameBoard;
    protected List<IShip> ships;
    protected TimerPanel timer;
}
