package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import java.io.Serializable;

public class GameBoardChange implements Serializable {
    private static final long serialVersionUID = 80085;

    public final Coords coords;
    public final GameBoardField newField;

    public GameBoardChange(Coords coords, GameBoardField newVal) {
        this.coords = coords;
        this.newField = newVal;
    }

    @Override
    public String toString() {
        return "GameBoardChange(coords=" + coords.toString() + ", newVal=" + newField.toString() + ")";
    }
}
