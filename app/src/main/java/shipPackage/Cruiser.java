package shipPackage;

import java.util.List;

public class Cruiser extends Ship{
    private final int cruiserLength = 3;

    public Cruiser(String name, ShipCondition condition, int shipImageID) {
        super(name, condition, shipImageID);
        this.length = this.cruiserLength;
    }
}
