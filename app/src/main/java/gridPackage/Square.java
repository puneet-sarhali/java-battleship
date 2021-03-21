package gridPackage;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

import shipPackage.ShipParts;

public class Square {
    SquareCondition condition;
    ShipParts shipPart;

    private SquareCondition getCondition() {
        return condition;
    }

    private ShipParts getShipPart() {
        return shipPart;
    }

    private void setCondition(SquareCondition condition) {
        this.condition = condition;
    }

    private void setShipPart(ShipParts shipPart) {
        this.shipPart = shipPart;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Boolean hasShipPart(){
        return Objects.nonNull(this.shipPart);
    }
}
