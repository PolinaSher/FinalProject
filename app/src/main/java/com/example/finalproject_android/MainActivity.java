package com.example.finalproject_android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button buttonNewYork = (Button)findViewById(R.id.button1);
        buttonNewYork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), New_York.class);
                view.getContext().startActivity(intent);}
        });


        Button buttonNews = (Button)findViewById(R.id.button4);
        buttonNewYork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewsFeed.class);
                view.getContext().startActivity(intent);}
        });
            }
            @Override
            protected void onPause(){
                super.onPause();
            }
            @Override
            protected void onDestroy(){
                super.onDestroy();
            }
            @Override
            protected void onStop(){
                super.onStop();
            }
            @Override
            protected void onResume(){
                super.onResume();
            }
            @Override
            protected void onStart(){
                super.onStart();
            }

        }

