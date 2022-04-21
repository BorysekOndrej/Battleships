package no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.States.IGameBoardState;

public class GameBoardObserver {
    private final IGameBoardState gameBoardState;

    public GameBoardObserver(IGameBoardState gameBoardState) {
        this.gameBoardState = gameBoardState;
    }

    public void notice(Coords coords) {
        gameBoardState.gameBoardTouch(coords);
    }
}
