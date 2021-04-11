package match;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import gridPackage.Grid;

public class FirebaseGrid {
    public interface OnGetChildEventListener{
        void onStart();
        void onFinish();
        void onSuccess(DataSnapshot snapshot);
        void onFailure();
    }

    // a database reference to the user grid and enemy's grid
    // 0 means empty, 1 means bomb on empty
    // 2 means ship, 3 means damaged ship
    static public int[][] hostGrid = new int[8][8];
    static public int[][] playerGrid = new int[8][8];
    static public int[][] currentGrid = new int[8][8];
    static public int[][] opponentGrid = new int[8][8];

    static {
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                currentGrid[i][j] = 0;
                opponentGrid[i][j] = 0;
                hostGrid[i][j] = 0;
                playerGrid[i][j] = 0;
            }
        }
    }

    // default constructor
    public FirebaseGrid(){
    }

    // don't call this
    static public void initializeGridInFirebase(){
        String userBoard = (FirebaseGame.isHost) ? "hostBoard" : "playerBoard";
        int value;
        String location;
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

    // a setter that sets the player squares that were hit to true into the database
    static public void setCurrentLocation(int row, int column){
        String userBoard = (FirebaseGame.isHost) ? "hostBoard" : "playerBoard";
        String location = row + "_" + column;

        if (currentGrid[row][column] == 0){
            currentGrid[row][column] = 1;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(userBoard).child(location).setValue(1);
        } else if (currentGrid[row][column] == 2) {
            currentGrid[row][column] = 3;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(userBoard).child(location).setValue(3);
        }
        if (FirebaseGame.isHost){
            hostGrid[row][column] = currentGrid[row][column];
        } else {
            playerGrid[row][column] = currentGrid[row][column];
        }
    }

    // a setter that sets the enemy's squares that were hit to true into the database
    static public void setOpponentLocation(int row, int column){
        String opponentBoard = (FirebaseGame.isHost) ? "playerBoard" : "hostBoard";
        String location = row + "_" + column;

        if (opponentGrid[row][column] == 0){
            opponentGrid[row][column] = 1;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(opponentBoard).child(location).setValue(1);
        }
        else if (opponentGrid[row][column] == 2){
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(opponentBoard).child(location).setValue(3);
        }
        if (FirebaseGame.isHost){
            playerGrid[row][column] = opponentGrid[row][column];
        } else {
            hostGrid[row][column] = opponentGrid[row][column];
        }
    }

    // read the current player's data
    static public void readCurrentLocation(OnGetChildEventListener mOnGetChildEventListener){
        mOnGetChildEventListener.onStart();
        String currentUser = (FirebaseGame.isHost) ? "hostMoves" : "playerMoves";
        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(currentUser).addChildEventListener(new ChildEventListener() {
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
                mOnGetChildEventListener.onSuccess(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mOnGetChildEventListener.onFailure();
            }
        });
        mOnGetChildEventListener.onFinish();
    }

    // read the opponent's data
    static public void readOpponentLocation(OnGetChildEventListener mOnGetChildEventListener){
        mOnGetChildEventListener.onStart();
        String opponentUser = (FirebaseGame.isHost) ? "playerMoves" : "hostMoves";
        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(opponentUser).addChildEventListener(new ChildEventListener() {
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
                    playerGrid[tempRow][tempColumn] = currentGrid[tempRow][tempColumn];
                } else {
                    hostGrid[tempRow][tempColumn] = currentGrid[tempRow][tempColumn];
                }
                mOnGetChildEventListener.onSuccess(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mOnGetChildEventListener.onFailure();
            }
        });
        mOnGetChildEventListener.onFinish();
    }
}
