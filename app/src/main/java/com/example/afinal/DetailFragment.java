package com.example.afinal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long newId, id;
    private int post;
    SQLiteDatabase db;
    private String msg,link,title;
    public static List<MessageModel> savedList;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        savedList=new ArrayList<>();
        dataFromActivity = getArguments();
 //       id = dataFromActivity.getLong(NewsFeed.ITEM_ID);
        post = dataFromActivity.getInt(NewsFeed.ITEM_POSITION);
        msg=dataFromActivity.getString(NewsFeed.ITEM_SELECTED);
        title=dataFromActivity.getString(NewsFeed.SEND_RECEIVE);
        link=dataFromActivity.getString(NewsFeed.URL);

        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_detail, container, false);

        //show the message
        TextView message = (TextView) result.findViewById(R.id.message);
        message.setText(msg);

        TextView send = (TextView) result.findViewById(R.id.send);
        send.setText(title);

        //show the id:
        TextView url = (TextView) result.findViewById(R.id.id);
        url.setText(link);

        // get the delete button, and add a click listener:
      Button saveButton = (Button) result.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(clk -> {

            if (isTablet) { //both the list and details are on the screen:
                NewsFeed parent = (NewsFeed) getActivity();

                MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(parent);
                db = dbOpener.getWritableDatabase();
                //add to the database and get the new ID
           ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, msg);
            newRowValues.put(MyDatabaseOpenHelper.COL_IS_SEND, title);
            newRowValues.put(MyDatabaseOpenHelper.COL_URL, link);
            //insert in the database:
           newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

            //now you have the newId, you can create the Contact object
           MessageModel newMessage = new  MessageModel(msg, link, title, newId);

            //add the new contact to the list:
            savedList.add(newMessage);


               // parent.deleteMessageId(id, post); //this deletes the item and updates the list


                //now remove the fragment since you deleted it
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();

            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                EmptyActivity parent = (EmptyActivity) getActivity();

                MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(parent);
                db = dbOpener.getWritableDatabase();
                //add to the database and get the new ID
                ContentValues newRowValues = new ContentValues();
                //put string name in the NAME column:
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, msg);
                newRowValues.put(MyDatabaseOpenHelper.COL_IS_SEND, title);
                newRowValues.put(MyDatabaseOpenHelper.COL_URL, link);
                //insert in the database:
                newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

                //now you have the newId, you can create the Contact object
                MessageModel newMessage = new  MessageModel(msg, link, title, newId);

                //add the new contact to the list:
                savedList.add(newMessage);

                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(NewsFeed.ITEM_SELECTED, msg);
                backToFragmentExample.putExtra(NewsFeed.SEND_RECEIVE, title);
                backToFragmentExample.putExtra(NewsFeed.URL, link);
                backToFragmentExample.putExtra(NewsFeed.ITEM_ID, newId);


                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });

        return result;
    }
}
