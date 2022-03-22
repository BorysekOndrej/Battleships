package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.ISerializable;

public enum NextTurn implements ISerializable {
    MY_TURN,
    OTHERS_TURN,
    GAME_OVER
}
