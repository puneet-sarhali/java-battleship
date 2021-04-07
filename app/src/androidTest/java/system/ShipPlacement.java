package system;

import gridPackage.Grid;
import gridPackage.Square;
import gridPackage.SquareCondition;
import shipPackage.Ship;

public class ShipPlacement {
    ShipList shipList;
    Grid grid;
    ShipList gridShipList;

    private ShipPlacement(ShipList shipList, Grid grid, ShipList gridShipList) {
        this.shipList = shipList;
        this.grid = grid;
        this.gridShipList = gridShipList;
    }

    private ShipList getShipList() {
        return shipList;
    }

    private Grid getGrid() {
        return grid;
    }

    private Boolean isEmpty(int r, int c){
        Square sq = grid.getSquare(r,c);
        return sq.getCondition().equals("EMPTY");
    }

    private Boolean isOccupied(int r, int c){
        Square sq = grid.getSquare(r,c);
        return sq.getCondition().equals("OCCUPIED");
    }

    private Boolean isProhibited(int r, int c){
        Square sq = grid.getSquare(r,c);
        return sq.getCondition().equals("PROHIBITED");
    }

    private void placeShip(Ship s, int r, int c){
        if(this.grid.getSquare(r,c).getCondition().equals("OCCUPIED")||
                this.grid.getSquare(r,c).getCondition().equals("PROHIBITED")){
            //square not available
        }
        else {
            this.grid.getSquare(r, c).setCondition(SquareCondition.valueOf("OCCUPIED"));
        }
    }
}
