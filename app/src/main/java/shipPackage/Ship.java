package shipPackage;

import android.widget.ImageView;

import java.util.List;

public class Ship {
    int shipImageID;
    Direction direction;
    int length;
    String name;
    ShipCondition condition;
    int amount;
    List<ShipParts> components;

    public Ship(String name, ShipCondition condition, int shipImageID) {
        this.direction = direction;
        //this.length = length;
        this.name = name;
        this.condition = condition;
        this.amount = amount;
        this.components = components;
        this.shipImageID = shipImageID;
    }

    public void setShipImageID(int id){
        shipImageID = id;
    }
    public int getShipImageID(){
        return shipImageID;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public ShipCondition getCondition() {
        return condition;
    }

    public int getAmount() {
        return amount;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCondition(ShipCondition condition) {
        this.condition = condition;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
