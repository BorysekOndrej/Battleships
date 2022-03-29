package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Actions;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoard;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardChange;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.GameBoardField;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.ShipPlacements;

public class SingleShot extends AbstractAction {

    public SingleShot(Coords coords) {
        this.setCoords(coords);
    }

    @Override
    public List<GameBoardChange> affect(ShipPlacements ships, GameBoard gameBoard) {
        Coords coords = this.getCoords();
        List<GameBoardChange> changes= new ArrayList<GameBoardChange>();
        if(ships.hasShipOnCoords(coords) && (gameBoard.get(coords)!= GameBoardField.HIT || gameBoard.get(coords)!= GameBoardField.SUNK)){
            changes.add(new GameBoardChange(coords, GameBoardField.HIT));
        }
        else{
            changes.add(new GameBoardChange(coords, GameBoardField.WATER));
        }
        return changes;
    }
}
