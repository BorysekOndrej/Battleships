package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;

public class RectangularShip extends AbstractShip {
    public RectangularShip(Coords start, int numberOfSquares, boolean horizontal){
        this.setType(numberOfSquares);
        List<Coords> positions = new ArrayList<Coords>();
        if(horizontal){
            positions.add(start);
            for(int i=1;i<numberOfSquares;i++){
                positions.add(new Coords((start.x+i), start.y));
            }
        }
        else{
            positions.add(start);
            for(int i=1;i<numberOfSquares;i++){
                positions.add(new Coords(start.x, (start.y+i)));
            }
        }
        this.setPositions(positions);
    }
}
