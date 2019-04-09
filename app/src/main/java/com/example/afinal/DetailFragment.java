package com.example.afinal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class DetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;
    private int post;

    public void setTablet(boolean tablet) {
        isTablet = tablet;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(NewsFeed.ITEM_ID);
        post = dataFromActivity.getInt(NewsFeed.ITEM_POSITION);
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_detail, container, false);

        //show the message
        TextView message = (TextView) result.findViewById(R.id.message);
        message.setText(dataFromActivity.getString(NewsFeed.ITEM_SELECTED));

        TextView send = (TextView) result.findViewById(R.id.send);
        send.setText(dataFromActivity.getString(NewsFeed.SEND_RECEIVE));
        //show the id:
        TextView idView = (TextView) result.findViewById(R.id.id);
        idView.setText("ID=" + id);

        // get the delete button, and add a click listener:
        Button saveButton = (Button) result.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(clk -> {

            if (isTablet) { //both the list and details are on the screen:
                NewsFeed parent = (NewsFeed) getActivity();
                parent.deleteMessageId(id, post); //this deletes the item and updates the list


                //now remove the fragment since you deleted it
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();

            }
            //for Phone:
            else //You are only looking at the details, you need to go back to the previous list page
            {
                EmptyActivity parent = (EmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra(NewsFeed.ITEM_ID, id);
                backToFragmentExample.putExtra(NewsFeed.ITEM_POSITION, post);

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        return result;
    }
}
