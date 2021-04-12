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

    LoadingDialog(Activity myActivity){
        activity=myActivity;
    }

    void customDialog(){

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
