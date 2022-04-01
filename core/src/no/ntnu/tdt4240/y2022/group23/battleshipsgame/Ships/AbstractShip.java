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

    public boolean getOrientation(){
        return horizontal;
    }

    public void setOrientation(boolean horizontal){
        this.horizontal = horizontal;
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
    public void rotateClockwise() {
        int size = this.getPositions().size();
        List<Coords> newPositions = new ArrayList<Coords>();
        Coords start = this.getPositions().get(0);
        Coords second = this.getPositions().get(1);
        boolean horizontal = this.getOrientation();
        boolean checkDirection = Math.min(second.x- start.x, second.y- start.y)<0;
        for(int i=0;i<size;i++){
            if(checkDirection) {
                newPositions.add(new Coords(
                        !horizontal ? start.x - i : start.x,
                        !horizontal ? start.y : start.y + i
                ));
            }
            else{
                newPositions.add(new Coords(
                        !horizontal ? start.x + i : start.x,
                        !horizontal ? start.y : start.y - i
                ));
            }
        }
        this.setPositions(newPositions);
    }
}
