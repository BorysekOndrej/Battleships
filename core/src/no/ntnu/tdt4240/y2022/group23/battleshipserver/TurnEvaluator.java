package no.ntnu.tdt4240.y2022.group23.battleshipserver;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.IAction;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.Radar;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions.SingleShot;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Lobby;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Timer;

public class TurnEvaluator {
    public abstract TurnEvaluator (GameBoard gameBoard, IAction action);

    public boolean isValidTurn();
    public GameBoard boardBeforeTurn();
    public GameBoard boardAfterTurn();

    private tmp(){
        // various msgs that can be transmited over the network
        GameBoard gameBoard;
        Lobby lobby;
        Timer timer;
        Radar radar;
        SingleShot singleShot;
    }

}
