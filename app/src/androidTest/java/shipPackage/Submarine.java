package shipPackage;

import java.util.List;

public class Submarine extends Ship{
    private final int submarineLength = 2;

    public Submarine(Direction direction, String name, ShipCondition condition, int amount, List<ShipParts> components) {
        super(direction, name, condition, amount, components);
        this.length = this.submarineLength;
    }
}
