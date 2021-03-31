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

import shipPackage.Battleship;
import shipPackage.Carrier;
import shipPackage.Cruiser;
import shipPackage.Destroyer;
import shipPackage.ShipCondition;
import shipPackage.Submarine;

public class CreateGrid extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = "debug";
    GridView gridView;
    boolean carrierPressed = false, battleshipPressed = false, cruiserPressed = false, submarinePressed = false, destroyerPressed = false;
    int clickCountCarrier = 0, clickCountBattleship = 0, clickCountCruiser = 0,clickCountSubmarine = 0,clickCountDestroyer = 0;

    Carrier carrier = new Carrier("Carrier", ShipCondition.UNDAMAGED,R.id.carrier);
    Battleship battleship = new Battleship("Battleship",ShipCondition.UNDAMAGED, R.id.battleship);
    Cruiser cruiser = new Cruiser("Cruiser", ShipCondition.UNDAMAGED,R.id.cruiser );
    Submarine submarine = new Submarine("Submarine", ShipCondition.UNDAMAGED,R.id.submarine );
    Destroyer destroyer = new Destroyer("Destroyer", ShipCondition.UNDAMAGED,R.id.destroyer );

    imageAdapter grid = new imageAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //initializations of button views
        Button carrierButton = findViewById(carrier.getShipImageID());
        Button battleshipButton = findViewById(battleship.getShipImageID());
        Button cruiserButton = findViewById(cruiser.getShipImageID());
        Button submarineButton = findViewById(submarine.getShipImageID());
        Button destroyerButton = findViewById(destroyer.getShipImageID());



        //fills the grid view with 64 water png's using image adapter class
        gridView = findViewById(R.id.grid_view);
        gridView.setAdapter(grid);

        Log.d(TAG, String.valueOf(carrier.getLength()) + "  " + String.valueOf(battleship.getLength()));
        //sets the onclick listener defined for this class.
        carrierButton.setOnClickListener(this);
        battleshipButton.setOnClickListener(this);
        cruiserButton.setOnClickListener(this);
        submarineButton.setOnClickListener(this);
        destroyerButton.setOnClickListener(this);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                grid.getRow(position);

                if(carrierPressed){
                    grid.setImageArray(position,R.drawable.carrier);
                    fillAdjacent(position,grid,carrier.getLength(), R.drawable.carrier);
                    gridView.setAdapter(grid);
                }else if(battleshipPressed){
                    grid.setImageArray(position,R.drawable.battleship);
                    fillAdjacent(position,grid,4, R.drawable.battleship);
                    gridView.setAdapter(grid);
                }else if(cruiserPressed){
                    grid.setImageArray(position,R.drawable.cruiser);
                    fillAdjacent(position,grid,3, R.drawable.cruiser);
                    gridView.setAdapter(grid);
                }else if(submarinePressed){
                    grid.setImageArray(position,R.drawable.submarine);
                    fillAdjacent(position,grid,2, R.drawable.submarine);
                    gridView.setAdapter(grid);
                }else if(destroyerPressed){
                    grid.setImageArray(position,R.drawable.destroyer);
                    fillAdjacent(position,grid,1, R.drawable.destroyer);
                    gridView.setAdapter(grid);
                }


            }
        });

    }

    public void fillAdjacent(int position, imageAdapter adapter, int shipSize, int drawable){
        if(grid.decodePosition(position)[0] != grid.decodePosition(position+shipSize)[0]){
            int startIndex = ((grid.decodePosition(position)[0]+1)*8)-1;
            Log.d("debug", String.valueOf(startIndex));
            for(int i = startIndex; i>= startIndex - shipSize + 1; i--){
                adapter.setImageArray(i, drawable);
                Log.d("current index:", String.valueOf(i));
            }
        }
        else {
            for (int i = position; i < position + shipSize; i++) {
                adapter.setImageArray(i, drawable);
            }
        }
        gridView.setAdapter(adapter);
    }



    //defines the onClick functionality of the ship buttons
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
}


















