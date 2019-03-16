package com.example.hackrpi.foodprint;

import android.graphics.drawable.Drawable;

public class Dish {

    String name;
    Ingredient[] ingredients;
    int servingCount;
    Drawable image;


    public Dish(String name, Ingredient[] ingredients, int servingCount) {

        this.name = name;
        this.ingredients = ingredients;
        this.servingCount = servingCount;

    }

    public void setServingCount(int newServingCount) {
        this.servingCount = newServingCount;
    }

    public Double getTotalCO2() {
        double total = 0.0;
        for (int i = 0; i < ingredients.length; i++) {
           total += ingredients[i].getCO2();
        }
        return total;
    }



}
