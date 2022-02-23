package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.ISerialializable;

import java.util.List;

public class GameBoard implements ISerialializable {
    private List<List<SingleRectangle>> board;
}
