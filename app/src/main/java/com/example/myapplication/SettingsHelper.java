package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsHelper extends AppCompatActivity {
    private final SharedPreferences sharedpreferences;

    public SettingsHelper(Context context) {
        sharedpreferences = context.getSharedPreferences("com.example.myapplication", Context.MODE_PRIVATE);
    }

    public float getTextScale(){
        float textScale = 1.0f;
        try {

            // Get the font size from sharedpreferences, set default to medium if key invalid
            String fontSizePref = sharedpreferences.getString("FONT_SIZE", "Medium");
            Log.d("fontpref",fontSizePref);

            //String rotationPref = settings.getString("AUTO_ROTATE","False");

            // Retrieve corresponding scale factor
            if (fontSizePref.equals("Small")) {
                textScale = 0.75f;
            } else if (fontSizePref.equals("Large")) {
                textScale = 1.50f;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return textScale;
    }

    public boolean getRotationSetting(){
        boolean auto_rotate = false;
        String rotationPref = sharedpreferences.getString("AUTO_ROTATE", "False");
        if(rotationPref.equals("True")){
            auto_rotate = true;
        }
        return auto_rotate;
    }

}
