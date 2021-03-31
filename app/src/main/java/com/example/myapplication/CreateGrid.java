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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Arrays;

public class CreateGrid extends AppCompatActivity implements View.OnClickListener{


    GridView gridView;
    boolean carrierPressed = false, battleshipPressed = false, cruiserPressed = false, submarinePressed = false, destroyerPressed = false;
    int clickCountCarrier = 0, clickCountBattleship = 0, clickCountCruiser = 0,clickCountSubmarine = 0,clickCountDestroyer = 0;

    public final static int CARRIER_LENGTH = 5;
    public final static int BATTLESHIP_LENGTH = 4;
    public final static int CRUISER_LENGTH = 3;
    public final static int SUBMARINE_LENGTH = 2;
    public final static int DESTROYER_LENGTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //initializations of views

        Button carrier = findViewById(R.id.carrier);
        Button battleship = findViewById(R.id.battleship);
        Button cruiser = findViewById(R.id.cruiser);
        Button submarine = findViewById(R.id.submarine);
        Button destroyer = findViewById(R.id.destroyer);
        gridView = (GridView) findViewById(R.id.grid_view);


        //fills the grid view with 64 water png's using image adapter class
        imageAdapter adapter = new imageAdapter(this);
        gridView.setAdapter(adapter);

        //sets the onclick listener defined for this class.
        carrier.setOnClickListener(this);
        battleship.setOnClickListener(this);
        cruiser.setOnClickListener(this);
        submarine.setOnClickListener(this);
        destroyer.setOnClickListener(this);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //get the row and column of the position that is being clicked.
                int row = -99, col = -99;
                int[] row_column = decodePosition(position);
                row = row_column[0];
                col = row_column[1];



                if(carrierPressed){
                    adapter.setImageArray(position,R.drawable.carrier);
                    gridView.setAdapter(adapter);
                }else if(battleshipPressed){
                    adapter.setImageArray(position,R.drawable.battleship);
                    gridView.setAdapter(adapter);
                }else if(cruiserPressed){
                    adapter.setImageArray(position,R.drawable.cruiser);
                    gridView.setAdapter(adapter);
                }else if(submarinePressed){
                    adapter.setImageArray(position,R.drawable.submarine);
                    gridView.setAdapter(adapter);
                }else if(destroyerPressed){
                    adapter.setImageArray(position,R.drawable.destroyer);
                    gridView.setAdapter(adapter);
                }
            }
        });

    }


    //desfines the onClick functionality of the ship buttons
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.carrier:
                Log.d("debug", "onClick: I'm in carrier");
                if(clickCountCarrier%2 == 0){
                    carrierPressed = true;
                    v.setBackgroundColor(getResources().getColor(R.color.black));
                    clickCountCarrier++;
                }else {
                    carrierPressed = false;
                    v.setBackgroundColor(getResources().getColor(R.color.carrierColour));
                    clickCountCarrier++;
                }
                break;

            case R.id.battleship:
                if(clickCountBattleship%2 == 0){
                    battleshipPressed = true;
                    v.setBackgroundColor(getResources().getColor(R.color.black));
                    clickCountBattleship++;
                }else {
                    battleshipPressed = false;
                    v.setBackgroundColor(getResources().getColor(R.color.battleshipColour));
                    clickCountBattleship++;
                }

                break;

            case R.id.cruiser:
                if(clickCountCruiser%2 == 0){
                    cruiserPressed = true;
                    v.setBackgroundColor(getResources().getColor(R.color.black));
                    clickCountCruiser++;
                }else {
                    cruiserPressed = false;
                    v.setBackgroundColor(getResources().getColor(R.color.cruiserColour));
                    clickCountCruiser++;
                }

                break;

            case R.id.submarine:
                if(clickCountSubmarine%2 == 0){
                    submarinePressed = true;
                    v.setBackgroundColor(getResources().getColor(R.color.black));
                    clickCountSubmarine++;
                }else {
                    submarinePressed = false;
                    v.setBackgroundColor(getResources().getColor(R.color.submarineColour));
                    clickCountSubmarine++;
                }


                break;

            case R.id.destroyer:
                if(clickCountDestroyer%2 == 0){
                    destroyerPressed = true;
                    v.setBackgroundColor(getResources().getColor(R.color.black));
                    clickCountDestroyer++;
                }else {
                    destroyerPressed = false;
                    v.setBackgroundColor(getResources().getColor(R.color.destroyerColour));
                    clickCountDestroyer++;
                }


                break;

        }
    }

    public int[] decodePosition(int position){
        int[] row_column = new int[2];

        if (position >= 0 && position <= 7) {
            row_column[0]=0;
            row_column[1]= position;
        } else if (position >= 8 && position <= 15) {
            row_column[0]=1;
            row_column[1]= position - 8;
        } else if (position >= 16 && position <= 23) {
            row_column[0]=2;
            row_column[1]= position - 16;
        } else if(position >= 24 && position <= 31) {
            row_column[0]=3;
            row_column[1]= position - 24;
        } else if(position >= 32 && position <= 39) {
            row_column[0]=4;
            row_column[1]= position - 32;
        } else if(position >= 40 && position <= 47) {
            row_column[0]=5;
            row_column[1]= position - 40;
        } else if(position >= 48 && position <= 55) {
            row_column[0]=6;
            row_column[1]= position - 48;
        }else if(position >= 56 && position <= 63) {
            row_column[0]=7;
            row_column[1]= position - 56;
        }else{
            row_column[0] = 100;
            row_column[1] = 100;
        }

        return row_column;
    }

    public boolean isValidPosition(int position, int shipSize){
        boolean validity = false;
        int row = -99, col = -99;
        int[] row_column = decodePosition(position);
        row = row_column[0];
        col = row_column[1];

        



        return validity;
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
