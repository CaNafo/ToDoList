package com.gigantdevs.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int KEY_1 = 1;
    public static final int KEY_2 = 2;

    private LinearLayout categoryLayout;
    private ImageView imgBackground;
    private LinearLayout addCategoryLayout;
    private NestedScrollView categoryScroll;
    private Button btnNewCategory;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private ArrayList<String> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("categories");

        init();
    }

    private void init(){

        categoryLayout = findViewById(R.id.categoryLayout);
        imgBackground = findViewById(R.id.imgMain);
        addCategoryLayout = findViewById(R.id.addCategoryLayout);
        categoryScroll = findViewById(R.id.categoryScroll);
        btnNewCategory = findViewById(R.id.btnAddCategory);

        btnNewCategory.setOnClickListener(addCategoryOnClickListener);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) categoryScroll.getLayoutParams();
        // Changes the height and width to the specified *pixels*
        params.height = (getWindowHeight()-(getWindowHeight()/2));
        categoryScroll.setLayoutParams(params);

        imgBackground.setMaxHeight(getWindowHeight()/6);
        imgBackground.setMinimumWidth(getWindowWidth());

        myRef.addValueEventListener(eventListener);
    }

    View.OnClickListener addCategoryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            EditText newCategory = findViewById(R.id.newCategoryName);
            Boolean alreadyExists = false;

            for (String cat : categories
                 ) {
                if(cat.equals(newCategory.getText().toString().toLowerCase()) || newCategory.getText().toString().equals("")){
                    alreadyExists = true;
                    return;
                }
            }

            if(!alreadyExists){
                myRef.push().setValue(newCategory.getText().toString());
                newCategory.setText("");
                Toast.makeText(getApplicationContext(),"Successfully added!",Toast.LENGTH_LONG).show();
            }
        }
    };

    View.OnClickListener categoryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this,CategoryActivity.class);

            Bundle b = new Bundle();
            b.putString("categoryName",view.getTag().toString());

            intent.putExtras(b);

            myRef.removeEventListener(eventListener);
            overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            startActivity(intent);
        }
    };

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            categoryLayout.removeAllViews();
            categories.removeAll(categories);

            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                if(ds.exists()){

                    String name = (String)ds.getValue();

                    LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)addCategoryLayout.getLayoutParams();
                    layoutParams.setMargins(15,30,15,0);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setBackgroundResource(R.drawable.category_shape);

                    Button btnTag = new Button(getApplicationContext());
                    btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
                    btnTag.setText(name);
                    btnTag.setBackgroundColor(Color.TRANSPARENT);
                    btnTag.setTag(name);
                    btnTag.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
                    btnTag.setOnClickListener(categoryOnClickListener);
                    btnTag.setGravity(Gravity.START);
                    btnTag.setAllCaps(false);
                    btnTag.setTextSize(20);
                    linearLayout.addView(btnTag);

                    Button btnDelete = new Button(getApplicationContext());
                    btnDelete.setLayoutParams(new LinearLayout.LayoutParams(getWindowWidth()/15, getWindowWidth()/15));
                    btnDelete.setBackgroundResource(R.drawable.delete);
                    btnDelete.setTag(R.string.value1,ds.getKey());
                    btnDelete.setTag(R.string.value2,ds.getValue().toString());
                    btnDelete.setOnClickListener(deleteOnClickListener);
                    linearLayout.addView(btnDelete);

                    categoryLayout.addView(linearLayout);

                    categories.add(name.toLowerCase());
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };

    View.OnClickListener deleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CustomYesNoDialogClass customYesNoDialogClass = new CustomYesNoDialogClass(MainActivity.this,"categories",view.getTag(R.string.value1).toString(),view.getTag(R.string.value2).toString());
            customYesNoDialogClass.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customYesNoDialogClass.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
            customYesNoDialogClass.show();
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

    @Override
    protected void onResume() {
        super.onResume();
        myRef.addValueEventListener(eventListener);

        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }
}