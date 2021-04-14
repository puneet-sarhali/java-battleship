package com.example.myapplication;

import android.content.Context;
import android.util.DisplayMetrics;

public class Converter {
//Conversion tool taken from https://stackoverflow.com/questions/4605527/converting-pixels-to-dp
    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
