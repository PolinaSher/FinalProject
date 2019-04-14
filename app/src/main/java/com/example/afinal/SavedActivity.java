package com.example.afinal;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SavedActivity extends AppCompatActivity {
    SQLiteDatabase db;
    MyDatabaseOpenHelper dbOpener;
    private NewsFeed.ChatAdapter adapter1;
    private List<MessageModel>  savedList;
    Cursor results;
    private ListView savedListView;
    public static final int EMPTY_ACTIVITY = 345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        savedList=new ArrayList<>();

        savedListView = findViewById(R.id.listview1);
        dbOpener = new MyDatabaseOpenHelper(this);

        db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_URL,MyDatabaseOpenHelper.COL_IS_SEND};
        results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int urlColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_URL);
        int sendColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_IS_SEND);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String message = results.getString(messageColumnIndex);
            String link = results.getString(urlColumnIndex);
            String isSend = results.getString(sendColumnIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
           savedList.add(new MessageModel(message,link, isSend, id));
        }


        adapter1 = new NewsFeed.ChatAdapter(this, savedList);
        savedListView.setAdapter(adapter1);

        savedListView.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(NewsFeed.ITEM_SELECTED, savedList.get(position).getMsg());

            dataToPass.putString(NewsFeed.SEND_RECEIVE, savedList.get(position).getIsSend());

            dataToPass.putInt(NewsFeed.ITEM_POSITION, position);
            dataToPass.putLong(NewsFeed.ITEM_ID, savedList.get(position).getId());
            dataToPass.putString(NewsFeed.URL, savedList.get(position).getUrl());


                Intent nextActivity = new Intent(SavedActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition



        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MessageModel newMessage;
        if (requestCode == EMPTY_ACTIVITY) {
            if (resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                String news = data.getStringExtra(NewsFeed.ITEM_SELECTED);
                String tl = data.getStringExtra(NewsFeed.SEND_RECEIVE);
                String lk = data.getStringExtra(NewsFeed.URL);
                long id = data.getLongExtra(NewsFeed.ITEM_ID, 0);
                int post = data.getIntExtra(NewsFeed.ITEM_POSITION, 0);

                deleteMessageId(id, post);

                }
            }
        }

    public void deleteMessageId(long id, int position) {
        int rowsEffected = db.delete(MyDatabaseOpenHelper.TABLE_NAME, MyDatabaseOpenHelper.COL_ID + "=?", new String[]{Long.toString(id)});
        if (rowsEffected > 0) {
            Log.i("Delete this message:", " id=" + id);
            savedList.remove(position);
            adapter1.notifyDataSetChanged();
        }
    }
}


