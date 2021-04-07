package shipPackage;

import java.util.List;

public class Cruiser extends Ship{
    private final int cruiserLength = 3;

    public Cruiser(Direction direction, String name, ShipCondition condition, int amount, List<ShipParts> components) {
        super(direction, name, condition, amount, components);
        this.length = this.cruiserLength;
    }
}
