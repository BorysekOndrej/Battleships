package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;

public abstract class AbstractAction implements IAction {
    private Coords coords;

    @Override
    public Coords getCoords(){
        return coords;
    }

    @Override
    public void setCoords(Coords newCoords){
        this.coords = newCoords;
    }
}
