package no.ntnu.tdt4240.y2022.group23.battleshipsgame.Ships;

import com.badlogic.gdx.math.Vector2;
import no.ntnu.tdt4240.y2022.group23.battleshipsgame.ISerialializable;

import java.util.List;

public interface IShip extends ISerialializable {
    public List<Vector2> getPositions();
}
