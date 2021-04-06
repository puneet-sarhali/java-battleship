package shipPackage;

import java.util.List;

public class Destroyer extends Ship{
    private final int destroyerLength = 1;

    public Destroyer(String name, ShipCondition condition, int shipImageID) {
        super(name, condition, shipImageID);
        this.length = this.destroyerLength;
    }
}
