package system;

import java.util.ArrayList;

import shipPackage.Ship;

public class ShipList {
    int size;
    ArrayList<Ship> shipList;

    private void ShipList(){
        shipList = new ArrayList<>();
    }

    private int getSize() {
        return size;
    }

    private void addShip(Ship s){
        this.shipList.add(s);
    }

    private void deleteShip(int i){
        this.shipList.remove(i);
    }

    private Ship getShip(int i){
        return this.shipList.get(i);
    }
}
