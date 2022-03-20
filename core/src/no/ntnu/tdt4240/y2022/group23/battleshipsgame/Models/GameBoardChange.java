package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

public class GameBoardChange {
    public final Coords coords;
    public final GameBoardField newField;

    GameBoardChange(Coords coords, GameBoardField newVal) {
        this.coords = coords;
        this.newField = newVal;
    }
}
