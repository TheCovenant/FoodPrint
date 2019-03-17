package com.example.hackrpi.foodprint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    public void toSearch(View view)
    {
        Intent intent = new Intent(this, searchResult.class);
        startActivity(intent);

    }
}
