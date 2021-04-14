package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import match.FirebaseGame;
import match.FirebaseGrid;
import match.Ready;
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

    final CreateGridDialog loadingDialog=new CreateGridDialog(  com.example.myapplication.CreateGrid.this);

    //instances of the ships
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



        //initializations of button and text views
        Button carrierButton = findViewById(carrier.getShipImageID());
        Button battleshipButton = findViewById(battleship.getShipImageID());
        Button cruiserButton = findViewById(cruiser.getShipImageID());
        Button submarineButton = findViewById(submarine.getShipImageID());
        Button destroyerButton = findViewById(destroyer.getShipImageID());
        Button rotate = findViewById(R.id.rotation);
        TextView helpText = findViewById(R.id.helpText);

        //Apply settings
        SettingsHelper s = new SettingsHelper(this);
        //text scale settings
        carrierButton.setTextSize(Converter.convertPixelsToDp(carrierButton.getTextSize(),this)*s.getTextScale());
        battleshipButton.setTextSize(Converter.convertPixelsToDp(battleshipButton.getTextSize(),this)*s.getTextScale());
        cruiserButton.setTextSize(Converter.convertPixelsToDp(cruiserButton.getTextSize(),this)*s.getTextScale());
        submarineButton.setTextSize(Converter.convertPixelsToDp(submarineButton.getTextSize(),this)*s.getTextScale());
        destroyerButton.setTextSize(Converter.convertPixelsToDp(destroyerButton.getTextSize(),this)*s.getTextScale());
        rotate.setTextSize(Converter.convertPixelsToDp(rotate.getTextSize(),this)*s.getTextScale());
        helpText.setTextSize(Converter.convertPixelsToDp(rotate.getTextSize(),this)*s.getTextScale());
        //rotation settings
        if(s.getRotationSetting()){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }



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
                    }else{
                        Toast.makeText(CreateGrid.this, "Place the ship one tile away from each other.", Toast.LENGTH_SHORT).show();
                    }

                }else if(battleshipPressed && grid.getItemId(position) == R.drawable.water){
                    imageDrawn = fillAdjacent(position,grid,4, R.drawable.battleship);
                    if(imageDrawn){
                        gridView.setAdapter(grid);
                        ship_placed++;
                        checkReady();
                        battleshipButton.setVisibility(View.GONE);
                        battleshipPressed = false;
                    }else{
                        Toast.makeText(CreateGrid.this, "Place the ship one tile away from each other.", Toast.LENGTH_SHORT).show();
                    }


                }else if(cruiserPressed && grid.getItemId(position) == R.drawable.water){
                    imageDrawn = fillAdjacent(position,grid,3, R.drawable.cruiser);
                    if(imageDrawn){
                        gridView.setAdapter(grid);
                        ship_placed++;
                        checkReady();
                        cruiserButton.setVisibility(View.GONE);
                        cruiserPressed = false;
                    }else{
                        Toast.makeText(CreateGrid.this, "Place the ship one tile away from each other.", Toast.LENGTH_SHORT).show();
                    }

                }else if(submarinePressed && grid.getItemId(position) == R.drawable.water){
                    imageDrawn = fillAdjacent(position,grid,2, R.drawable.submarine);
                    if(imageDrawn){
                        gridView.setAdapter(grid);
                        ship_placed++;
                        checkReady();
                        submarineButton.setVisibility(View.GONE);
                        submarinePressed = false;
                    }else{
                        Toast.makeText(CreateGrid.this, "Place the ship one tile away from each other.", Toast.LENGTH_SHORT).show();
                    }

                }else if(destroyerPressed && grid.getItemId(position) == R.drawable.water){
                    imageDrawn = fillAdjacent(position,grid,1, R.drawable.destroyer);
                    if(imageDrawn){
                        gridView.setAdapter(grid);
                        ship_placed++;
                        checkReady();
                        destroyerButton.setVisibility(View.GONE);
                        destroyerPressed = false;
                    }else{
                        Toast.makeText(CreateGrid.this, "Place the ship one tile away from each other.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

    }

    //checks whether all 5 ships have been placed on the board
    public void checkReady(){
        if(ship_placed >= 5){
            Button btn = (Button) findViewById(R.id.rotation);
            btn.setText(getString(R.string.ready));
            btn.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            btn.setTextSize(24);
        }
    }

    // check the position of the grid, i means position
    // the ships must be 1 tile away from one another
    public boolean checkPosition(int i, imageAdapter adapter){
        if(adapter.isOccupied(i)) {
            return true;
        } else if (i / 8 >= 1 && adapter.isOccupied(i - 8)) {
            return true;
        } else if (i % 8 != 0 && adapter.isOccupied(i - 1)) {
            return true;
        } else if (i / 8 < 7 && adapter.isOccupied(i + 8)) {
            return true;
        } else if (i % 8 != 7 && adapter.isOccupied(i + 1)) {
            return true;
        } else if (i / 8 >= 1 && i % 8 != 0 && adapter.isOccupied(i - 9)) {
            return true;
        } else if (i / 8 < 7 && i % 8 != 7 && adapter.isOccupied(i + 9)) {
            return true;
        } else if (i / 8 < 7 && i % 8 != 0 && adapter.isOccupied(i + 7)) {
            return true;
        } else if (i / 8 >= 1 && i % 8 != 7 && adapter.isOccupied(i - 7)) {
            return true;
        } else {
            return false;
        }
    }

    //autofill ship placement
    public boolean fillAdjacent(int position, imageAdapter adapter, int shipSize, int drawable){

        //Horitontal ship placement
        boolean validPosition = false;
        if(!rotated) {

            if (grid.decodePosition(position)[0] != grid.decodePosition(position + shipSize)[0]) {
                int startIndex = ((grid.decodePosition(position)[0] + 1) * 8) - 1;

                //checks whether the positions where the ship will be placed is occupied
                for (int i = startIndex; i >= startIndex - shipSize + 1; i--) {
                    if (checkPosition(i, adapter)){
                        return false;
                    } else validPosition = true;
                }

                if(validPosition){
                    for (int i = startIndex; i >= startIndex - shipSize + 1; i--) {
                        adapter.setImageArray(i, drawable);
                        // set the grid
                        FirebaseGrid.setCurrentGrid(shipSize, i);
                    }
                    return true;
                }else{
                    return false;
                }
            } else {

                for (int i = position; i < position + shipSize; i++) {
                    if (checkPosition(i, adapter)){
                        return false;
                    } else validPosition = true;
                }

                if(validPosition){
                    for (int i = position; i < position + shipSize; i++) {
                        adapter.setImageArray(i, drawable);
                        FirebaseGrid.setCurrentGrid(shipSize, i);
                    }
                    return true;
                }else{
                    return false;
                }

            }
        }
        //Vertical ship placement
        else {
            if (position + 8*shipSize >= 64) {
                int startIndex = grid.decodePosition(position)[1] + 56;

                for (int i = startIndex; i > startIndex - shipSize*8; i-=8) {
                    if (checkPosition(i, adapter)){
                        return false;
                    } else validPosition = true;
                    Log.d("if", String.valueOf(validPosition));
                }

                if(validPosition){
                    for (int i = startIndex; i > startIndex - shipSize*8; i-=8) {
                        adapter.setImageArray(i, drawable);
                        FirebaseGrid.setCurrentGrid(shipSize, i);
                    }
                    return true;
                }else{
                    return false;
                }


            } else {
                for (int i = position; i < position + shipSize*8; i+=8) {
                    if (checkPosition(i, adapter)){
                        return false;
                    } else validPosition = true;
                    Log.d("else", String.valueOf(validPosition));
                }

                if(validPosition){
                    for (int i = position; i < position + shipSize*8; i+=8) {
                        adapter.setImageArray(i, drawable);
                        FirebaseGrid.setCurrentGrid(shipSize, i);
                        Log.d("current index:", String.valueOf(i));
                    }
                    return true;
                }else{
                    return false;
                }

            }
        }

    }



    //defines the onClick functionality of the ship buttons
    @Override
    public void onClick(View v) {
        // if all the ships are placed, run this
        if(ship_placed>=5){

            // creates a loading dialog that tells the player to be ready for the opponent
            loadingDialog.customDialog();
            loadingDialog.dismiss();

            // get the database reference for both the user and the opponent to see if they are ready
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.currentUID).child("isReady");;
            DatabaseReference opponentReference = FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.opponentUID).child("isReady");

            // the user is ready
            Ready ready = new Ready(userReference, opponentReference, new Ready.ReadyMatchComplete() {
                @Override
                public void run() {
                    //goto next activity

                    FirebaseGame.removeOpponentValueListener();
                    FirebaseGrid.getOpponentGrid(new FirebaseGrid.OnSuccessReadingCallBack() {
                        @Override
                        public void onSuccess(int row, int column) {
                            Intent intent;

                            if (FirebaseGame.isHost){
                                intent = new Intent(CreateGrid.this, OpponentGame.class);
                            } else {
                                intent = new Intent(CreateGrid.this, UserGame.class);
                            }

                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });

            ready.beReady();

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


















