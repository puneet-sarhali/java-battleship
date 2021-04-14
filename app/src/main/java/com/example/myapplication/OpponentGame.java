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

// the activity attacks on opponent's grid
public class OpponentGame extends AppCompatActivity {

    // declare the views
    GridView gridView;
    imageAdapter grid = new imageAdapter(this);
    Button button;
    TextView stateText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // set the views by id
        TextView hitCounter = findViewById(R.id.hitCounterOpponent);
        TextView missCounter = findViewById(R.id.missCounterOpponent);
        TextView hitRate = findViewById(R.id.hitRateOpponent);
        TextView infoText = findViewById(R.id.infoOpponent);
        stateText = findViewById(R.id.stateInfoOpponent);
        // set the info text to this message
        infoText.setText("Attacking on opponent " + FirebaseGame.opponentName + "\'s grid");
        stateText.setText("");

        //fills the grid view with 64 water png's using image adapter class
        gridView = findViewById(R.id.grid_view_game);

        // creates the grid view based on the ship data stored in the grid
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
                } else if (FirebaseGrid.opponentGrid[i][j] == -6) {
                    grid.setImageArray(j + 8 * i, R.drawable.water_sunk);
                } else if (FirebaseGrid.opponentGrid[i][j] == 0) {
                    grid.setImageArray(j + 8 * i, R.drawable.ship_parts);
                }
            }
        }

        // create the grid view on the screen
        gridView.setAdapter(grid);

        // set the hit, miss counter and hit rate on the grid
        hitCounter.setText("Hit Counter: " + Analysis.hitCounter);
        missCounter.setText("Miss Counter: " + Analysis.missCounter);
        hitRate.setText("Hit Rate: " + Analysis.getHitRate());

        button = findViewById(R.id.opponentButton);

        // set a listener on the grid
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // run this method whenever clicking on the grid
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("debug:", "clicked");

                // store the location the user clicked on the Firebase database and local as well
                FirebaseGrid.setOpponentLocation((int) (position / 8), (int) (position % 8), new FirebaseGrid.OnSuccessSettingCallBack() {
                    @Override
                    public void onSuccess(int number, int row, int column) {
                        // if the bomb hits the water, set the image to water_error and go to UserGame activity
                        if (number == -6) {
                            grid.setImageArray(position, R.drawable.water_error);
                            gridView.setAdapter(grid);

                            // set the text view to miss
                            stateText.setText("miss");

                            Analysis.missCounter++;
                            missCounter.setText("Miss Counter: " + Analysis.missCounter);
                            hitRate.setText("Hit Rate: " + Analysis.getHitRate());

                            Intent intent = new Intent(OpponentGame.this, UserGame.class);
                            startActivity(intent);
                            finish();
                        }
                        // if the bomb hits the ships, set the image to a broken ship, and stay in this activity
                        else {
                            stateText.setText("hit");
                            // a destroyer is hit
                            if (number == -1) {
                                grid.setImageArray(position, R.drawable.destroyer_sunk);

                                Analysis.hitCounter++;
                                hitCounter.setText("Hit Counter: " + Analysis.hitCounter);
                                hitRate.setText("Hit Rate: " + Analysis.getHitRate());

                                // if all the destroyer sunk, run this
                                autofillWaterSunk(1);
                            }
                            // a submarine is hit
                            else if (number == -2) {
                                grid.setImageArray(position, R.drawable.submarine_sunk);

                                Analysis.hitCounter++;
                                hitCounter.setText("Hit Counter: " + Analysis.hitCounter);
                                hitRate.setText("Hit Rate: " + Analysis.getHitRate());

                                autofillWaterSunk(2);
                            }
                            // a cruiser is hit
                            else if (number == -3) {
                                grid.setImageArray(position, R.drawable.cruiser_sunk);

                                Analysis.hitCounter++;
                                hitCounter.setText("Hit Counter: " + Analysis.hitCounter);
                                hitRate.setText("Hit Rate: " + Analysis.getHitRate());

                                autofillWaterSunk(3);
                            }
                            // a battleship is hit
                            else if (number == -4) {
                                grid.setImageArray(position, R.drawable.battleship_sunk);

                                Analysis.hitCounter++;
                                hitCounter.setText("Hit Counter: " + Analysis.hitCounter);
                                hitRate.setText("Hit Rate: " + Analysis.getHitRate());

                                autofillWaterSunk(4);
                            }
                            // a carrier is hit
                            else if (number == -5) {
                                grid.setImageArray(position, R.drawable.carrier_sunk);

                                Analysis.hitCounter++;
                                hitCounter.setText("Hit Counter: " + Analysis.hitCounter);
                                hitRate.setText("Hit Rate: " + Analysis.getHitRate());

                                autofillWaterSunk(5);
                            }

                            // set the images on the screen
                            gridView.setAdapter(grid);

                            // check if the player wins. If so, run this
                            if (FirebaseGrid.isWinning()) {
                                // tell the player he/she wins
                                stateText.setText("Congrats! You win");
                                // disable the on click listener so that the user can't click on the grid no more
                                gridView.setOnItemClickListener(null);
                                // make a button visible so that the players can go back to the main menu
                                button.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }, false);
            }
        });

        // set an on click listener on a invisible button that only appears at the end of the game
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // upon clicking, delete all the game data and event listeners in the Firebase database
                String currentUser = (FirebaseGame.isHost) ? "hostBoard" : "playerBoard";
                if (FirebaseGrid.readCurrentLocationListener != null){
                    FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).
                            child(currentUser).removeEventListener(FirebaseGrid.readCurrentLocationListener);
                }

                FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).setValue(null);

                // reset the game grid
                FirebaseGrid.resetGrid();
                // reset all the counters
                Analysis.resetCounter();
                // go back to the main menu
                Intent intent = new Intent(OpponentGame.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // auto fill the surrounding tiles around the ships if the ship is sunk
    public void autofillWaterSunk(int ship){
        boolean condition = true;
        // check if the ship is sunk
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (FirebaseGrid.opponentGrid[i][j] == ship){
                    condition = false;
                }
            }
        }

        // if the ship is sunk, auto fill the surrounding tiles around the ship
        if (condition){
            stateText.setText("Destroying enemy ships");

            // check which tiles are damaged ship parts
            for (int i = 0; i < 8; i++){
                for (int j = 0; j < 8; j++){
                    if(FirebaseGrid.opponentGrid[i][j] == -ship) {

                        // if setting the grid is successful, set the images to that ship parts
                        FirebaseGrid.OnSuccessSettingCallBack onSuccessSettingCallBack = new FirebaseGrid.OnSuccessSettingCallBack() {
                            @Override
                            public void onSuccess(int number, int row, int column) {
                                grid.setImageArray(row * 8 + column , R.drawable.ship_parts);
                                gridView.setAdapter(grid);
                            }
                        };

                        // if the surrounding tiles exist, and the surrounding tiles are water, set that tile to broken ship parts
                        if (i != 0 && (FirebaseGrid.opponentGrid[i - 1][j] == 6 || FirebaseGrid.opponentGrid[i - 1][j] == -6)){
                            FirebaseGrid.setOpponentLocation(i - 1, j, onSuccessSettingCallBack, true);
                        }
                        if (i != 7 && (FirebaseGrid.opponentGrid[i + 1][j] == 6 || FirebaseGrid.opponentGrid[i + 1][j] == -6)) {
                            FirebaseGrid.setOpponentLocation(i + 1, j, onSuccessSettingCallBack, true);
                        }
                        if (j != 0 && (FirebaseGrid.opponentGrid[i][j - 1] == 6 || FirebaseGrid.opponentGrid[i][j - 1] == -6)) {
                            FirebaseGrid.setOpponentLocation(i, j - 1, onSuccessSettingCallBack, true);
                        }
                        if (j != 7 && (FirebaseGrid.opponentGrid[i][j + 1] == 6 || FirebaseGrid.opponentGrid[i][j + 1] == -6)) {
                            FirebaseGrid.setOpponentLocation(i, j + 1, onSuccessSettingCallBack, true);
                        }
                        if (i != 7 && j != 7 && (FirebaseGrid.opponentGrid[i + 1][j + 1] == 6 || FirebaseGrid.opponentGrid[i + 1][j + 1] == -6)) {
                            FirebaseGrid.setOpponentLocation(i + 1, j + 1, onSuccessSettingCallBack, true);
                        }
                        if (i != 0 && j != 7 && (FirebaseGrid.opponentGrid[i - 1][j + 1] == 6 || FirebaseGrid.opponentGrid[i - 1][j + 1] == -6)) {
                            FirebaseGrid.setOpponentLocation(i - 1, j + 1, onSuccessSettingCallBack, true);
                        }
                        if (i != 0 && j != 0 && (FirebaseGrid.opponentGrid[i - 1][j - 1] == 6 || FirebaseGrid.opponentGrid[i - 1][j - 1] == -6)) {
                            FirebaseGrid.setOpponentLocation(i - 1, j - 1, onSuccessSettingCallBack, true);
                        }
                        if (i != 7 && j != 0 && (FirebaseGrid.opponentGrid[i + 1][j - 1] == 6 || FirebaseGrid.opponentGrid[i + 1][j - 1] == -6)) {
                            FirebaseGrid.setOpponentLocation(i + 1, j - 1, onSuccessSettingCallBack, true);
                        }
                    }
                }
            }
        }
    }
}