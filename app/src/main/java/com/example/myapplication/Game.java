package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class Game extends AppCompatActivity {

    GridView gridView;
    imageAdapter grid = new imageAdapter(this);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //fills the grid view with 64 water png's using image adapter class
        gridView = findViewById(R.id.grid_view_game);
        gridView.setAdapter(grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("debug:", "clicked");
                grid.setImageArray(position, R.drawable.destroyer);
                gridView.setAdapter(grid);
                //send position to firebase
                //game logic
            }
        });

    }
}