package com.example.hackrpi.foodprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class searchResult extends AppCompatActivity {

    RecyclerView resultList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        resultList = findViewById(R.id.resultList);
        List<Dish> dishes = new ArrayList<Dish>();
        dishes.add(new Dish("Spaghetti", getResources().getDrawable(R.drawable.acc_button),R.drawable.acc_button));
        dishes.add(new Dish("Spaghetti", getResources().getDrawable(R.drawable.agric_button),R.drawable.agric_button));
        dishes.add(new Dish("Spaghetti", getResources().getDrawable(R.drawable.bio_button),R.drawable.bio_button));
        dishes.add(new Dish("Spaghetti", getResources().getDrawable(R.drawable.choose_subject), R.drawable.choose_subject));
        dishes.add(new Dish("Spaghetti", getResources().getDrawable(R.drawable.crk_button), R.drawable.crk_button));
        dishes.add(new Dish("Spaghetti", getResources().getDrawable(R.drawable.eco_button), R.drawable.eco_button));
        dishes.add(new Dish("Spaghetti", getResources().getDrawable(R.drawable.eco_button), R.drawable.eco_button));
        dishes.add(new Dish("Spaghetti", getResources().getDrawable(R.drawable.hist), R.drawable.hist));
        dishes.add(new Dish("Spaghetti", getResources().getDrawable(R.drawable.bio_button), R.drawable.bio_button));
        dishes.add(new Dish("Spaghetti", getResources().getDrawable(R.drawable.gov_button), R.drawable.gov_button));

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        DishRecyclerAdapter dishAdapter = new DishRecyclerAdapter(dishes);
        resultList.setLayoutManager(llm);
        resultList.setAdapter(dishAdapter);

    }
}
