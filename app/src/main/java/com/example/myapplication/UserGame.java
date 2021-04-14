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

// spectate the opponent's move
public class UserGame extends AppCompatActivity {
    GridView gridView;
    imageAdapter grid = new imageAdapter(this);
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_game);
        getSupportActionBar().hide();

        gridView = findViewById(R.id.grid_view_game);

        // set view by id
        TextView hitCounter = (TextView) findViewById(R.id.hitCounterUser);
        TextView missCounter = (TextView) findViewById(R.id.missCounterUser);
        TextView hitRate = (TextView)findViewById(R.id.hitRateUser);
        TextView infoText = (TextView)findViewById(R.id.infoUser);
        TextView stateText = (TextView)findViewById(R.id.stateInfoUser);
        infoText.setText("Spectating enemy " + FirebaseGame.opponentName + "\'s move");
        stateText.setText("");

        // creates the grid image based on the local grid
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
                } else if (FirebaseGrid.currentGrid[i][j] == -6) {
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
                } else if (FirebaseGrid.currentGrid[i][j] == 0) {
                    grid.setImageArray(j + 8 * i, R.drawable.ship_parts);
                }
            }
        }

        // create the image
        gridView.setAdapter(grid);

        hitCounter.setText(getString(R.string.hit) + Analysis.hitCounter);
        missCounter.setText(getString(R.string.miss) + Analysis.missCounter);
        hitRate.setText(getString(R.string.hit_rate) + Analysis.getHitRate());

        button = findViewById(R.id.userButton);

        // read from the Firebase database on the user's grid
        FirebaseGrid.readCurrentLocation(new FirebaseGrid.OnSuccessReadingCallBack() {
            @Override
            // check which tile the opponent attacks
            public void onSuccess(int row, int column) {
                // if the opponent missed the ships, go to OpponentGame activity
                if (FirebaseGrid.currentGrid[row][column] == -6){
                    // set the image to missed bomb
                    grid.setImageArray(column + 8 * row, R.drawable.water_sunk);
                    gridView.setAdapter(grid);
                    stateText.setText("Enemy missed");
                    Intent intent = new Intent(UserGame.this, OpponentGame.class);
                    startActivity(intent);
                    finish();
                }
                // if the opponent hit, he/she shall hit again, and the user stays in this activity
                else {
                    stateText.setText("Enemy has hit your ship");
                    // shows all the ships the user could hit
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
                    }
                    // this is auto fill to ship parts by the system
                    else if (FirebaseGrid.currentGrid[row][column] == 0){
                        grid.setImageArray(column + 8 * row, R.drawable.ship_parts);
                        stateText.setText("Your ship has been destroyed");
                    }

                    // set the grid image
                    gridView.setAdapter(grid);
                    // check if you are losing. If you lose, you get a you lost message and get a
                    // button that leads to the main menu
                    if (FirebaseGrid.isLosing()){
                        stateText.setText("Sorry, you lost");
                        button.setVisibility(View.VISIBLE);


                    }
                }
            }
        });

        // the button that leads to the main menu
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // reset the grid
                FirebaseGrid.resetGrid();
                // set the Firebase database game path to null (delete the game path)
                FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).setValue(null);
                String currentUser = (FirebaseGame.isHost) ? "hostBoard" : "playerBoard";
                // remove the event listener in the Firebase database
                if (FirebaseGrid.readCurrentLocationListener != null){
                    FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).
                            child(currentUser).removeEventListener(FirebaseGrid.readCurrentLocationListener);
                }
                // reset the game counter
                Analysis.resetCounter();
                // go to the main activity
                Intent intent = new Intent(UserGame.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}