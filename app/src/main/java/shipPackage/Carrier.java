package shipPackage;

import java.util.List;

public class Carrier extends Ship{
    private final int carrierLength = 5;

    public Carrier(String name, ShipCondition condition, int shipImageID) {
        super(name, condition, shipImageID);
        this.length = this.carrierLength;
    }
}
