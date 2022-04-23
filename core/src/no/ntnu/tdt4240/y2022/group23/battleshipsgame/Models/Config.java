package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.Radar;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.SingleShot;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

public class Config {
    public static final int GAME_BOARD_WIDTH = 10;
    public static final int GAME_BOARD_HEIGHT = 10;

    public static final int SHIP_PLACEMENT_TIMEOUT = 120;
    public static final int TURN_TIMEOUT = 30;

    public static final int RADAR_COOLDOWN = 5;

    public static List<Pair<IShip, Integer>> remainingShips() {
        List<Pair<IShip, Integer>> remainingShips = new ArrayList<>();
        for (int i = 4; i >= 1; i--) {
            remainingShips.add(Pair.with(new RectangularShip(new Coords(0, 0), i, false), 5 - i));
        }
        return remainingShips;
    }

    public static List<Pair<IAction, Boolean>> actions() {
        List<Pair<IAction, Boolean>> actions = new ArrayList<>();
        Coords coords = new Coords(0, 0);
        actions.add(Pair.with(new SingleShot(coords), true));
        actions.add(Pair.with(new Radar(coords),true));
        actions.add(Pair.with(new SingleShot(coords), false));
        actions.add(Pair.with(new SingleShot(coords), false));

        return actions;
    }
}
