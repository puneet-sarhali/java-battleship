package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Shahmat's code
    EditText editText;
    Button playButton;
    ImageButton settingsButton;
    String value;
    TextView titleText;

    private FirebaseAuth mAuth;

    private void updateUI(FirebaseUser currentUser)
    {
        if (currentUser == null)
        {
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information

                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(MainActivity.this, "Succeed",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        // shahmat's code
        editText=(EditText) findViewById(R.id.edittext);
        playButton=(Button) findViewById(R.id.playButton1);
        settingsButton =(ImageButton) findViewById(R.id.exitButton);
        titleText = (TextView) findViewById(R.id.titleText) ;

        //Apply settings

        try {
            // Get the font size value from SharedPreferences.
            SharedPreferences settings =
                    getSharedPreferences("com.example.myapplication", Context.MODE_PRIVATE);

            // Get the font size option.  We use "FONT_SIZE" as the key.
            // Make sure to use this key when you set the value in SharedPreferences.
            // We specify "Medium" as the default value, if it does not exist.
            String fontSizePref = settings.getString("FONT_SIZE", "Medium");
            Log.d("fontpref",fontSizePref);

            // Select the proper values inside resource files
            float textScale = 1.0f;
            TypedValue outValue = new TypedValue();
            if (fontSizePref == "Small") {
                getResources().getValue(R.dimen.FontSizeSmall,outValue,true);
                textScale = outValue.getFloat();
            } else if (fontSizePref == "Large") {
                getResources().getValue(R.dimen.FontSizeLarge,outValue,true);
                textScale = outValue.getFloat();
            }

            // Set the size of texts
            Log.d("scale",String.valueOf(textScale));
            titleText.setTextSize(Converter.convertPixelsToDp(titleText.getTextSize(),this)*textScale);
            editText.setTextSize(Converter.convertPixelsToDp(editText.getTextSize(),this)*textScale);
            playButton.setTextSize(Converter.convertPixelsToDp(playButton.getTextSize(),this)*textScale);

            //Set screen rotation
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().length()==0) {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_LONG).show();
                }
                // it will take to the next activity
                else {
                    Intent intent=new Intent(MainActivity.this,UserwaitingActivity.class);
                    value = editText.getText().toString(); //to get the name
                    intent.putExtra("username", value);
                    startActivity(intent);
                    finish();

                }
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(MainActivity.this,Settings.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}