package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.ISerialializable;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;

public interface IAction extends ISerialializable {

    public GameBoard apply(GameBoard gameBoard);
}
