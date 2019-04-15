package com.example.finalproject_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class Article extends AppCompatActivity {
    WebView webView;
    Toolbar tool;
    Bundle extras;
    Bundle snip;
    List<String> newYorkArticles;
    ArrayAdapter<String> arrayAdapter;
    private static final String TAG = "Article Activity";

    private String string = "You will go back";
    private String snippet;
    private DBHelper myDb;
    private Cursor cursor;

    public void setArticles(List<String> articles) {
        this.articles = articles;
    }
    public static List<String> getArticles() {
        return articles;
    }

    private static List<String> articles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

       tool = (Toolbar)findViewById(R.id.toolbar_article);
        setSupportActionBar(tool);

        webView =(WebView) findViewById(R.id.articleURL);
        articles = new ArrayList<String>();

        myDb = new DBHelper(this);

        String newString;
        if (savedInstanceState == null) {
            extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("url");
                snippet= extras.getString("section_name");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("url");
        }
        webView.loadUrl(newString);
        Log.i("ffffff", snippet);

        //setContentView(webView);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menufor_article, menu);
        return true;}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                LayoutInflater inflater = LayoutInflater.from(Article.this);
                View subView = inflater.inflate(R.layout.custom_alert_box_view, null);
                final ImageView subImageView = (ImageView) subView.findViewById(R.id.imageView1);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Do you want to save this article?");
                builder.setView(subView);
                AlertDialog alertDialog = builder.create();
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id ) {

                        cursor = myDb.getAllData();

                        while(cursor.moveToNext()){
                            String temp = cursor.getString(1);
                            articles.add(temp);
                        }
                        setArticles(articles);
                        Intent toDataBase = new Intent(Article.this, DataBase.class);
                        startActivity(toDataBase);

                        myDb.addData(snippet);
                    }
                })
                        .setNegativeButton("no", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).show();
                return super.onOptionsItemSelected(item);





            case R.id.back:
                //can not see Snackbar
//                Snackbar sb = Snackbar.make(tool, string, Snackbar.LENGTH_LONG)
//                        .setAction("Go back", e -> finish());
//                sb.show();
                Intent goBack = new Intent(Article.this, New_York.class);
                startActivityForResult( goBack, 60);


        }
        return true;
    }

//    private class ChatAdapter extends ArrayAdapter<Article>implements ListAdapter {
//        public ChatAdapter(Context ctx) {
//            super(ctx, 0);
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = Article.this.getLayoutInflater();
//            View result;
//            Boolean send = getItem(position).getSend();
//
//
//            return result;
//        }
    }

