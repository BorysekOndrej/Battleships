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

    public Radar(Coords coords){
        this.setRadius(1);
        this.setCoords(coords);
    }

    public Radar(Coords coords, int radius) {
        this.setRadius(radius);
        this.setCoords(coords);
    }

    @Override
    public List<GameBoardChange> affect(ShipPlacements ships, GameBoard gameBoard) {
        Coords affectedPoint = this.getCoords();
        List<GameBoardChange> changes= new ArrayList<GameBoardChange>();

        for(int delta_x = (Math.max(affectedPoint.x - radius, 0)); delta_x<=(Math.min(affectedPoint.x + radius, gameBoard.getWidth() - 1)); delta_x++){
            Coords newCoords = new Coords(delta_x, this.getCoords().y);
            if(ships.hasShipOnCoords(newCoords) && (gameBoard.get(newCoords)!= GameBoardField.HIT || gameBoard.get(newCoords)!= GameBoardField.SUNK) && !(newCoords.x==this.getCoords().x && newCoords.y==this.getCoords().y)){
                changes.add(new GameBoardChange(newCoords, GameBoardField.SHIP));
            }
            else if(gameBoard.get(newCoords)== GameBoardField.UNKNOWN && newCoords!=this.getCoords() && !(newCoords.x==this.getCoords().x && newCoords.y==this.getCoords().y)){
                changes.add(new GameBoardChange(newCoords, GameBoardField.WATER));
            }
        }
        for(int delta_y = (Math.max(affectedPoint.y - radius, 0)); delta_y<=(Math.min(affectedPoint.y + radius, gameBoard.getHeight() - 1)); delta_y++){
            Coords newCoords = new Coords(this.getCoords().x, delta_y);
            if(ships.hasShipOnCoords(newCoords) && (gameBoard.get(newCoords)!= GameBoardField.HIT || gameBoard.get(newCoords)!= GameBoardField.SUNK)){
                changes.add(new GameBoardChange(newCoords, GameBoardField.SHIP));
            }
            else if(gameBoard.get(newCoords)== GameBoardField.UNKNOWN){
                changes.add(new GameBoardChange(newCoords, GameBoardField.WATER));
            }
        }

        /*
        for(int delta_x = (Math.max(affectedPoint.x - radius, 0)); delta_x<=(Math.min(affectedPoint.x + radius, gameBoard.getWidth() - 1)); delta_x++){
            for(int delta_y = (Math.max(affectedPoint.y - radius, 0)); delta_y<=(Math.min(affectedPoint.y + radius, gameBoard.getHeight() - 1)); delta_y++){
                Coords newCoords = new Coords(delta_x, delta_y);
                if(ships.hasShipOnCoords(newCoords) && (gameBoard.get(newCoords)!= GameBoardField.HIT || gameBoard.get(newCoords)!= GameBoardField.SUNK)){
                    changes.add(new GameBoardChange(newCoords, GameBoardField.SHIP));
                }
                else if(gameBoard.get(newCoords)== GameBoardField.UNKNOWN){
                    changes.add(new GameBoardChange(newCoords, GameBoardField.WATER));
                }
            }
        }*/
        return changes;
    }

    public int getRadius(){
        return radius;
    }

    public void setRadius(int newRadius){
        this.radius = newRadius;
    }
}
