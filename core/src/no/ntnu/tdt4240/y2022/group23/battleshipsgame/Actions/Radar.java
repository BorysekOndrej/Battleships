package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardChange;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;

public class Radar extends AbstractAction {

    private int radius;

    public Radar(Coords coords, int radius) {
        this.setRadius(radius);
        this.setCoords(coords);
    }

    @Override
    public List<GameBoardChange> affect(ShipPlacements ships, GameBoard gameBoard) {
        Coords affectedPoint = this.getCoords();
        List<GameBoardChange> changes= new ArrayList<GameBoardChange>();
        for(int delta_x=(affectedPoint.x-radius);delta_x<=(affectedPoint.x+radius);delta_x++){
            for(int delta_y=(affectedPoint.y-radius);delta_y<=(affectedPoint.y+radius);delta_y++){
                Coords newCoords = new Coords(delta_x, delta_y);
                if(ships.hasShipOnCoords(newCoords) && (gameBoard.get(newCoords)!= GameBoardField.HIT || gameBoard.get(newCoords)!= GameBoardField.SUNK)){
                    changes.add(new GameBoardChange(newCoords, GameBoardField.SHIP));
                }
                else{
                    changes.add(new GameBoardChange(newCoords, GameBoardField.WATER));
                }
            }
        }
        return changes;
    }

    public int getRadius(){
        return radius;
    }

    public void setRadius(int newRadius){
        this.radius = newRadius;
    }
}
