package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TextView;

import org.w3c.dom.Text;

import match.FirebaseGame;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog alertDialog;
    public static final int WAITINGROOM = 1;
    public static final int CREATEGRID = 2;
    TextView text;

    LoadingDialog(Activity myActivity){
        activity=myActivity;
    }

    void customDialog(int tag){
        if (tag == WAITINGROOM){
            alertDialog.setTitle("Searching for players...");
        } else if (tag == CREATEGRID){
            alertDialog.setTitle("Waiting for " + FirebaseGame.opponentName + " to be ready");
        }

        AlertDialog.Builder builder=new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater=activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);


        alertDialog=builder.create();
        builder.show();
    }

    void dismiss(){
        alertDialog.dismiss();
    }
}
