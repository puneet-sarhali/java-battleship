package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    GridView gridView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (GridView) findViewById(R.id.grid_view);
        imageAdapter adapter = new imageAdapter(this);
        gridView.setAdapter(adapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setImageArray(position,R.drawable.ship2);
                gridView.setAdapter(adapter);
            }
        });

    }
}

















//updates the location of the ships after they are dragged
/*    @SuppressLint("ClickableViewAccessibility")
    private void updateLocation(int id){
        ImageView imageView = findViewById(id);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        xDown = event.getX();
                        yDown = event.getY();
                        Log.d("debug", String.valueOf(imageView.getX())+ "   " + String.valueOf(event.getX()));
                        break;

                    case MotionEvent.ACTION_MOVE:
                        imageView.setX(imageView.getX()+(event.getX()-xDown));
                        imageView.setY(imageView.getY()+(event.getY()-yDown));
                        break;
                }
                return true;

            }
        });

    }
}*/
