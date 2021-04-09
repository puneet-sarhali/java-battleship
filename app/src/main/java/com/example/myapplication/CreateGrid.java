package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import shipPackage.Battleship;
import shipPackage.Carrier;
import shipPackage.Cruiser;
import shipPackage.Destroyer;
import shipPackage.ShipCondition;
import shipPackage.Submarine;

public class CreateGrid extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = "debug";
    GridView gridView;
    boolean rotated = false;
    imageAdapter grid = new imageAdapter(this);
    int ship_placed = 0;
    boolean carrierPressed = false, battleshipPressed = false, cruiserPressed = false, submarinePressed = false, destroyerPressed = false;
    int clickCountCarrier = 0, clickCountBattleship = 0, clickCountCruiser = 0,clickCountSubmarine = 0,clickCountDestroyer = 0;


    Carrier carrier = new Carrier("Carrier", ShipCondition.UNDAMAGED,R.id.carrier);
    Battleship battleship = new Battleship("Battleship",ShipCondition.UNDAMAGED, R.id.battleship);
    Cruiser cruiser = new Cruiser("Cruiser", ShipCondition.UNDAMAGED,R.id.cruiser );
    Submarine submarine = new Submarine("Submarine", ShipCondition.UNDAMAGED,R.id.submarine );
    Destroyer destroyer = new Destroyer("Destroyer", ShipCondition.UNDAMAGED,R.id.destroyer );



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_ship);
        getSupportActionBar().hide();

        //initializations of button views
        Button carrierButton = findViewById(carrier.getShipImageID());
        Button battleshipButton = findViewById(battleship.getShipImageID());
        Button cruiserButton = findViewById(cruiser.getShipImageID());
        Button submarineButton = findViewById(submarine.getShipImageID());
        Button destroyerButton = findViewById(destroyer.getShipImageID());
        Button rotate = findViewById(R.id.rotation);



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
        rotate.setOnClickListener(this);

        //places ships on the board
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean imageDrawn = false;
                if(carrierPressed && grid.getItemId(position) == R.drawable.water){
                    imageDrawn = fillAdjacent(position,grid,5, R.drawable.carrier);
                    if(imageDrawn){
                        gridView.setAdapter(grid);
                        ship_placed++;
                        checkReady();
                        carrierButton.setVisibility(View.GONE);
                        carrierPressed = false;
                    }

                }else if(battleshipPressed && grid.getItemId(position) == R.drawable.water){
                    imageDrawn = fillAdjacent(position,grid,4, R.drawable.battleship);
                    if(imageDrawn){
                        gridView.setAdapter(grid);
                        ship_placed++;
                        checkReady();
                        battleshipButton.setVisibility(View.GONE);
                        battleshipPressed = false;
                    }


                }else if(cruiserPressed && grid.getItemId(position) == R.drawable.water){
                    imageDrawn = fillAdjacent(position,grid,3, R.drawable.cruiser);
                    if(imageDrawn){
                        gridView.setAdapter(grid);
                        ship_placed++;
                        checkReady();
                        cruiserButton.setVisibility(View.GONE);
                        cruiserPressed = false;
                    }

                }else if(submarinePressed && grid.getItemId(position) == R.drawable.water){
                    imageDrawn = fillAdjacent(position,grid,2, R.drawable.submarine);
                    if(imageDrawn){
                        gridView.setAdapter(grid);
                        ship_placed++;
                        checkReady();
                        submarineButton.setVisibility(View.GONE);
                        submarinePressed = false;
                    }

                }else if(destroyerPressed && grid.getItemId(position) == R.drawable.water){
                    imageDrawn = fillAdjacent(position,grid,1, R.drawable.destroyer);
                    if(imageDrawn){
                        gridView.setAdapter(grid);
                        ship_placed++;
                        checkReady();
                        destroyerButton.setVisibility(View.GONE);
                        destroyerPressed = false;
                    }
                }


            }
        });

    }

    public void checkReady(){
        if(ship_placed >= 5){
            Button btn = (Button) findViewById(R.id.rotation);
            btn.setText("Ready");
        }
    }

    public boolean fillAdjacent(int position, imageAdapter adapter, int shipSize, int drawable){
        //Horitontal ship placement
        if(!rotated) {
            boolean validPosition = false;
            if (grid.decodePosition(position)[0] != grid.decodePosition(position + shipSize)[0]) {
                int startIndex = ((grid.decodePosition(position)[0] + 1) * 8) - 1;

                for (int i = startIndex; i >= startIndex - shipSize + 1; i--) {
                    if(adapter.isOccupied(i)) validPosition = false;
                    else validPosition = true;
                }

                if(validPosition){
                    for (int i = startIndex; i >= startIndex - shipSize + 1; i--) {
                        adapter.setImageArray(i, drawable);
                    }
                    return true;
                }else{
                    Toast.makeText(this, "Invalid Position", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {

                for (int i = position; i < position + shipSize; i++) {
                    if(adapter.isOccupied(i)) validPosition = false;
                    else validPosition = true;
                }

                if(validPosition){
                    for (int i = position; i < position + shipSize; i++) {
                        adapter.setImageArray(i, drawable);
                    }
                    return true;
                }else{
                    Toast.makeText(this, "Invalid Position", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        }
        //Vertical ship placement
        else {
            if (position + 8*shipSize >= 64) {
                int startIndex = grid.decodePosition(position)[1] + 56;
                Log.d("start index:", String.valueOf(startIndex));
                Log.d("vertical:", "extends to next column");
                for (int i = startIndex; i > startIndex - shipSize*8; i-=8) {
                    adapter.setImageArray(i, drawable);
                    Log.d("current index:", String.valueOf(i));
                }
            } else {
                Log.d("vertical:", "does not extend to next column");
                for (int i = position; i < position + shipSize*8; i+=8) {
                    adapter.setImageArray(i, drawable);
                    Log.d("current index:", String.valueOf(i));
                }
            }
        }

        gridView.setAdapter(adapter);
        return true;

    }



    //defines the onClick functionality of the ship buttons
    @Override
    public void onClick(View v) {
        if(ship_placed>=5){
            //goto next activity
            Intent intent=new Intent(CreateGrid.this,Game.class);
            startActivity(intent);
            finish();
        }
        else{
            if(v.getId() == R.id.rotation) {
                Button btn = (Button) findViewById(R.id.rotation);
                if (btn.getText().equals("Horizontal")) {
                    rotated = true;
                    btn.setText("Vertical");
                } else {
                    rotated = false;
                    btn.setText("Horizontal");
                }
            }
        }


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


















