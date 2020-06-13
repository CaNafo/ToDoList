package com.gigantdevs.todolist;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

public class CustomYesNoDialogClass extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button btnYes, btnNo;
    private FirebaseDatabase database;
    private String ref, key, name;

    public CustomYesNoDialogClass(Activity a, String catRef, String catKey, String catName) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        database = FirebaseDatabase.getInstance();
        ref = catRef;
        key = catKey;
        name = catName;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_yes_no_dialog);
        btnYes = (Button) findViewById(R.id.btnYes);
        btnYes.setTag("YES");
        btnNo = (Button) findViewById(R.id.btnNo);
        btnNo.setTag("NO");

        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if(v.getTag().equals("YES")){
            database.getReference(name).removeValue();
            database.getReference(ref).child(key).removeValue();
        }
        dismiss();
    }

}