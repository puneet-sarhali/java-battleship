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

    // create a firebase authentication class object
    private FirebaseAuth mAuth;

    /** check if the current user is been authenticated in Firebase.
     *  If not, re-authenticate the user.
     *  Once the user is authenticated, store the user UID in Firebase
     */
    private void updateUI(FirebaseUser currentUser)
    {

        // authenticate the user again if the user hasn't been authenticated yet

        if (currentUser == null)
        {

            // Signs the users in anonymously so that they don't need to manually signs in
            mAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        // run the method upon completion signs in
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            /*
                            Sign in success, update UI with the signed-in user's information
                             */
                            if (task.isSuccessful()) {


                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }
                            /*
                            Sign in failure, try sign in the user again
                            Display a sign in failure message to the user.
                             */
                            else {
                                Toast.makeText(MainActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
        /*
        User signs in anonymously successfully! proceed to next step
         */
        else
        {
            /*
            Read data from the firebase database in the "Name" node by adding a event listener.
            Check if the user UID is stored there.
            If the UID is stored, do nothing.
            If the UID is not stored, create a node of UID in the "Name" node, and assign its value as "Name"
             */
            FirebaseDatabase.getInstance().getReference("Name").addValueEventListener(new ValueEventListener() {
                @Override
                /*
                once initialized the listener, run this method
                 */
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    /*
                    Check the user's UID in the "Name" node in database
                     */
                    boolean condition = false;
                    for (DataSnapshot data : snapshot.getChildren()){
                        if (data.getKey().equals(currentUser.getUid())){
                            condition = true;
                        }
                    }
                    /*
                    If user UID is not in the database, create one
                     */
                    if (!condition){
                        FirebaseDatabase.getInstance().getReference("Name").child(currentUser.getUid()).setValue("Name");
                    }

                    /*
                    remove the event listener once finished reading data
                     */
                    FirebaseDatabase.getInstance().getReference("Name").removeEventListener(this);
                }

                /*
                display an error message if reading data failed
                 */
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Failing getting UID", Toast.LENGTH_SHORT).show();
                }
            });

            /*
            Read and get the user name under the user UID node by attaching an event listener on the node
            Set the edit text name to the user name in the database
             */
            FirebaseDatabase.getInstance().getReference("Name").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override

                // run this method upon initialization of the listener
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    // set the edit text to the user name under the UID node
                    if (snapshot.getValue() != null){
                        editText.setText(snapshot.getValue().toString());
                    }
                    // remove the event listener once finish rea
                    FirebaseDatabase.getInstance().getReference("Name").child(currentUser.getUid()).removeEventListener(this);
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