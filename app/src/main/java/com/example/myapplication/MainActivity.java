package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Shahmat's code
    EditText editText;
    Button playButton;
    String value;

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
            FirebaseDatabase.getInstance().getReference("Name").child(mAuth.getCurrentUser().getUid()).setValue("Name");
            FirebaseDatabase.getInstance().getReference("Name").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    editText.setText(snapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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
                    FirebaseDatabase.getInstance().getReference("Name").child(mAuth.getCurrentUser().getUid()).setValue(value);
                    intent.putExtra("username", value);
                    startActivity(intent);
                    finish();

                }
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