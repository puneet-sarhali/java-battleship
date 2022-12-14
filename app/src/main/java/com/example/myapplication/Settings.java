package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    ImageButton exitButton;
    RadioGroup sizeGroup;
    RadioButton smallButton;
    RadioButton mediumButton;
    RadioButton largeButton;
    TextView settingsTitle;
    TextView textSize;
    TextView screenRotation;
    Switch rotateSwitch;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        exitButton = (ImageButton) findViewById(R.id.exitButton);

        settingsTitle = (TextView) findViewById(R.id.titleText_settings);
        textSize = (TextView) findViewById(R.id.textSize);
        screenRotation = (TextView) findViewById(R.id.screenRotation);
        smallButton = (RadioButton) findViewById(R.id.small);
        mediumButton = (RadioButton) findViewById(R.id.medium);
        largeButton = (RadioButton) findViewById(R.id.large);
        rotateSwitch = (Switch) findViewById(R.id.rotateSwitch);

        //Apply settings
        SettingsHelper s = new SettingsHelper(this);
        //text scale settings
        settingsTitle.setTextSize(Converter.convertPixelsToDp(settingsTitle.getTextSize(), this) * s.getTextScale());
        textSize.setTextSize(Converter.convertPixelsToDp(textSize.getTextSize(), this) * s.getTextScale());
        screenRotation.setTextSize(Converter.convertPixelsToDp(screenRotation.getTextSize(), this) * s.getTextScale());
        smallButton.setTextSize(Converter.convertPixelsToDp(smallButton.getTextSize(), this) * s.getTextScale());
        mediumButton.setTextSize(Converter.convertPixelsToDp(mediumButton.getTextSize(), this) * s.getTextScale());
        largeButton.setTextSize(Converter.convertPixelsToDp(largeButton.getTextSize(), this) * s.getTextScale());
        Log.d("size", String.valueOf(Converter.convertPixelsToDp(settingsTitle.getTextSize(), this)));
        //rotation settings
        if (s.getRotationSetting()) {
            rotateSwitch.setChecked(true);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            rotateSwitch.setChecked(false);
        }


        //Exit settings menu - go back to main page
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sizeGroup = (RadioGroup) findViewById(R.id.sizeGroup);
        sizeGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {
                        // Get the selected Radio Button
                        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                        SharedPreferences sharedPref = getSharedPreferences("com.example.myapplication", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        //Update FONT_SIZE settings inside sharedPreferences depending on user choice
                        if (radioButton.getText().equals("Small")) {
                            Toast.makeText(Settings.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                            editor.putString("FONT_SIZE", "Small");
                            editor.apply();
                            finish();
                            startActivity(getIntent());
                        } else if (radioButton.getText().equals("Medium")) {
                            Toast.makeText(Settings.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                            editor.putString("FONT_SIZE", "Medium");
                            editor.apply();
                            finish();
                            startActivity(getIntent());
                        } else if (radioButton.getText().equals("Large")) {
                            Toast.makeText(Settings.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                            editor.putString("FONT_SIZE", "Large");
                            editor.apply();
                            finish();
                            startActivity(getIntent());
                        }

                    }
                });

        //Check if auto rotation switch is on/off
        rotateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPref = getSharedPreferences("com.example.myapplication", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                //Set the AUTO_ROTATE to corresponding setting inside sharedPreferences
                if (isChecked) {
                    Toast.makeText(Settings.this, "Auto Rotate:ON", Toast.LENGTH_SHORT).show();
                    editor.putString("AUTO_ROTATE", "True");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                } else {
                    Toast.makeText(Settings.this, "Auto Rotate:OFF", Toast.LENGTH_SHORT).show();
                    editor.putString("AUTO_ROTATE", "False");
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                editor.apply();
            }
        });
    }


}