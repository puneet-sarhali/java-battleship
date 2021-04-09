package com.example.myapplication;

import android.content.Context;
import android.media.Image;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import static com.example.myapplication.R.drawable.water;

public class imageAdapter extends BaseAdapter {

    //this class takes care of all the grid formation for this game

    int row;
    int col;
    int row_col[];
    private Context context;
    int arrayLength = 64;
    public int[] imageArray = new int[]{
            water, water, water, water, water, water, water, water,
            water, water, water, water, water, water, water, water,
            water, water, water, water, water, water, water, water,
            water, water, water, water, water, water, water, water,
            water, water, water, water, water, water, water, water,
            water, water, water, water, water, water, water, water,
            water, water, water, water, water, water, water, water,
            water, water, water, water, water, water, water, water,

    };


    public void setRow(int Row){
        this.row = Row;
    }
    public void setCol(int Col){
        this.col = Col;
    }
    
    public int getRow(int position){
        row_col = decodePosition(position);
        row = row_col[0];
        return row;
    }
    public int getCol(int position){
        row_col = decodePosition(position);
        col = row_col[1];
        return col;
    }

    public void setImageArray(int position, int id){
        imageArray[position]= id;
    }

    public imageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayLength;
    }

    @Override
    public Object getItem(int position) {
        return imageArray.length;
    }


    @Override
    public long getItemId(int position) {
        return imageArray[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //imageArray = fillArray();
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(imageArray[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(120,120));

        return imageView;
    }

    //takes position as input and returns an array of length 2 with 1st int as row and 2nd int as col
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

    //this functions checks how many ship parts have been drawn on the board
    public boolean checkShipsParts(){
        return true;
    }

    public boolean isOccupied(int position){
        if(imageArray[position] != water){
            return true;
        }else return false;
    }


}
