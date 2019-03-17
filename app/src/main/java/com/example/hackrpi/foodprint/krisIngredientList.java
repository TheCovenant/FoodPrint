package com.example.hackrpi.foodprint;

import java.util.ArrayList;

public class krisIngredientList {

    ArrayList<krisIngredient> ingredients;

    public krisIngredientList(ArrayList<krisIngredient> ingredients) {
        this.ingredients = ingredients;


    }

    public krisIngredient getMatchingKrisIngredient(Ingredient ingredient) {

        for (int i = 0; i < ingredients.size(); i++)
            if (ingredients.get(i).getName().equals(ingredient.getName()))
                return ingredients.get(i);

        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getName().contains(ingredient.getName()))
                return ingredients.get(i);
            if (ingredient.getName().contains(ingredients.get(i).getName()))
                return ingredients.get(i);
        }

        return null;
    }

}
