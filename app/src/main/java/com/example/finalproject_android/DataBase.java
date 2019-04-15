package com.example.finalproject_android;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends AppCompatActivity {

    private ListView listview;
    private Button deletej;
    private Toolbar toolbar;
    private String string = "You will go back";
    Articles art;

    protected static final String ACTIVITY_NAME = "DataBase";
    protected SQLiteDatabase db;
    protected boolean FrameExists;
    protected Cursor cursor;
    List<String> newYorkArticles;//contain section_names and snippet
    ArrayAdapter<String> arrayAdapter;
    DBHelper dbhelp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_list);


        listview = (ListView) findViewById(R.id.listindatabase);

        deletej = (Button) findViewById(R.id.deletelayout);
        deletej.setOnClickListener(
                new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Integer deleteRow = dbhelp.deleteOne(listview.toString());
                if (deleteRow > 0)
                    Toast.makeText(DataBase.this, "Article deleted", Toast.LENGTH_LONG).show();

                else
                    Toast.makeText(DataBase.this, "Article not deleted", Toast.LENGTH_LONG).show();


            }
        });
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Article.getArticles());
        listview.setAdapter(arrayAdapter);
        dbhelp = new DBHelper(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menufor_database, menu);
        return true;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id){
               Integer deletedRows = dbhelp.deleteOne(arrayAdapter.toString());
                if (deletedRows > 0)
                    Toast.makeText(DataBase.this, "Article deleted", Toast.LENGTH_LONG).show();

                else
                    Toast.makeText(DataBase.this, "Article not deleted", Toast.LENGTH_LONG).show();

            }
                });
                return super.onOptionsItemSelected(item);


            case R.id.back:
                Snackbar sb = Snackbar.make(toolbar, string, Snackbar.LENGTH_LONG)
                        .setAction("Go back", e -> finish());
                sb.show();

//                Intent goBack = new Intent(DataBase.this, Article.class);
//                startActivity( goBack);


    }
    return true;}

}


//   // public void DeleteData() {
////        deletej.setOnClickListener(
////                new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Integer deletedRows = dbhelp.deleteOne(null);
////                    if (deletedRows > 0)
////                        Toast.makeText(DataBase.this, "Article deleted", Toast.LENGTH_LONG).show();
////
////                    else
////                            Toast.makeText(DataBase.this, "Article not deleted", Toast.LENGTH_LONG).show();
////                    }
////                }
////
////        );
////    }





