package com.example.finalproject_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AttributeOfTheFlight extends AppCompatActivity {

    Button save;

    String recievedCode;

    TextView code;

    TextView check;

    Double altitude = 0.0;
    String stat = "";


    Button backToMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_flight_attributes);

        backToMain = (Button) findViewById(R.id.back);

        backToMain.setOnClickListener(e -> startActivity(new Intent(AttributeOfTheFlight.this, MainActivity.class)));


        //received Code from previous activity
        recievedCode = getIntent().getExtras().get("Code").toString();


        //put Code into TextView
        code = (TextView) findViewById(R.id.codeDescription);
        code.setText(recievedCode);


        //grab data about received Code
        URL url = NetworkUtils.generateURL(recievedCode);
        new TrackerQuery().execute(url.toString());






        //Trying to put data about Code
        check = (TextView) findViewById(R.id.description);
        check.setText(stat);




        //Save data for database
        save = (Button) findViewById(R.id.save);
    }


    private class TrackerQuery extends AsyncTask<String, Void, Void> {

        String data = "";
        String resultString = "nothing";





        @Override
        protected Void doInBackground(String... params) {


            try {

                String myUrl = params[0];
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
                StringBuilder sb = new StringBuilder();


                while ((data = reader.readLine()) != null) {
                    sb.append(data + "\n");
                }
                resultString = sb.toString();


                JSONArray arr = new JSONArray(resultString);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject anObject = arr.getJSONObject(i);
                    JSONObject geog = anObject.getJSONObject("geography");

                    stat = anObject.getString("status");


                    altitude = geog.getDouble("altitude");

                }


            } catch (Exception e) {
                e.getStackTrace();
            }


            return null;
        }


    }
}
