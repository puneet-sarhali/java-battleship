package shipPackage;

public class ShipParts {
    ShipPartsCondition condition;
    Ship ship;

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
