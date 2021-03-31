package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class imageAdapter extends BaseAdapter {


    
    private Context mcontext;
    int arrayLength = 64;
    public int[] imageArray = new int[]{
            R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,
            R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,
            R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,
            R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,
            R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,
            R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,
            R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,
            R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water, R.drawable.water,

    };

    public void setImageArray(int position, int id){
        imageArray[position]= id;
    }

    public imageAdapter(Context mcontext) {
        this.mcontext = mcontext;
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
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //imageArray = fillArray();
        ImageView imageView = new ImageView(mcontext);
        imageView.setImageResource(imageArray[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(120,120));

        return imageView;
    }

    

    /*public int[] fillArray(){
        int[] imageArray = new int[arrayLength];

        for(int i= 0; i<64; i++){

                imageArray[i] = R.drawable.water;

        }

        return imageArray;
    }*/
}
