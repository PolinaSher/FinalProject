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
    private int post;
    private long id;
    SQLiteDatabase db;
    private String buttonText,msg, link, title;
    MessageModel newMessage;
    MyDatabaseOpenHelper dbOpener;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbOpener = new MyDatabaseOpenHelper(getContext());
        db = dbOpener.getWritableDatabase();

        dataFromActivity = getArguments();

        buttonText=dataFromActivity.getString("buttonText");
        id = dataFromActivity.getLong(NewsFeed.ITEM_ID);
        post = dataFromActivity.getInt(NewsFeed.ITEM_POSITION);
        msg = dataFromActivity.getString(NewsFeed.ITEM_SELECTED);
        title = dataFromActivity.getString(NewsFeed.SEND_RECEIVE);
        link = dataFromActivity.getString(NewsFeed.URL);

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
        Button button = (Button) result.findViewById(R.id.saveButton);
        if(buttonText.equals("Save")){
            button.setText(buttonText);
        }
        if(buttonText.equals("Delete")){
            button.setText(buttonText);
        }

        button.setOnClickListener(clk -> {


            //for Phone:
            //You are only looking at the details, you need to go back to the previous list page

                EmptyActivity parent = (EmptyActivity) getActivity();


                Intent backToFragmentExample = new Intent();

                backToFragmentExample.putExtra(NewsFeed.ITEM_SELECTED, msg);
                backToFragmentExample.putExtra(NewsFeed.SEND_RECEIVE, title);
                backToFragmentExample.putExtra(NewsFeed.URL, link);

            backToFragmentExample.putExtra(NewsFeed.ITEM_ID, id);
            backToFragmentExample.putExtra(NewsFeed.ITEM_POSITION, post);

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back

        });

        return result;
    }
}
