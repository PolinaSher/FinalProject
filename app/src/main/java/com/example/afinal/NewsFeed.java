package com.example.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private SharedPreferences sp;
    private ListView chatListView;
    private Button sendButton;
    private Button receiveButton;
    private EditText chatEditText;
    Cursor results;
    SQLiteDatabase db;
    private ChatAdapter adapter;
    private List<MessageModel> chatList;
    String[] columns;
    private ProgressBar progressBar;
    private String searched;
    private String link, title, text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);


        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        chatListView = findViewById(R.id.listview);

        receiveButton = findViewById(R.id.button6);
        chatEditText = (EditText) findViewById(R.id.editText);

        chatList = new ArrayList<>();
        //get a database:
   /*     MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_IS_SEND};
        results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int sendColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_IS_SEND);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String message = results.getString(messageColumnIndex);
            String isSend = results.getString(sendColumnIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            chatList.add(new MessageModel(message, isSend, id));
        }
*/


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

    public class MessageModel {
        private String msg;
        private String isSend;
        private String url;
        private long id;

        public MessageModel(String msg, String isSend, long id) {
            this.msg = msg;
            this.isSend = isSend;
            this.id = id;
        }

        public MessageModel(String msg, String isSend, String url) {
            this.msg = msg;
            this.isSend = isSend;
            this.url = url;
        }


        public String getMsg() {
            return msg;
        }

        public String getIsSend() {
            return isSend;
        }

        public long getId() {
            return id;
        }

        public String getUrl() {
            return url;
        }

    }

    protected class ChatAdapter extends BaseAdapter {

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
        if (requestCode == EMPTY_ACTIVITY) {
            if (resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra(ITEM_ID, 0);
                int post = data.getIntExtra(ITEM_POSITION, 0);
                deleteMessageId(id, post);
            }
        }
    }

    public void deleteMessageId(long id, int position) {
        int rowsEffected = db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "=?", new String[]{Long.toString(id)});
        if (rowsEffected > 0) {
            Log.i("Delete this message:", " id=" + id);
            chatList.remove(position);
            adapter.notifyDataSetChanged();
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
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
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


    /*                        parameter1 = xpp.getAttributeValue(null, "value");
                            Log.e("AsyncTask", "Found parameter value: " + parameter1);
                            publishProgress(25); //tell android to call onProgressUpdate with 1 as parameter

                            parameter2 = xpp.getAttributeValue(null, "min");
                            Log.e("AsyncTask", "Found parameter min: " + parameter2);
                            publishProgress(50); //tell android to call onProgressUpdate with 1 as parameter

                            parameter3 = xpp.getAttributeValue(null, "max");
                            Log.e("AsyncTask", "Found parameter max: " + parameter3);
                            publishProgress(75); //tell android to call onProgressUpdate with 1 as parameter
                        } else if (tagName.equals("weather")) {
                            iconName = xpp.getAttributeValue(null, "icon");
                            Log.e("AsyncTask", "Found parameter icon: " + iconName);


                            if (fileExistance(iconName + ".png")) {
                                Log.i(iconName + ".png", "I found the image locally");
                                FileInputStream fis = null;
                                try {
                                    fis = openFileInput(iconName + ".png");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                image = BitmapFactory.decodeStream(fis);


                            } else {
                                Log.i(iconName + ".png", "I need to download it");
                                image = null;
                                URL url2 = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                                HttpURLConnection connection = (HttpURLConnection) url2.openConnection();

                                connection.connect();

                                int responseCode = connection.getResponseCode();
                                if (responseCode == 200) {
                                    image = BitmapFactory.decodeStream(connection.getInputStream());
                                }

                                publishProgress(100); //tell android to call onProgressUpdate with 2 as parameter

                                // Bitmap image  = HTTPUtils.getImage("http://openweathermap.org/img/w/" +iconName+".png");
                                FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();


                            }
                        }

                    }

                    xpp.next(); //advance to next XML event
                }*/

                }
            }catch (Exception ex) {
                Log.e("Crash!!", ex.getMessage());
            }
           /* catch (IOException e) {
                return "connection_error";
            } catch (XmlPullParserException e) {
                return "xml_error";
            } */

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




//            progressBar.setVisibility(View.INVISIBLE);
        }



        /* public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
  */
    }
}
