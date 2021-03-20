package shipPackage;

import java.util.List;

public class Ship {
    Direction direction;
    int length;
    String name;
    ShipCondition condition;
    int amount;
    List<ShipParts> components;

    private Direction getDirection() {
        return direction;
    }

    private int getLength() {
        return length;
    }

    private String getName() {
        return name;
    }

    private ShipCondition getCondition() {
        return condition;
    }

    private int getAmount() {
        return amount;
    }

    private void setDirection(Direction direction) {
        this.direction = direction;
    }

    private void setLength(int length) {
        this.length = length;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setCondition(ShipCondition condition) {
        this.condition = condition;
    }

    private void setAmount(int amount) {
        this.amount = amount;
    }

}
