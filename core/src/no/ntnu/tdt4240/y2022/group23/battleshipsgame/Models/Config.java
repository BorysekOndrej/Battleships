package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

public class Config {
    public static final int GAME_BOARD_WIDTH = 10;
    public static final int GAME_BOARD_HEIGHT = 10;

    public static List<Pair<IShip, Integer>> remainingShips() {
        List<Pair<IShip, Integer>> remainingShips = new ArrayList<>();
        for (int i = 4; i >= 1; i--) {
            remainingShips.add(Pair.with(new RectangularShip(new Coords(0, 0), i, false), i == 1 ? 1 : 0));
        }
        return remainingShips;
    }
}
