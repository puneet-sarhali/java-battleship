package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import match.Game;
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

                        Game.gameReference = matchMaking.mGameLocation;
                        Game.isHost = matchMaking.isUserHost();

                        if (Game.isHost){
                           Game.host = auth;
                           Game.hostName = userName;
                           FirebaseDatabase.getInstance().getReference(Game.gameReference).child(Game.host).child("isHost").setValue(true);
                        }
                        else{
                            Game.player = auth;
                            Game.playerName = userName;
                            FirebaseDatabase.getInstance().getReference(Game.gameReference).child(Game.host).child("isHost").setValue(false);
                        }

                        Game game = new Game(matchMaking.mGameLocation, matchMaking.isUserHost(), userName);
                        // create the game and set the game info in the firebase database
                        game.printGame();
                        Intent intent=new Intent(v.getContext(), CreateGrid.class);
                        startActivity(intent);
                        finish();
                    }
                };
                PlayerMatchMaking matchMaking = PlayerMatchMaking.createInstance("1234",
                        matchMadeCallback);

                matchMaking.searchMatch();
                loadingDialog.customDialog();
            }
        });
    }
}