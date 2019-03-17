package com.example.hackrpi.foodprint;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class krisIngredientList implements Parcelable{

    ArrayList<krisIngredient> ingredients;

    public krisIngredientList(ArrayList<krisIngredient> ingredients) {
        this.ingredients = ingredients;
    
    }

    public ArrayList<krisIngredient> getKrisIngredients() {
        return ingredients;
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
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedList(ingredients);

    }

    public static final Parcelable.Creator<krisIngredientList> CREATOR = new Parcelable.Creator<krisIngredientList>() {
        public krisIngredientList createFromParcel(Parcel in) {
            return new krisIngredientList(in);
        }

        public krisIngredientList[] newArray(int size) {
            return new krisIngredientList[size];
        }
    };

    private krisIngredientList(Parcel in) {
        ingredients = new ArrayList<krisIngredient>();
        in.readTypedList(ingredients,krisIngredient.CREATOR);

    }
}
