package shipPackage;

public class ShipParts {
    ShipPartsCondition condition;
    Ship ship;

    public ShipParts(ShipPartsCondition condition, Ship ship) {
        this.condition = condition;
        this.ship = ship;
    }

    private ShipPartsCondition getCondition() {
        return condition;
    }

    private Ship getShip() {
        return ship;
    }

    private void setCondition(ShipPartsCondition condition) {
        this.condition = condition;
    }

    private void setShip(Ship ship) {
        this.ship = ship;
    }
}
