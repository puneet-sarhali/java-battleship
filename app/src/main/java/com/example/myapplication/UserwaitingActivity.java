package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
                        Intent intent=new Intent(v.getContext(), CreateGrid.class);
                        startActivity(intent);
                        finish();
                    }
                };
                PlayerMatchMaking matchMaking = PlayerMatchMaking.createInstance("Test",
                        matchMadeCallback);

                matchMaking.searchMatch();
                loadingDialog.customDialog();
            }
        });



    }
}