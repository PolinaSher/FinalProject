package com.example.finalproject_android;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class New_York extends AppCompatActivity {
    private ListView listview;
    private ProgressBar progressBar;
    private int progressStatus =0;
    private Handler handler = new Handler();

    private Toolbar toolbarjava;
    String forTost= "Please enter a search criteria";
    final Context contextForAlert = this;
    private EditText editText;
    private Button button;
    protected SQLiteDatabase db;
    ArrayList<String> articles = new ArrayList<>();
    protected Cursor cursor;
    private String search;
    public String text;
    public String title;
    List<String> newYorkArticles;//contain section_names and snippet
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String>snippetarray;

    List<String> urlArray;
    DBHelper dbhelper;

    String urlArt = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=election&api-key=8071whZnMJTPcYntKxLRl8MGozpXYkJr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new__york);

        Intent fromMain = getIntent();

        progressBar= (ProgressBar) findViewById(R.id.progressBar);
        //loadText=(TextView)findViewById(R.id.loadTextView);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus <100){
                    progressStatus++;
                    android.os.SystemClock.sleep(50);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                         //   progressBar.setVisibility(View.VISIBLE);
                           // progressBar.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

        }).start();


       toolbarjava = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbarjava);


        newYorkArticles = new ArrayList<String>();
        urlArray = new ArrayList<String>();
        listview = (ListView)findViewById(R.id.listtype);
        editText = (EditText)findViewById(R.id.search_box);
        button = (Button)findViewById(R.id.search_text);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, newYorkArticles );

        listview.setAdapter(arrayAdapter);

        Articles networkThread = new Articles();
        networkThread.execute();

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                Articles networkThread = new Articles();
                networkThread.execute();
                String edit = editText.getText().toString();
                if (edit.matches("")) {
                    Toast.makeText(New_York.this, forTost, Toast.LENGTH_SHORT).show();
                    return;

                }


            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){


            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){


                String itemChosen = (String) adapter.getItemAtPosition(position);  //this is section_name

                Intent intent = new Intent(New_York.this, Article.class);

                intent.putExtra("url", urlArray.get(position));
                intent.putExtra("section_name", newYorkArticles.get(position));
                startActivity(intent);

            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.overflowmenu, menu);
        return true;}

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.about:
                LayoutInflater inflater = LayoutInflater.from(New_York.this);
                View subView = inflater.inflate(R.layout.about_alert_dialogbox, null);
                final ImageView subImageView = (ImageView) subView.findViewById(R.id.imageView1);
                final ImageView sub1 = (ImageView) subView.findViewById(R.id.delete);
                final ImageView sub2 = (ImageView) subView.findViewById(R.id.save);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage("This project was creared by Polina Sherriuble.");
                builder.setView(subView);

                AlertDialog alertDialog = builder.create();
                builder.setNegativeButton("Close dialog box", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).show();

                        return super.onOptionsItemSelected(item);

            case R.id.bookmark:
                Intent gotodatabase = new Intent(New_York.this, DataBase.class);
                startActivity(gotodatabase);
                    }
        return true;
    }

    private class Articles extends AsyncTask<String, Integer, String> {

        private String source;
        private String paragraph;
        String snippet;
        Bitmap picBit;

        @Override
        protected String doInBackground(String... params) {

            try {
                //create the network connection:
                URL url = new URL(urlArt);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();


                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                StringBuffer stringBuffer = new StringBuffer();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line + "\n");
                }
                reader.close();
                inStream.close();
                urlConnection.disconnect();

                String result = stringBuffer.toString();
                JSONObject jObject = new JSONObject(result);
                newYorkArticles.clear();
                String response = jObject.getString("response"); //json: we took object {}response...
                JSONObject temp = new JSONObject(response);
                JSONArray ja = jObject.getJSONObject("response").getJSONArray("docs");//json: we took array [] docs
                String edit = editText.getText().toString();
                Log.d("jalength",ja.length()+"");

                for(int i = 0; i < ja.length(); i ++){
                    //display all available section_names and snippets from json.This is string.
                    text = ja.getJSONObject(i).getString("section_name") + "\n" + ja.getJSONObject(i).getString("snippet");
                  // snippet = ja.getJSONObject(i).getString("snippet");

//                    if (text==null) {
//                        Log.d("text","text is null");
//                    }
                    //Compare string text from json with input from client (EditText)edit

                    //newYorkArticles.add(text);

                    if (text.contains(edit)) {
                        newYorkArticles.add(text);
                        urlArray.add(ja.getJSONObject(i).getString("web_url"));
                    }

//                    if (snippet==null) {
//                        Log.d("snippet","snippet is null");
//                    }
/*
                    if(snippet.contains(edit)){
                        snippetarray.add(snippet);

                        //snippet = ja.getJSONObject(i).getString("snippet");//must show snippet
                    Log.d("json",snippet = ja.getJSONObject(i).getString("snippet"));//must show snippet)
                        dbhelper.addData(snippet);

                    }

                    Log.d("json",snippet = ja.getJSONObject(i).getString("snippet"));//must show snippet)
*/

                }

                return text;



            } catch (Exception ex) {
                Log.e("Crash!!", ex.getMessage());
            }

            //return type 3, which is String:
            return "Finished task";
        }


          @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            arrayAdapter.notifyDataSetChanged();
            Log.i("arrayAdapter", newYorkArticles.size() + "");

        }
        @Override
        protected void onPreExecute(){

        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setVisibility(values[0]);
            Log.i("PROGRESS BAR", "update:" + values[0]);

        }
    }
}
