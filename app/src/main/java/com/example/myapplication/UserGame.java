package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import match.Analysis;
import match.FirebaseGame;
import match.FirebaseGrid;

public class UserGame extends AppCompatActivity {
    GridView gridView;
    imageAdapter grid = new imageAdapter(this);
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_game);

        gridView = findViewById(R.id.grid_view_game);
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (FirebaseGrid.currentGrid[i][j] == 1){
                    grid.setImageArray(j + 8 * i, R.drawable.battleship);
                } else if (FirebaseGrid.currentGrid[i][j] == 2){
                    grid.setImageArray(j + 8 * i, R.drawable.carrier);
                } else if (FirebaseGrid.currentGrid[i][j] == 3){
                    grid.setImageArray(j + 8 * i, R.drawable.destroyer);
                }
            }
        }

        gridView.setAdapter(grid);

        button = findViewById(R.id.userButton);

        FirebaseGrid.readCurrentLocation(new FirebaseGrid.OnSuccessReadingCallBack() {
            @Override
            public void onSuccess(int row, int column) {
                if (FirebaseGrid.currentGrid[row][column] == 1){
                    grid.setImageArray(column + 8 * row, R.drawable.battleship);
                    gridView.setAdapter(grid);
                    Intent intent = new Intent(UserGame.this, OpponentGame.class);
                    startActivity(intent);
                    finish();
                } else if (FirebaseGrid.currentGrid[row][column] == 3) {
                    grid.setImageArray(column + 8 * row, R.drawable.destroyer);
                    gridView.setAdapter(grid);
                    if (FirebaseGrid.isLosing()){
                        Toast.makeText(UserGame.this, "Sorry, you lost", Toast.LENGTH_SHORT).show();
                        button.setVisibility(View.VISIBLE);
                        String currentUser = (FirebaseGame.isHost) ? "hostBoard" : "playerBoard";
                        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).
                                child(currentUser).removeEventListener(FirebaseGrid.readCurrentLocationListener);
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).setValue(null);
                Intent intent = new Intent(UserGame.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}