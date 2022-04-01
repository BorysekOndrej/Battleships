package no.ntnu.tdt4240.y2022.group23.battleshipslogic.Observers;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.States.ShipPlacementState;

public class SelectedShipObserver implements IBattleshipObserver {
    private final ShipPlacementState shipPlacementState;

    public SelectedShipObserver(ShipPlacementState shipPlacementState) {
        this.shipPlacementState = shipPlacementState;
    }

    @Override
    public void notice() {
        shipPlacementState.selectShip();
    }
}
