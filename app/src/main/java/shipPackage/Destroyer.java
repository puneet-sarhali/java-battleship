package shipPackage;

import java.util.List;

public class Destroyer extends Ship{
    private final int destroyerLength = 1;

    public Destroyer(Direction direction, String name, ShipCondition condition, int amount, List<ShipParts> components) {
        super(direction, name, condition, amount, components);
        this.length = this.destroyerLength;
    }
}
