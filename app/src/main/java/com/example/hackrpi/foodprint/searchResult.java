package com.example.hackrpi.foodprint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class searchResult extends AppCompatActivity {

    RecyclerView resultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        resultList = findViewById(R.id.resultList);
        Intent intent = getIntent();
        List<Dish> dishes = intent.getParcelableArrayListExtra("dishes");
        Toast.makeText(getApplicationContext(), dishes.size(),
                Toast.LENGTH_SHORT).show();


        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        DishRecyclerAdapter dishAdapter = new DishRecyclerAdapter(dishes);
        resultList.setLayoutManager(llm);
        resultList.setAdapter(dishAdapter);

    }
}
