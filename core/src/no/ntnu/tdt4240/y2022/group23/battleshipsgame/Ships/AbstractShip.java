package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;

public abstract class AbstractShip implements IShip {
    private List<Coords> positions;
    private int parts;
    private boolean horizontal;

    public AbstractShip() {}

    @Override
    public List<Coords> getPositions() {
        return positions;
    }

    @Override
    public void setPositions(List<Coords> positions){
        this.positions = positions;
    }

    @Override
    public boolean isSunk(GameBoard board) {
        for (Coords coords:positions) {
            if (!(board.get(coords) == GameBoardField.HIT || board.get(coords) == GameBoardField.SUNK)) {
                return false;
            }
        }
        return true;
    }

    public int getType(){
        return this.parts;
    }

    public void setType(int parts){
        this.parts = parts;
    }

    public boolean getOrientation(){
        return horizontal;
    }

    public void setOrientation(boolean horizontal){
        this.horizontal = horizontal;
    }

    @Override
    public void rotateClockwise() {
        int size = this.getPositions().size();
        List<Coords> newPositions = new ArrayList<Coords>();
        Coords start = this.getPositions().get(0);
        for (int i = 0; i < size; i++) {
            newPositions.add(new Coords(-(this.getPositions().get(i).y - start.y) + start.x, this.getPositions().get(i).x - start.x + start.y));
        }
        this.setPositions(newPositions);
        this.setOrientation(!getOrientation());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof IShip))
            return false;

        IShip ship1 = ((IShip) obj).copy();
        IShip ship2 = this.copy();

        ship1.displace();
        ship2.displace();

        return ship1.getPositions().containsAll(ship2.getPositions()) && ship2.getPositions().containsAll(ship1.getPositions());
    }
}
