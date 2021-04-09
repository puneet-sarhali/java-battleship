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
    public boolean[][] hostGrid = new boolean[8][8];
    public boolean[][] playerGrid = new boolean[8][8];
    public boolean[][] currentGrid = new boolean[8][8];
    public boolean[][] opponentGrid = new boolean[8][8];

    // default constructor
    public FirebaseGrid(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                currentGrid[i][j] = false;
                opponentGrid[i][j] = false;
                hostGrid[i][j] = false;
                playerGrid[i][j] = false;
            }
        }
    }

    // a setter that sets the player squares that were hit to true into the database
    public void setCurrentLocation(int row, int column){
        String currentUser = (FirebaseGame.isHost) ? "hostMoves" : "playerMoves";
        String moveName = row + "_" + column;

        currentGrid[row][column] = true;
        if (FirebaseGame.isHost){
            hostGrid[row][column] = true;
        } else {
            playerGrid[row][column] = true;
        }

        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(currentUser).child(moveName).setValue(true);
    }

    // a setter that sets the enemy's squares that were hit to true into the database
    public void setOpponentLocation(int row, int column){
        String opponentUser = (FirebaseGame.isHost) ? "playerMoves" : "hostMoves";
        String moveName = row + "_" + column;

        opponentGrid[row][column] = true;
        if (FirebaseGame.isHost){
            playerGrid[row][column] = true;
        } else {
            hostGrid[row][column] = true;
        }

        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(opponentUser).child(moveName).setValue(true);
    }

    // read the current player's data
    public void readCurrentLocation(OnGetChildEventListener mOnGetChildEventListener){
        mOnGetChildEventListener.onStart();
        String currentUser = (FirebaseGame.isHost) ? "hostMoves" : "playerMoves";
        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(currentUser).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int tempRow = Integer.parseInt(snapshot.getValue().toString().substring(0, 1));
                int tempColumn = Integer.parseInt(snapshot.getValue().toString().substring(2, 3));
                currentGrid[tempRow][tempColumn] = true;
                if (FirebaseGame.isHost){
                    hostGrid[tempRow][tempColumn] = true;
                } else {
                    playerGrid[tempRow][tempColumn] = true;
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
    public void readOpponentLocation(OnGetChildEventListener mOnGetChildEventListener){
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
                currentGrid[tempRow][tempColumn] = true;
                if (FirebaseGame.isHost){
                    playerGrid[tempRow][tempColumn] = true;
                } else {
                    hostGrid[tempRow][tempColumn] = true;
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
