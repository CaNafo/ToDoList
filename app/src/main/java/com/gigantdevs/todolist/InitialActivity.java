package com.gigantdevs.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InitialActivity extends AppCompatActivity {

    private EditText txtUsername;
    private Button btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        txtUsername = findViewById(R.id.txtUsername);
        btnOk = findViewById(R.id.btnOkInitialActivity);

        btnOk.setOnClickListener(okOnClickListener);

        SharedPreferences prefs = getSharedPreferences("NAME", MODE_PRIVATE);
        Boolean firstRun = prefs.getBoolean("firstRun", true);

        if(!firstRun){
            startActivity(new Intent(InitialActivity.this, MainActivity.class));
            finish();
        }
    }

    View.OnClickListener okOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(!txtUsername.getText().toString().equals("")){
                SharedPreferences.Editor editor = getSharedPreferences("NAME", MODE_PRIVATE).edit();
                editor.putBoolean("firstRun", false);
                editor.putString("name", txtUsername.getText().toString());
                editor.apply();

                startActivity(new Intent(InitialActivity.this, MainActivity.class));
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Please enter Your name...", Toast.LENGTH_LONG).show();
            }
        }
    };
}