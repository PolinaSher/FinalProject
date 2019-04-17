package com.example.finalproject_android;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


 public class Flight extends Activity {

    private Button start;
    private Button info;

    private Button saved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);

        info = (Button) findViewById(R.id.helpInfo);

        saved = (Button) findViewById(R.id.savedFlights);

        //About button, provide general information
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(info, "Dennnis Kravchenko - Activity version 1.0 \n Click Start button to start ", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });



        start.setOnClickListener(e -> {
            startActivity(new Intent(this, DepartureArrivals.class));
            Toast.makeText(getApplicationContext(),"Flight Tracker started", Toast.LENGTH_LONG).show();
        });


        saved.setOnClickListener(e -> startActivity(new Intent(Flight.this, DepartureArrivals.class)));




    }


}
