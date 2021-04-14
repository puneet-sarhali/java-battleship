package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import match.FirebaseGame;
import match.PlayerMatchMaking;

// the user will match make in this activity
public class UserwaitingActivity extends AppCompatActivity {

    // declare the views
    Button createRoom;
    Button quickMatch;
    TextView textView;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userwaiting);
        getSupportActionBar().hide();

        // set the view by id
        quickMatch=(Button) findViewById(R.id.quickMatchBTNID);
        textView=(TextView) findViewById(R.id.userNameShowID);

        // store the extra info onto the next activity
        Bundle bundle=getIntent().getExtras();

        // set the user name string on the text view
        String name=bundle.getString("username");
        String userName=name;
        textView.setText("Hi "+name);

        // creates a loading dialog
        final LoadingDialog loadingDialog=new LoadingDialog(com.example.myapplication.UserwaitingActivity.this);

        // set an on click listener on the quick match button
        quickMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // match the player online
                PlayerMatchMaking.MatchMadeCallback matchMadeCallback = new PlayerMatchMaking.MatchMadeCallback() {
                    @Override

                    // once finished matching, run this method
                    public void run(PlayerMatchMaking matchMaking) {

                        // set auth to the user's UID
                        String auth = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        // set the user's value on Firebase database
                        FirebaseGame.setUserValue(matchMaking.isUserHost(), auth, matchMaking.mGameLocation, userName);
                        // set the opponent's value on Firebase database
                        FirebaseGame.setOpponentValue(new FirebaseGame.onCompletion() {
                            @Override
                            public void onSuccess() {
                            }

                            // if failed, give a message to the developer
                            @Override
                            public void onFailure() {
                                System.out.println("setOpponentValue failed");
                            }
                        });

                        // dismiss the loading dialog
                        loadingDialog.dismiss();

                        // go to the Create grid activity
                        Intent intent=new Intent(v.getContext(), CreateGrid.class);
                        startActivity(intent);
                        finish();
                    }
                };

                // create a matching making object that allows the player to match make in the room id 1234
                PlayerMatchMaking matchMaking = PlayerMatchMaking.createInstance("1234",
                        matchMadeCallback);

                // search for the match
                matchMaking.searchMatch();
                // create the loading dialog
                loadingDialog.customDialog();
            }
        });

    }
}