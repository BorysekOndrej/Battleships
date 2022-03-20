package no.ntnu.tdt4240.y2022.group23.battleshipserver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Models.Coords;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.IShip;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships.RectangularShip;

public class TestTest {
    @Test
    public void justAnExample() {
        System.out.println("This test method should be run");
    }

    @Test
    public void fails() {
        Assertions.fail();
    }

    @Test
    public void importTest() {
        IShip ship = new RectangularShip(1, 2, true,3);
        List<Coords> positions = ship.getPositions();

        Assertions.assertEquals(positions.size(), 3);
    }

    @ExtendWith(MockitoExtension.class)
    public class UserServiceUnitTest {
        IShip ship = new RectangularShip(1, 2, true, 3);
    }
}
