package shipPackage;

import java.util.List;

public class Submarine extends Ship{
    private final int submarineLength = 2;

    public Submarine(String name, ShipCondition condition, int shipImageID) {
        super(name, condition, shipImageID);
        this.length = this.submarineLength;
    }
}
