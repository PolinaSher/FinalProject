package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        Button buttonP = (Button)findViewById(R.id.button1);
        buttonP.setOnClickListener( b -> {

            Intent first_activity = new Intent(MainActivity.this, New_York.class);
            startActivityForResult(first_activity, 1);});
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