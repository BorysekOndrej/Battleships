package no.ntnu.tdt4240.y2022.group23.battleshipsgame.States;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;

//Represents all states that have a game board
public interface IGameBoardState extends  IState{

    //Notices the observer about the touching of the game board, giving the coords
    void gameBoardTouch(Coords coords);
}
