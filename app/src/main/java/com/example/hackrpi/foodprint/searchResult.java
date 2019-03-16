package com.example.hackrpi.foodprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class searchResult extends AppCompatActivity {

    RecyclerView resultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        resultList = findViewById(R.id.resultList);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        DishRecyclerAdapter dishAdapter = new DishRecyclerAdapter();
        resultList.setAdapter(dishAdapter);

    }
}
