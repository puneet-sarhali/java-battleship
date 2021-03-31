package shipPackage;

import java.util.List;

public class Battleship extends Ship {
    private final int battleshipLength = 4;

    public Battleship(Direction direction, String name, ShipCondition condition, int amount, List<ShipParts> components) {
        super(direction, name, condition, amount, components);
        this.length = this.battleshipLength;
    }
}
