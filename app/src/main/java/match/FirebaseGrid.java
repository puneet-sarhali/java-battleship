package match;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// the class stores all the grid info for both players
public class FirebaseGrid {
    // the callback interface that will be called when finishing setting the data in the database
    public interface OnSuccessSettingCallBack {
        // upon successfully setting the data
        void onSuccess(int number, int row, int column);
    }

    // the callback interface that will be called when finishing reading the data in the database
    public interface OnSuccessReadingCallBack {
        // upon successfully reading the data
        void onSuccess(int row, int column);
    }

    /**A database reference to the user grid and enemy's grid
    *  0 means bomb shreds, 1 means destroyer
    *  2 means submarine, 3 means cruiser
    *  4 means battleship, 5 means carrier, 6 means water
    * -1 means damaged destroyer, -2 means damaged submarine
    * -3 means damaged cruiser, -4 means damaged battleship
    */
    static public int[][] hostGrid = new int[8][8];
    static public int[][] playerGrid = new int[8][8];
    static public int[][] currentGrid = new int[8][8];
    static public int[][] opponentGrid = new int[8][8];

    // a child event listener in the database
    static public ChildEventListener readCurrentLocationListener;

    // a static initialization block; initialize all the grid
    static {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                currentGrid[i][j] = 6;
                opponentGrid[i][j] = 6;
                hostGrid[i][j] = 6;
                playerGrid[i][j] = 6;
            }
        }
    }

    // default constructor
    public FirebaseGrid(){
    }

    // modify the current local grid, and set the data to number corresponding to the ships
    static public void setCurrentGrid(int shipSize, int location){
        if (shipSize == 1){
            // if the ship is a destroyer, set it to 1
            FirebaseGrid.currentGrid[location / 8][location % 8] = 1;
        } else if (shipSize == 2){
            // if the ship is a submarine, set it to 2
            FirebaseGrid.currentGrid[location / 8][location % 8] = 2;
        } else if (shipSize == 3){
            // if the ship is a cruiser, set it to 3
            FirebaseGrid.currentGrid[location / 8][location % 8] = 3;
        } else if (shipSize == 4){
            // if the ship is a battleship, set it to 4
            FirebaseGrid.currentGrid[location / 8][location % 8] = 4;
        } else if (shipSize == 5){
            // if the ship is a carrier, set it to 5
            FirebaseGrid.currentGrid[location / 8][location % 8] = 5;
        }
    }

    // initialize the the grid in Firebase database, call this only once
    static public void initializeGridInFirebase(){
        // set the user board to host board if the user is host, else: player board
        String userBoard = (FirebaseGame.isHost) ? "hostBoard" : "playerBoard";
        int value;
        String location;

        // set the player's grid locally and pushed the location into the Firebase database
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (FirebaseGame.isHost){
                    hostGrid[i][j] = currentGrid[i][j];
                } else {
                    playerGrid[i][j] = currentGrid[i][j];
                }
                location = i + "_" + j;
                value = currentGrid[i][j];
                FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(userBoard).child(location).setValue(value);
            }
        }
    }

    // get the opponent's grid and set it locally, run this only once
    static public void getOpponentGrid(OnSuccessReadingCallBack onSuccessReadingCallBack){
        // set the enemy board the player board if you are the host, else: host board
        String enemyBoard = (FirebaseGame.isHost) ? "playerBoard" : "hostBoard";
        // add a event listener on opponent's grid in the Firebase database, and retrieve all their data
        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(enemyBoard).addValueEventListener(new ValueEventListener() {
            @Override
            // run this upon initialization of the event listener
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // find the opponent grid path in the database
                for (DataSnapshot data1 : snapshot.getChildren()){
                    int row = Integer.parseInt(data1.getKey().substring(0, 1));
                    int column = Integer.parseInt(data1.getKey().substring(2, 3));
                    opponentGrid[row][column] = data1.getValue(Integer.class);
                    // if the user is host, set the player grid to the opponent grid
                    if (FirebaseGame.isHost){
                        playerGrid[row][column] = opponentGrid[row][column];
                    }
                    // if the user is not host, set the host grid to the host grid
                    else {
                        hostGrid[row][column] = opponentGrid[row][column];
                    }
                }

                // call the callback interface once finishing reading the data
                onSuccessReadingCallBack.onSuccess(1, 2);
                // remove the event listener when finish reading the data
                FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(enemyBoard).removeEventListener(this);
            }

            @Override
            // if the event listener failed, return an error statement to the developer
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Get opponent's grid failed");
            }
        });
    }

    // a setter that sets the player squares that were hit to true into the database
    static public void setCurrentLocation(int row, int column, OnSuccessSettingCallBack onSuccessSettingCallBack){
        String userBoard = (FirebaseGame.isHost) ? "hostBoard" : "playerBoard";
        String location = row + "_" + column;

        // set the current location only if the ships and water hasn't been bombed yet
        if (currentGrid[row][column] > 0) {
            currentGrid[row][column] = -currentGrid[row][column];
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(userBoard).child(location).setValue(currentGrid[row][column]);
            onSuccessSettingCallBack.onSuccess(currentGrid[row][column], row, column);
        }


        // if the user is host, set the current grid to host grid, else set it to player grid
        if (FirebaseGame.isHost){
            hostGrid[row][column] = currentGrid[row][column];
        } else {
            playerGrid[row][column] = currentGrid[row][column];
        }
    }

    // a setter that sets the enemy's squares that were hit to true into the database
    static public void setOpponentLocation(int row, int column, OnSuccessSettingCallBack onSuccessSettingCallBack, boolean condition){
        String opponentBoard = (FirebaseGame.isHost) ? "playerBoard" : "hostBoard";
        String location = row + "_" + column;

        // if the condition is true from the parameter, then set every grid to 0
        // if the condition is true, set every grid the negative number of the original value
        if (condition){
            opponentGrid[row][column] = 0;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(opponentBoard).child(location).setValue(0);
            onSuccessSettingCallBack.onSuccess(0, row, column);
        } else {
            if (opponentGrid[row][column] > 0) {
                opponentGrid[row][column] = -opponentGrid[row][column];
                FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(opponentBoard).child(location).setValue(opponentGrid[row][column]);
                onSuccessSettingCallBack.onSuccess(opponentGrid[row][column], row, column);
            }
        }

        if (FirebaseGame.isHost){
            playerGrid[row][column] = opponentGrid[row][column];
        } else {
            hostGrid[row][column] = opponentGrid[row][column];
        }
    }

    // read the current player's data
    static public void readCurrentLocation(OnSuccessReadingCallBack onSuccessReadingCallBack){
        String currentUser = (FirebaseGame.isHost) ? "hostBoard" : "playerBoard";

        readCurrentLocationListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int tempRow = Integer.parseInt(snapshot.getKey().substring(0, 1));
                int tempColumn = Integer.parseInt(snapshot.getKey().substring(2, 3));
                int value = snapshot.getValue(Integer.class).intValue();
                currentGrid[tempRow][tempColumn] = value;
                if (FirebaseGame.isHost){
                    hostGrid[tempRow][tempColumn] = currentGrid[tempRow][tempColumn];
                } else {
                    playerGrid[tempRow][tempColumn] = currentGrid[tempRow][tempColumn];
                }
                onSuccessReadingCallBack.onSuccess(tempRow, tempColumn);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("readCurrentLocation Failed");
            }
        };

        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(currentUser).addChildEventListener(readCurrentLocationListener);
    }

    // read the opponent's data
    static public void readOpponentLocation(OnSuccessReadingCallBack onSuccessReadingCallBack){
        String opponentUser = (FirebaseGame.isHost) ? "playerBoard" : "hostBoard";
        // read all the opponent's children node data
        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(opponentUser).addChildEventListener(new ChildEventListener() {

            // run this when adding more children node
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            // run this when the child node changed
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // update the current local grid from opponent's grid in the database
                int tempRow = Integer.parseInt(snapshot.getKey().substring(0, 1));
                int tempColumn = Integer.parseInt(snapshot.getKey().substring(2, 3));
                int value = snapshot.getValue(Integer.class).intValue();
                opponentGrid[tempRow][tempColumn] = value;

                // if the user is the host, set the opponent's grid to the player grid
                if (FirebaseGame.isHost){
                    playerGrid[tempRow][tempColumn] = opponentGrid[tempRow][tempColumn];
                }
                // if the user is not the host, set the opponent's grid to the host grid
                else {
                    hostGrid[tempRow][tempColumn] = opponentGrid[tempRow][tempColumn];
                }
                // call the callback interface once we finished reading the data
                onSuccessReadingCallBack.onSuccess(tempRow, tempColumn);
            }

            // run this when we remove the child node
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            // run this when we cancel the child node
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            // if the read is cancelled, return an error message to the user
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read Opponent location failed");
            }
        });
    }

    // check if the user is winning
    static public boolean isWinning(){
        boolean winning = true;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (opponentGrid[i][j] == 1 || opponentGrid[i][j] == 2 || opponentGrid[i][j] == 3 || opponentGrid[i][j] == 4 || opponentGrid[i][j] == 5){
                    winning = false;
                }
            }
        }
        return winning;
    }

    // check if the user is losing
    static public boolean isLosing(){
        boolean losing = true;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (currentGrid[i][j] == 1 || currentGrid[i][j] == 2 || currentGrid[i][j] == 3 || currentGrid[i][j] == 4 || currentGrid[i][j] == 5){
                    losing = false;
                }
            }
        }
        return losing;
    }

    // reset the grid once the game is finished
    static public void resetGrid(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                currentGrid[i][j] = 6;
                opponentGrid[i][j] = 6;
                hostGrid[i][j] = 6;
                playerGrid[i][j] = 6;
            }
        }
    }
}
