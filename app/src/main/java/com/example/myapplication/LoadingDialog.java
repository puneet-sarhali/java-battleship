package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.TextView;

import org.w3c.dom.Text;

import match.FirebaseGame;

// a loading dialog that displays in the UserwaitingActivity
public class LoadingDialog {


    private Activity activity;
    private AlertDialog alertDialog;

    // parameterized constructor
    LoadingDialog(Activity myActivity){
        activity=myActivity;
    }

    // create a custom dialog
    void customDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater=activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        alertDialog=builder.create();
        builder.show();
    }

    // dismiss the custom dialog
    void dismiss(){
        alertDialog.dismiss();
    }
}
