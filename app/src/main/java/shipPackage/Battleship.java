package shipPackage;

import java.util.List;

public class Battleship extends Ship{
    private final int battleshipLength = 4;



    public Battleship(String name, ShipCondition condition, int shipImageID) {
        super(name, condition, shipImageID);
        this.length = this.battleshipLength;
    }
}
