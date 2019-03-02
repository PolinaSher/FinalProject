package com.example.afinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class New_York extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_york);

        Button btn = (Button)findViewById(R.id.button_back);

        btn.setOnClickListener( b -> {
            setResult(50,null);
        finish();});
    }
}