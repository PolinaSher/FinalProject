package com.example.finalproject_android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject_android.R;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DepartureArrivals extends AppCompatActivity {

    private ArrayList<String> listCode;

    ArrayAdapter<String> adapter;

    private ListView list;

    Button search;
    EditText code;
    TextView helpText;
    Button help;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrivlas_departure);

        search = (Button) findViewById(R.id.search);

        code = (EditText) findViewById(R.id.searchCode);




        listCode = new ArrayList<>(); //init to something??

        help = (Button) findViewById(R.id.needHelp);

        help.setOnClickListener(e -> dialog());


        list = (ListView) findViewById(R.id.deparAndArrival);



        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listCode);




        list.setAdapter(adapter);





        search.setOnClickListener(view -> {




            URL url = NetworkUtils.generateURL(code.getText().toString());
            new TrackerQuery().execute(url.toString());



        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentCode = parent.getItemAtPosition(position).toString();

                Intent description = new Intent(DepartureArrivals.this, AttributeOfTheFlight.class);

                description.putExtra("Code", currentCode);

                startActivity(description);
                Toast.makeText(getApplicationContext(),parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
            }
        });


    }

    public void dialog(){
        com.example.youtube_practice.CustomDialog dial = new com.example.youtube_practice.CustomDialog();

        dial.show(getSupportFragmentManager(), "dialog");
    }




    private class TrackerQuery extends AsyncTask<String, Void, Void> {

        String data = "";
        String resultString = "nothing";

        String flightsData = "";

        String check = "";

        @Override
        protected Void doInBackground(String... params) {


            try {

                String myUrl = params[0];
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
                StringBuilder sb = new StringBuilder();


                while ((data = reader.readLine()) != null)
                {
                    sb.append(data + "\n");
                }
                resultString = sb.toString();



                JSONArray arr = new JSONArray(resultString);

                for(int i = 0; i < arr.length(); i++) {

                    JSONObject anObject = arr.getJSONObject(i);
                    JSONObject geog = anObject.getJSONObject("arrival");
                    flightsData = geog.getString("iataCode");

                    check = check + flightsData;
                    listCode.add(flightsData);

                }



            }catch (Exception e){
                e.getStackTrace();
            }


            return null;
        }



//        @Override
//        protected void onPostExecute(Void avoid){
//            dataCheck.setText(check);
//
//        }
    }





}
