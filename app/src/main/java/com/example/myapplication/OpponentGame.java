package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import match.Analysis;
import match.FirebaseGame;
import match.FirebaseGrid;

public class OpponentGame extends AppCompatActivity {

    GridView gridView;
    imageAdapter grid = new imageAdapter(this);
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();

        TextView hitCounter = findViewById(R.id.hitCounterOpponent);
        TextView missCounter = findViewById(R.id.missCounterOpponent);
        TextView hitRate = findViewById(R.id.hitRateOpponent);

        //fills the grid view with 64 water png's using image adapter class
        gridView = findViewById(R.id.grid_view_game);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (FirebaseGrid.opponentGrid[i][j] == -1) {
                    grid.setImageArray(j + 8 * i, R.drawable.destroyer_sunk);
                } else if (FirebaseGrid.opponentGrid[i][j] == -2) {
                    grid.setImageArray(j + 8 * i, R.drawable.submarine_sunk);
                } else if (FirebaseGrid.opponentGrid[i][j] == -3) {
                    grid.setImageArray(j + 8 * i, R.drawable.cruiser_sunk);
                } else if (FirebaseGrid.opponentGrid[i][j] == -4) {
                    grid.setImageArray(j + 8 * i, R.drawable.battleship_sunk);
                } else if (FirebaseGrid.opponentGrid[i][j] == -5) {
                    grid.setImageArray(j + 8 * i, R.drawable.carrier_sunk);
                } else if (FirebaseGrid.opponentGrid[i][j] == -6 || FirebaseGrid.opponentGrid[i][j] == 0) {
                    grid.setImageArray(j + 8 * i, R.drawable.water_sunk);
                }
            }
        }

        gridView.setAdapter(grid);

        hitCounter.setText(getString(R.string.hit) + Analysis.hitCounter);
        missCounter.setText(getString(R.string.miss)+ Analysis.missCounter);
        hitRate.setText(getString(R.string.hit_rate) + Analysis.getHitRate());

        button = findViewById(R.id.opponentButton);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("debug:", "clicked");

                FirebaseGrid.setOpponentLocation((int) (position / 8), (int) (position % 8), new FirebaseGrid.OnSuccessSettingCallBack() {
                    @Override
                    public void onSuccess(int number, int row, int column) {
                        if (number == -6) {
                            grid.setImageArray(position, R.drawable.water_error);
                            gridView.setAdapter(grid);
                            Analysis.missCounter++;
                            missCounter.setText(getString(R.string.miss) + Analysis.missCounter);
                            hitRate.setText(getString(R.string.hit_rate) + Analysis.getHitRate());

                            Intent intent = new Intent(OpponentGame.this, UserGame.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (number == -1) {
                                grid.setImageArray(position, R.drawable.destroyer_sunk);

                                Analysis.hitCounter++;
                                hitCounter.setText(getString(R.string.hit) + Analysis.hitCounter);
                                hitRate.setText(getString(R.string.hit_rate) + Analysis.getHitRate());

                                autofillWaterSunk(1);
                            } else if (number == -2) {
                                grid.setImageArray(position, R.drawable.submarine_sunk);

                                Analysis.hitCounter++;
                                hitCounter.setText(getString(R.string.hit) + Analysis.hitCounter);
                                hitRate.setText(getString(R.string.hit_rate) + Analysis.getHitRate());

                                autofillWaterSunk(2);
                            } else if (number == -3) {
                                grid.setImageArray(position, R.drawable.cruiser_sunk);

                                Analysis.hitCounter++;
                                hitCounter.setText(getString(R.string.hit) + Analysis.hitCounter);
                                hitRate.setText(getString(R.string.hit_rate) + Analysis.getHitRate());

                                autofillWaterSunk(3);
                            } else if (number == -4) {
                                grid.setImageArray(position, R.drawable.battleship_sunk);

                                Analysis.hitCounter++;
                                hitCounter.setText(getString(R.string.hit) + Analysis.hitCounter);
                                hitRate.setText(getString(R.string.hit_rate) + Analysis.getHitRate());

                                autofillWaterSunk(4);
                            } else if (number == -5) {
                                grid.setImageArray(position, R.drawable.carrier_sunk);

                                Analysis.hitCounter++;
                                hitCounter.setText(getString(R.string.hit) + Analysis.hitCounter);
                                hitRate.setText(getString(R.string.hit_rate) + Analysis.getHitRate());

                                autofillWaterSunk(5);
                            }

                            gridView.setAdapter(grid);

                            if (FirebaseGrid.isWinning()) {
                                Toast.makeText(OpponentGame.this, "Congrats! You win", Toast.LENGTH_SHORT).show();
                                gridView.setOnItemClickListener(null);
                                String currentUser = (FirebaseGame.isHost) ? "hostBoard" : "playerBoard";
                                button.setVisibility(View.VISIBLE);
                                if (FirebaseGrid.readCurrentLocationListener != null){
                                    FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).
                                            child(currentUser).removeEventListener(FirebaseGrid.readCurrentLocationListener);
                                }
                                FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).setValue(null);
                            }
                        }
                    }
                }, false);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseGrid.resetGrid();
                Analysis.resetCounter();
                Intent intent = new Intent(OpponentGame.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void autofillWaterSunk(int ship){
        boolean condition = true;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (FirebaseGrid.opponentGrid[i][j] == ship){
                    condition = false;
                }
            }
        }

        if (condition){

            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    if(FirebaseGrid.opponentGrid[i][j] == -ship) {

                        FirebaseGrid.OnSuccessSettingCallBack onSuccessSettingCallBack = new FirebaseGrid.OnSuccessSettingCallBack() {
                            @Override
                            public void onSuccess(int number, int row, int column) {
                                grid.setImageArray(row * 8 + column , R.drawable.water_sunk);
                                gridView.setAdapter(grid);
                            }
                        };

                        if (i != 0 && FirebaseGrid.opponentGrid[i - 1][j] == 6) {
                            FirebaseGrid.setOpponentLocation(i - 1, j, onSuccessSettingCallBack, true);
                        }
                        if (i != 7 && FirebaseGrid.opponentGrid[i + 1][j] == 6) {
                            FirebaseGrid.setOpponentLocation(i + 1, j, onSuccessSettingCallBack, true);
                        }
                        if (j != 0 && FirebaseGrid.opponentGrid[i][j - 1] == 6) {
                            FirebaseGrid.setOpponentLocation(i, j - 1, onSuccessSettingCallBack, true);
                        }
                        if (j != 7 && FirebaseGrid.opponentGrid[i][j + 1] == 6) {
                            FirebaseGrid.setOpponentLocation(i, j + 1, onSuccessSettingCallBack, true);
                        }
                        if (i != 7 && j != 7 && FirebaseGrid.opponentGrid[i + 1][j + 1] == 6) {
                            FirebaseGrid.setOpponentLocation(i + 1, j + 1, onSuccessSettingCallBack, true);
                        }
                        if (i != 0 && j != 7 && FirebaseGrid.opponentGrid[i - 1][j + 1] == 6) {
                            FirebaseGrid.setOpponentLocation(i - 1, j + 1, onSuccessSettingCallBack, true);
                        }
                        if (i != 0 && j != 0 && FirebaseGrid.opponentGrid[i - 1][j - 1] == 6) {
                            FirebaseGrid.setOpponentLocation(i - 1, j - 1, onSuccessSettingCallBack, true);
                        }
                        if (i != 7 && j != 0 && FirebaseGrid.opponentGrid[i + 1][j - 1] == 6) {
                            FirebaseGrid.setOpponentLocation(i + 1, j - 1, onSuccessSettingCallBack, true);
                        }
                    }
                }
            }
        }
    }
}