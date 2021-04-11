package match;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseEnemyShipList {

    EnemyDestroyerLocation destroyer;
    EnemySubmarineLocation submarine;
    EnemyCruiserLocation cruiser;
    EnemyBattleshipLocation battleship;
    EnemyCarrierLocation carrier;

    public FirebaseEnemyShipList() {
    }

    public FirebaseEnemyShipList(EnemyDestroyerLocation destroyer, EnemySubmarineLocation submarine,
                                 EnemyCruiserLocation cruiser, EnemyBattleshipLocation battleship,
                                 EnemyCarrierLocation carrier){
        this.destroyer = destroyer;
        this.submarine = submarine;
        this.cruiser = cruiser;
        this.battleship = battleship;
        this.carrier = carrier;
    }

    // set your ship list, so that your enemy can receive your ship location data in the database
    public void setMyShipList(){
        String userShipList = (FirebaseGame.isHost) ? "hostShipList" : "playerShipList";
        int row;
        int column;
        String location;
        for (int i = 0; i < 1; i++){
            row = destroyer.location[i][0];
            column = destroyer.location[i][1];
            location = row + "_" + column;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).
                    child(userShipList).child(location).setValue(true);
        }
        for (int i = 0; i < 2; i++){
            row = submarine.location[i][0];
            column = submarine.location[i][1];
            location = row + "_" + column;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).
                    child(userShipList).child(location).setValue(true);
        }
        for (int i = 0; i < 3; i++){
            row = cruiser.location[i][0];
            column = cruiser.location[i][1];
            location = row + "_" + column;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).
                    child(userShipList).child(location).setValue(true);
        }
        for (int i = 0; i < 4; i++){
            row = battleship.location[i][0];
            column = battleship.location[i][1];
            location = row + "_" + column;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).
                    child(userShipList).child(location).setValue(true);
        }
        for (int i = 0; i < 5; i++){
            row = cruiser.location[i][0];
            column = cruiser.location[i][1];
            location = row + "_" + column;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).
                    child(userShipList).child(location).setValue(true);
        }
    }
}
