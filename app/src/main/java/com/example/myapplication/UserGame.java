package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
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

        TextView hitCounter = (TextView) findViewById(R.id.hitCounterUser);
        TextView missCounter = (TextView) findViewById(R.id.missCounterUser);
        TextView hitRate = (TextView)findViewById(R.id.hitRateUser);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (FirebaseGrid.currentGrid[i][j] == -1) {
                    grid.setImageArray(j + 8 * i, R.drawable.destroyer_sunk);
                } else if (FirebaseGrid.currentGrid[i][j] == -2) {
                    grid.setImageArray(j + 8 * i, R.drawable.submarine_sunk);
                } else if (FirebaseGrid.currentGrid[i][j] == -3) {
                    grid.setImageArray(j + 8 * i, R.drawable.cruiser_sunk);
                } else if (FirebaseGrid.currentGrid[i][j] == -4) {
                    grid.setImageArray(j + 8 * i, R.drawable.battleship_sunk);
                } else if (FirebaseGrid.currentGrid[i][j] == -5) {
                    grid.setImageArray(j + 8 * i, R.drawable.carrier_sunk);
                } else if (FirebaseGrid.currentGrid[i][j] == -6 || FirebaseGrid.currentGrid[i][j] == 0) {
                    grid.setImageArray(j + 8 * i, R.drawable.water_sunk);
                } else if (FirebaseGrid.currentGrid[i][j] == 1) {
                    grid.setImageArray(j + 8 * i, R.drawable.destroyer);
                } else if (FirebaseGrid.currentGrid[i][j] == 2) {
                    grid.setImageArray(j + 8 * i, R.drawable.submarine);
                } else if (FirebaseGrid.currentGrid[i][j] == 3) {
                    grid.setImageArray(j + 8 * i, R.drawable.cruiser);
                } else if (FirebaseGrid.currentGrid[i][j] == 4) {
                    grid.setImageArray(j + 8 * i, R.drawable.battleship);
                } else if (FirebaseGrid.currentGrid[i][j] == 5) {
                    grid.setImageArray(j + 8 * i, R.drawable.carrier);
                }
            }
        }

        gridView.setAdapter(grid);

        hitCounter.setText("Hit Counter: " + Analysis.hitCounter);
        missCounter.setText("Miss Counter: " + Analysis.missCounter);
        hitRate.setText("Hit Rate: " + Analysis.getHitRate());

        button = findViewById(R.id.userButton);

        FirebaseGrid.readCurrentLocation(new FirebaseGrid.OnSuccessReadingCallBack() {
            @Override
            public void onSuccess(int row, int column) {
                if (FirebaseGrid.currentGrid[row][column] == -6){
                    grid.setImageArray(column + 8 * row, R.drawable.water_sunk);
                    gridView.setAdapter(grid);
                    Intent intent = new Intent(UserGame.this, OpponentGame.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (FirebaseGrid.currentGrid[row][column] == -1){
                        grid.setImageArray(column + 8 * row, R.drawable.destroyer_sunk);
                    } else if (FirebaseGrid.currentGrid[row][column] == -2){
                        grid.setImageArray(column + 8 * row, R.drawable.submarine_sunk);
                    } else if (FirebaseGrid.currentGrid[row][column] == -3){
                        grid.setImageArray(column + 8 * row, R.drawable.cruiser_sunk);
                    } else if (FirebaseGrid.currentGrid[row][column] == -4){
                        grid.setImageArray(column + 8 * row, R.drawable.battleship_sunk);
                    } else if (FirebaseGrid.currentGrid[row][column] == -5){
                        grid.setImageArray(column + 8 * row, R.drawable.carrier_sunk);
                    } else if (FirebaseGrid.currentGrid[row][column] == 0){
                        grid.setImageArray(column + 8 * row, R.drawable.water_sunk);
                    }

                    gridView.setAdapter(grid);
                    if (FirebaseGrid.isLosing()){
                        Toast.makeText(UserGame.this, "Sorry, you lost", Toast.LENGTH_SHORT).show();
                        button.setVisibility(View.VISIBLE);

                        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).setValue(null);
                    }
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseGrid.resetGrid();
                String currentUser = (FirebaseGame.isHost) ? "hostBoard" : "playerBoard";
                if (FirebaseGrid.readCurrentLocationListener != null){
                    FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).
                            child(currentUser).removeEventListener(FirebaseGrid.readCurrentLocationListener);
                }
                Analysis.resetCounter();
                Intent intent = new Intent(UserGame.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}