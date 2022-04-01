package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;

public abstract class AbstractShip implements IShip {
    private List<Coords> positions;
    private int parts;

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
        return parts;
    }

    public void setType(int parts){
        this.parts = parts;
    }

    @Override
    public void displace() {
        throw new UnsupportedOperationException("not implemented");
    }

    @Override
    public void rotateClockwise() {
        throw new UnsupportedOperationException("not implemented");
    }
}
