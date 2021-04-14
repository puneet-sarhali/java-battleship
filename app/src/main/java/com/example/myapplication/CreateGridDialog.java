package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

// the dialog only appears in the create grid activity
public class CreateGridDialog {
    private Activity activity;
    private AlertDialog alertDialog;

    // a parameterized constructor
    CreateGridDialog(Activity myActivity){
        activity=myActivity;
    }

    // create a custom dialog
    void customDialog(){

        // builds the alert dialog
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);

        LayoutInflater layoutInflater=activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.activity_create_grid_dialog, null));
        builder.setCancelable(false);

        // create and show the dialog
        alertDialog=builder.create();
        builder.show();
    }

    // dismiss the dialog
    void dismiss(){
        alertDialog.dismiss();
    }
}
