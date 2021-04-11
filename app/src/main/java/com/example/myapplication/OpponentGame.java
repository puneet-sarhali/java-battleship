package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import match.FirebaseGrid;

public class OpponentGame extends AppCompatActivity {

    GridView gridView;
    imageAdapter grid = new imageAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //fills the grid view with 64 water png's using image adapter class
        gridView = findViewById(R.id.grid_view_game);

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (FirebaseGrid.opponentGrid[i][j] == 1){
                    grid.setImageArray(j + 8 * i, R.drawable.battleship);
                }
                else if (FirebaseGrid.opponentGrid[i][j] == 3){
                    grid.setImageArray(j + 8 * i, R.drawable.destroyer);
                }
            }
        }

        gridView.setAdapter(grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("debug:", "clicked");

                FirebaseGrid.setOpponentLocation((int) (position / 8), (int) (position % 8), new FirebaseGrid.OnSuccessSettingCallBack() {
                    @Override
                    public void onSuccess(int number) {
                        if (number == 1) {
                            grid.setImageArray(position, R.drawable.battleship);
                            gridView.setAdapter(grid);
                            Intent intent = new Intent(OpponentGame.this, UserGame.class);
                            startActivity(intent);
                        } else {
                            grid.setImageArray(position, R.drawable.destroyer);
                            gridView.setAdapter(grid);
                            if (FirebaseGrid.isWinning()){
                                Toast.makeText(OpponentGame.this, "Congrats! You win", Toast.LENGTH_SHORT).show();
                                gridView.setOnItemClickListener(null);
                            }
                        }
                    }
                });
            }
        });
    }
}