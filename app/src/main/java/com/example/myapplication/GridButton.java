package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GridButton extends AppCompatActivity {
    private Button createGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_button);

        createGrid = (Button) findViewById(R.id.createGridButton);

        createGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGridActivity();
            }
        });

    }
    public void openGridActivity(){
        Intent intent = new Intent(this, CreateGrid.class);
        startActivity(intent);
    }
}