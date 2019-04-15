package com.example.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class NewsFeed extends AppCompatActivity {

    public static final String ITEM_SELECTED = "News";
    public static final String ITEM_POSITION = "POSITION";
    public static final String SEND_RECEIVE = "Title";
    public static final String URL = "url";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;

    MessageModel newMessage;
    private SharedPreferences sp;
    private ListView chatListView;
    private Button sendButton;
    private Button receiveButton;
    private EditText chatEditText;
    Cursor results;
    SQLiteDatabase db;
    private ChatAdapter adapter, adapter1;
    private List<MessageModel> chatList, savedList;
    String[] columns;
    private ProgressBar progressBar;
    private String searched, s;
    private String link, title, text;
    MyDatabaseOpenHelper dbOpener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        s="super";

        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        chatListView = findViewById(R.id.listview);

        receiveButton = findViewById(R.id.button6);
        chatEditText = (EditText) findViewById(R.id.editText);

        chatList = new ArrayList<>();
        savedList = new ArrayList<>();
        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);


        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tBar);


        sp = getSharedPreferences("FileName", Context.MODE_PRIVATE);
        String savedString = sp.getString("ReserveName", "");
        chatEditText.setText(savedString);



        receiveButton.setOnClickListener(b -> {
             searched = chatEditText.getText().toString();
          //  URLEncoder.encode( searched,"UTF-8");
             chatList.clear();
            ForecastQuery networkThread = new ForecastQuery();
            networkThread.execute("http://webhose.io/filterWebContent?token=ec43cbf6-a1e9-4a0b-ac09-8c4e7e9c6fd1&format=xml&sort=crawled&q="+searched);


            adapter = new ChatAdapter(this, chatList);
            chatListView.setAdapter(adapter);

/*            progressBar = (ProgressBar) findViewById(R.id.progressBar);
          progressBar.setVisibility(View.VISIBLE);  //show the progress bar   */

            //add to the database and get the new ID
/*            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, msg);
            newRowValues.put(MyDatabaseOpenHelper.COL_IS_SEND, Boolean.toString(false));
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

            //now you have the newId, you can create the Contact object
            MessageModel newMessage = new MessageModel(msg, false, newId);

            //add the new contact to the list:
            chatList.add(newMessage);

            chatEditText.setText("");
            adapter.notifyDataSetChanged();
            //updateChatMessage(false);
            */
          //  printCursor();
        });

        //printCursor();


        //chatListView.setOnItemClickListener( (list, item, position, id) -> {
       /* chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                // Do the onItemClick action*/
        chatListView.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString("buttonText", "Save");
            dataToPass.putString(ITEM_SELECTED, chatList.get(position).getMsg());

            dataToPass.putString(SEND_RECEIVE, chatList.get(position).getIsSend());

            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, chatList.get(position).getId());
            dataToPass.putString(URL, chatList.get(position).getUrl());

            if (isTablet) {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            } else //isPhone
            {
                Intent nextActivity = new Intent(NewsFeed.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }


        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.item1:
                Toast.makeText(this, s, Toast.LENGTH_LONG).show();
                break;


            case R.id.item2:

            Intent save=new Intent(this, SavedActivity.class);
            startActivity(save);

            case R.id.item3:
                Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
                Snackbar sb = Snackbar.make(tBar, "Go Back", Snackbar.LENGTH_LONG)
                        .setAction("Go Back", e -> {
                            finish();
                        });
                sb.show();
                break;

            case R.id.item4:
                //Show the toast immediately:
                View middle = getLayoutInflater().inflate(R.layout.dialog, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("About the app")
                        .setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // What to do on Accept
                              //  s = et.getText().toString();

                            }
                        })
                        .setNegativeButton("back", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // What to do on Cancel
                            }
                        }).setView(middle);

                builder.create().show();
                break;

        }
        return true;
    }

    protected void onPause() {
        super.onPause();

        //get an editor object
        SharedPreferences.Editor editor = sp.edit();

        //save what was typed under the name "ReserveName"
        String whatWasTyped = chatEditText.getText().toString();
        editor.putString("ReserveName", whatWasTyped);

        //write it to disk:
        editor.commit();
    }



    public static class ChatAdapter extends BaseAdapter {

        private List<MessageModel> msgList;
        private Context context;

        ChatAdapter(Context context, List<MessageModel> msgList) {
            this.context = context;
            this.msgList = msgList;
        }

        @Override
        public int getCount() {
            return msgList.size();
        }

        @Override
        public Object getItem(int position) {
            return msgList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return msgList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            final MessageModel model = msgList.get(position);

            view = LayoutInflater.from(context).inflate(R.layout.receive, null);


            TextView msgText = view.findViewById(R.id.msgText);
            msgText.setText(model.getIsSend());

            return view;
        }
    }

    //This function only gets called on the phone. The tablet never goes to a new activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MessageModel newMessage;
        if (requestCode == EMPTY_ACTIVITY) {
            if (resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                String news = data.getStringExtra(ITEM_SELECTED);
                String tl = data.getStringExtra(SEND_RECEIVE);
                String lk = data.getStringExtra(URL);


                long id = dbOpener.insertNewsFeed(news, lk, tl);
                if (id > 0) {
                    //now you have the newId, you can create the Contact object
                    newMessage = new MessageModel(news, lk, tl, id);
                    savedList.add(newMessage);
                    adapter1 = new ChatAdapter(this, savedList);
 /*                   chatListView.setAdapter(adapter1);

                    adapter1.notifyDataSetChanged();
*/
    /*            long id = data.getLongExtra(ITEM_ID, 0);
                int post = data.getIntExtra(ITEM_POSITION, 0);
                deleteMessageId(id, post);
                */
                }
            }
        }
    }






    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        String windSpeed, minTemp, maxTemp, currentTemp;
        Bitmap image;
        ImageView pic;
        TextView current;
        String parameter1, parameter2, parameter3;
        float aDouble;
        String iconName;

        @Override
        protected String doInBackground(String... urls) {

            try {
                String myUrl = urls[0];

                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
  /*          urlConnection.setReadTimeout(10000 );
                urlConnection.setConnectTimeout(15000 );
   */
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                InputStream inStream = urlConnection.getInputStream();


                //create a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");  //inStream comes from line 46


                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = xpp.getName(); //get the name of the starting tag: <tagName>
                        if (tagName.equals("url")) {
                            xpp.next();
                            link = xpp.getText();
  //                          publishProgress(30);
                        }
                       else if (tagName.equals("title")) {
                            xpp.next();
                           title = xpp.getText();
                           if(title==null || title.length()==0){
                               title="No title";
                           }
   //                         publishProgress(60);
                        }
                        else if (tagName.equals("text")) {
                            xpp.next();
                           text = xpp.getText();


                            //add to the database and get the new ID

   /*                         ContentValues newRowValues = new ContentValues();
                            //put string name in the NAME column:
                            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, text);
                            newRowValues.put(MyDatabaseOpenHelper.COL_IS_SEND, title);


                            //insert in the database:
                            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
*/
                            //now you have the newId, you can create the Contact object
                            MessageModel newMessage = new MessageModel(text, title, link);

                            //add the new contact to the list:
                            chatList.add(newMessage);

               //             chatEditText.setText("");
  //                          adapter.notifyDataSetChanged();
  //                          publishProgress(100);

                        }


                    }
                        xpp.next();


                }
            }catch (Exception ex) {
                Log.e("Crash!!", ex.getMessage());
            }
           /* catch (IOException e) {
                return "connection_error";
            } catch (XmlPullParserException e) {
                return "xml_error";
            } */
    //        publishProgress(100);
            //return type 3, which is String:
            return "Finished task";

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            Log.i("AsyncTask", "update:" + values[0]);

/*            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
*/
        }

        @Override
        protected void onPostExecute(String s) {

            adapter.notifyDataSetChanged();


//            progressBar.setVisibility(View.INVISIBLE);
        }



        /* public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
  */
    }
}
