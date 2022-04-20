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

    private RectangularShip(List<Coords> coords) { super(coords); }

    @Override
    public IShip copy() {
        return new RectangularShip(this.getPositions());
    }

    @Override
    public void displace() {
        int size = this.getPositions().size();
        List<Coords> newPositions = new ArrayList<Coords>();
        Coords start = new Coords(0,0);
        for(int i=0;i<size;i++){
            newPositions.add(new Coords(start.x+i, start.y));
        }
        this.setPositions(newPositions);
    }

    @Override
    public void placeShip(Coords start){
        List<Coords> positions = new ArrayList<Coords>();
        for(int i=0;i<this.getType();i++){
            positions.add(new Coords(
                    this.getOrientation() ? start.x+i : start.x,
                    this.getOrientation() ? start.y : start.y+i
            ));
        }
        this.setPositions(positions);
    }
}
