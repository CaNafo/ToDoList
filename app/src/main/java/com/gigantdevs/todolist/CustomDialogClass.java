package com.gigantdevs.todolist;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button btnOk;
    public TextView txtCreator;
    public TextView txtLastModification;
    public TextView txtDate;

    public String creator;
    public String lastModification;
    public String date;
    
    public CustomDialogClass(Activity a, String creator, String lastModification, String date) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.creator =creator;
        this.lastModification=lastModification;
        this.date = date;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        txtCreator = findViewById(R.id.txtCreator);
        txtLastModification = findViewById(R.id.txtModificator);
        txtDate = findViewById(R.id.txtCreatonDate);

        txtCreator.setText("Created by "+creator);
        txtLastModification.setText("Last modified by "+lastModification);
        txtDate.setText("Creation date: \n"+date);
    }


    @Override
    public void onClick(View v) {
        dismiss();
    }
}