package no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.States.ShipPlacementState;

public class CollocateShipObserver {
    private final ShipPlacementState shipPlacementState;

    public CollocateShipObserver(ShipPlacementState shipPlacementState) {
        this.shipPlacementState = shipPlacementState;
    }

    public void notice(Coords coords) {
        shipPlacementState.collocateShip(coords);
    }
}
