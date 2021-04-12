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

public class UserwaitingActivity extends AppCompatActivity {

    Button createRoom;
    Button quickMatch;
    TextView textView;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userwaiting);

        quickMatch=(Button) findViewById(R.id.quickMatchBTNID);
        textView=(TextView) findViewById(R.id.userNameShowID);

        Bundle bundle=getIntent().getExtras();

        String name=bundle.getString("username");
        String userName=name;
        textView.setText("Username: "+name);

        final LoadingDialog loadingDialog=new LoadingDialog(com.example.myapplication.UserwaitingActivity.this);


        quickMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerMatchMaking.MatchMadeCallback matchMadeCallback = new PlayerMatchMaking.MatchMadeCallback() {
                    @Override
                    public void run(PlayerMatchMaking matchMaking) {


                        String auth = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        FirebaseGame.setUserValue(matchMaking.isUserHost(), auth, matchMaking.mGameLocation, userName);
                        FirebaseGame.setOpponentValue(new FirebaseGame.onCompletion() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure() {
                                System.out.println("setOpponentValue failed");
                            }
                        });

                        loadingDialog.dismiss();

                        Intent intent=new Intent(v.getContext(), CreateGrid.class);
                        startActivity(intent);
                        finish();
                    }
                };
                PlayerMatchMaking matchMaking = PlayerMatchMaking.createInstance("1234",
                        matchMadeCallback);

                matchMaking.searchMatch();
                loadingDialog.customDialog(LoadingDialog.WAITINGROOM);
            }
        });

    }
}