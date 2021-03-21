package shipPackage;

import java.util.List;

public class Carrier extends Ship{
    private final int carrierLength = 5;

    public Carrier(Direction direction, String name, ShipCondition condition, int amount, List<ShipParts> components) {
        super(direction, name, condition, amount, components);
        this.length = this.carrierLength;
    }
}
