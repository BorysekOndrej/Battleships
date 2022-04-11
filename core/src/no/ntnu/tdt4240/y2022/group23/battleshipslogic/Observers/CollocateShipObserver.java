package no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.States.ShipPlacementState;

public class CollocateShipObserver implements IBattleshipObserver {
    private final ShipPlacementState shipPlacementState;

    public CollocateShipObserver(ShipPlacementState shipPlacementState) {
        this.shipPlacementState = shipPlacementState;
    }

    @Override
    public void notice() {
        shipPlacementState.collocateShip();
    }
}
