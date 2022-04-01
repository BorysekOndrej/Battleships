package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;

public class RectangularShip extends AbstractShip {
    public RectangularShip(Coords start, int numberOfSquares, boolean horizontal){
        this.setType(numberOfSquares);
        this.setOrientation(horizontal);
        List<Coords> positions = new ArrayList<Coords>();
        for(int i=0;i<numberOfSquares;i++){
            positions.add(new Coords(
                    horizontal ? start.x+i : start.x,
                    horizontal ? start.y : start.y+i
            ));
        }
        this.setPositions(positions);
    }

    public RectangularShip(RectangularShip shipToCopy){
        this.setType(shipToCopy.getType());
        this.setOrientation(shipToCopy.getOrientation());
        Coords start = shipToCopy.getPositions().get(0);
        List<Coords> positions = new ArrayList<Coords>();
        for(int i=0;i<shipToCopy.getPositions().size();i++){
            positions.add(new Coords(
                    shipToCopy.getOrientation() ? start.x+i : start.x,
                    shipToCopy.getOrientation() ? start.y : start.y+i
            ));
        }
        this.setPositions(positions);
    }
}
