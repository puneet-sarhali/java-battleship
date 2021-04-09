package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.firebase.database.DataSnapshot;

import match.FirebaseGame;
import match.FirebaseGrid;

public class Game extends AppCompatActivity {

    GridView gridView;
    imageAdapter grid = new imageAdapter(this);
    FirebaseGrid firebaseGrid = new FirebaseGrid();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //fills the grid view with 64 water png's using image adapter class
        gridView = findViewById(R.id.grid_view_game);
        gridView.setAdapter(grid);

        firebaseGrid.readOpponentLocation(new FirebaseGrid.OnGetChildEventListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onSuccess(DataSnapshot snapshot) {
                System.out.println("Enemy's grid: " + snapshot.getKey());
            }

            @Override
            public void onFailure() {
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("debug:", "clicked");
                grid.setImageArray(position, R.drawable.destroyer);
                gridView.setAdapter(grid);
                //send position to firebase
                firebaseGrid.setCurrentLocation((int) (position / 8), (int) (position % 8));

                //game logic
            }
        });
    }
}