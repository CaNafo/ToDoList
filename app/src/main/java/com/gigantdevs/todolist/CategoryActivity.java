package com.gigantdevs.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gigantdevs.todolist.models.TodoItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CategoryActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private NestedScrollView toDoScroll;
    private LinearLayout toDoLayout;
    private LinearLayout addToDoLayout;
    private EditText newToDo;
    private Button btnAdd;
    private TextView txtTittle;

    private String ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        overridePendingTransition(R.anim.fadein,R.anim.fadeout);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(getIntent().getStringExtra("categoryName"));
        myRef.addValueEventListener(eventListener);

        init();
    }

    private void init(){
        toDoScroll = findViewById(R.id.toDoScroll);
        newToDo = findViewById(R.id.newToDo);
        toDoLayout = findViewById(R.id.toDoLayout);
        addToDoLayout = findViewById(R.id.addToDoLayout);
        txtTittle = findViewById(R.id.txtTittle);
        btnAdd = findViewById(R.id.btnAddToDo);
        btnAdd.setOnClickListener(onAddOnClickListener);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) toDoScroll.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = (getWindowHeight()-(getWindowHeight()/2));
        toDoScroll.setLayoutParams(params);

        ref = getIntent().getStringExtra("categoryName");
        txtTittle.setText(ref + " ToDo List");
    }

    View.OnClickListener onAddOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(!newToDo.getText().toString().equals("")){
                TodoItem todoItem = new TodoItem();

                SharedPreferences prefs = getSharedPreferences("NAME", MODE_PRIVATE);
                String name = prefs.getString("name", "No name defined");

                todoItem.setCreator(name);
                todoItem.setDate(Calendar.getInstance().getTime().toString());
                todoItem.setText(newToDo.getText().toString());
                todoItem.setLastModificator(name);

                myRef.push().setValue(todoItem);
                newToDo.setText("");
                Toast.makeText(getApplicationContext(),"Successfully added!",Toast.LENGTH_LONG).show();
            }
        }
    };

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            toDoLayout.removeAllViews();
            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                if(ds.exists()){
                    TodoItem todoItem = ds.getValue(TodoItem.class);

                    /*LinearLayout outLinearLayout = new LinearLayout(getApplicationContext());
                    outLinearLayout.setOrientation(LinearLayout.VERTICAL);
                    LinearLayout.LayoutParams outLinearLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    outLinearLayoutParams.setMargins(10,40,10,0);
                    outLinearLayout.setLayoutParams(outLinearLayoutParams);
                    outLinearLayout.setBackgroundResource(R.drawable.item_shape);*/

                    LinearLayout btnLinearLayout = new LinearLayout(getApplicationContext());
                    btnLinearLayout.setGravity(Gravity.END);
                    btnLinearLayout.setOrientation(LinearLayout.HORIZONTAL);


                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) addToDoLayout.getLayoutParams();
                    layoutParams.setMargins(5,15,5,0);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setGravity(Gravity.CENTER);

                    TextView todoTittle = new TextView(getApplicationContext());
                    todoTittle.setText(todoItem.getText());
                    todoTittle.setTextSize(18);
                    todoTittle.setTextColor(Color.BLACK);
                    todoTittle.setTypeface(Typeface.create("sans-serif-thin", Typeface.NORMAL));
                    LinearLayout.LayoutParams paramsTittle = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                    todoTittle.setLayoutParams(paramsTittle);
                    todoTittle.setTag(R.string.value1,todoItem.getText());
                    todoTittle.setBackgroundResource(R.drawable.item_shape);
                    todoTittle.setTag(R.string.value2,ds.getKey());
                    todoTittle.setTag(R.string.value3,ref);
                    todoTittle.setOnClickListener(editOnClickListener);

                    Button btnDone= new Button(getApplicationContext());
                    btnDone.setLayoutParams(new LinearLayout.LayoutParams(getWindowWidth()/10, getWindowWidth()/10));
                    if(todoItem.isDone()){
                        btnDone.setBackgroundResource(R.drawable.done);
                        btnDone.setTag(R.string.value1,true);
                    }
                    else{
                        btnDone.setBackgroundResource(R.drawable.notdone);
                        btnDone.setTag(R.string.value1,false);
                    }
                    btnDone.setTag(R.string.value2,ds.getKey());
                    btnDone.setOnClickListener(doneOnClickListener);

                    Button btnInfo = new Button(getApplicationContext());
                    btnInfo.setBackgroundResource(R.drawable.info);
                    btnInfo.setOnClickListener(infoOnClickListener);
                    btnInfo.setLayoutParams(new LinearLayout.LayoutParams(getWindowWidth()/15, getWindowWidth()/15));
                    btnInfo.setTag(R.string.value1,todoItem.getCreator());
                    btnInfo.setTag(R.string.value2,todoItem.getLastModificator());
                    btnInfo.setTag(R.string.value3,todoItem.getDate());
                    btnLinearLayout.addView(btnInfo);

                    Button btnEdit = new Button(getApplicationContext());
                    btnEdit.setBackgroundResource(R.drawable.ic_baseline_edit_24);
                    btnEdit.setOnClickListener(infoOnClickListener);
                    btnEdit.setLayoutParams(new LinearLayout.LayoutParams(getWindowWidth()/15, getWindowWidth()/15));
                    btnEdit.setTag(R.string.value1,todoItem.getText());
                    btnEdit.setTag(R.string.value2,ds.getKey());
                    btnEdit.setTag(R.string.value3,ref);
                    btnEdit.setOnClickListener(editOnClickListener);
                    btnLinearLayout.addView(btnEdit);

                    Button btnDelete = new Button(getApplicationContext());
                    btnDelete.setLayoutParams(new LinearLayout.LayoutParams(getWindowWidth()/15, getWindowWidth()/15));
                    btnDelete.setBackgroundResource(R.drawable.delete);
                    btnDelete.setTag(ds.getKey());
                    btnDelete.setOnClickListener(deleteOnClickListener);
                    btnLinearLayout.addView(btnDelete);

                    linearLayout.addView(btnDone);
                    linearLayout.addView(todoTittle);
                    linearLayout.addView(btnLinearLayout);

                    toDoLayout.addView(linearLayout);

                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

    View.OnClickListener deleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            myRef.child(view.getTag().toString()).removeValue();
        }
    };

    View.OnClickListener doneOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if((boolean)view.getTag(R.string.value1)){
                myRef.child(view.getTag(R.string.value2).toString()).child("done").setValue(false);
            }else{
                myRef.child(view.getTag(R.string.value2).toString()).child("done").setValue(true);
            }
        }
    };

    View.OnClickListener infoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomDialogClass customDialogClass = new CustomDialogClass(CategoryActivity.this,view.getTag(R.string.value1).toString(),view.getTag(R.string.value2).toString(),view.getTag(R.string.value3).toString());
            customDialogClass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialogClass.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
            customDialogClass.show();
        }
    };

    View.OnClickListener editOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomEditDialogClass customEditDialogClass = new CustomEditDialogClass(CategoryActivity.this, view.getTag(R.string.value3).toString(), view.getTag(R.string.value2).toString(), view.getTag(R.string.value1).toString());
            customEditDialogClass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customEditDialogClass.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
            customEditDialogClass.show();
        }
    };

    private int getWindowHeight(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.heightPixels;
    }

    private int getWindowWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return displayMetrics.widthPixels;
    }
}