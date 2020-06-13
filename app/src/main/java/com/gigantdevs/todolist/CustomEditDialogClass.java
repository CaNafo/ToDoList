package com.gigantdevs.todolist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomEditDialogClass extends Dialog implements
        View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button btnUpdate, btnCancel;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String ref, key, text;
    private TextView txtText;

    public CustomEditDialogClass(Activity a, String ref, String id, String text) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.ref =ref;
        this.key=id;
        this.text = text;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_edit_dialog);

        txtText = findViewById(R.id.txtEditDialog);
        txtText.setText(text);

        btnUpdate = (Button) findViewById(R.id.btnEditDialog);
        btnUpdate.setTag("UPDATE");
        btnUpdate.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.btnCancelDialog);
        btnCancel.setTag("CANCEL");
        btnCancel.setOnClickListener(this);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(ref);

    }


    @Override
    public void onClick(View v) {
        if(v.getTag().toString().equals("UPDATE")){
            if(!txtText.getText().toString().equals("")){
                SharedPreferences prefs = c.getSharedPreferences("NAME", Context.MODE_PRIVATE);
                String name = prefs.getString("name", "No name defined");

                myRef.child(key).child("text").setValue(txtText.getText().toString());
                myRef.child(key).child("lastModificator").setValue(name);
            }else{
                Toast.makeText(c.getApplicationContext(),"Please do not leave ToDo field empty!", Toast.LENGTH_LONG).show();
            }
        }
        dismiss();
    }
}