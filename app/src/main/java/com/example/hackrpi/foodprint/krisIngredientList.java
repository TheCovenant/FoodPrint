package com.example.hackrpi.foodprint;

public class krisIngredientList {

    krisIngredient[] ingredients;

    public krisIngredientList() {

    }

    public krisIngredient getMatchingKrisIngredient(Ingredient ingredient) {

        for (int i = 0; i < ingredients.length; i++)
            // If the ingredient names from API 1 is equal to the name from API 2
            // Return the CO2 value
            if (ingredients[i].getName().equals(ingredient.getName()))
                return ingredients[i];

        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i].getName().contains(ingredient.getName()))
                return ingredients[i];
            if (ingredient.getName().contains(ingredients[i].getName()))
                return ingredients[i];
        }

        return null;
    }

}
