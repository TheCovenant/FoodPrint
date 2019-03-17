package com.example.hackrpi.foodprint;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    String name;
    double quantity;
    double weight;
    double CO2 = 0.0;
    double serving_size;
    double price_per_serving;

    String[] measurements = new String[] {"frozen", "cups", "cup", "oz", "grams", "gram", "whole",
            "halved", "half", "quartered","cloves", "slices", "Tbsp", "Pinch", "-", "to ", "tsp",
            "tablespoons", "teaspoons", "tablespoon", "teaspoon", "thin", "thick", "pint", "quart", "gallon","medium", "tbsp",
            "--", "brined", "fresh", "chopped", "pinch", "shredded", "grated", "ml" ,"finely", "taste",
            "coarse", "ly", "plus", "c. ", " x ", "recipes"} ;
    public Ingredient(String name, double weight, krisIngredientList krisIngredientList ) {
        this.weight = weight;
        double quantity = 0;
        String food = "";

        name = name.replaceAll("-", " ");
        name = name.replaceAll("Â", "");
        name = name.replaceAll(",", " ");

        String[] split = name.split("\\s+");
        double fraction = 0;
        for(int i = 0; i < split.length; i++) {
            //System.out.println("\n"+ split[i]);

            if(split[i].contains("/")) {
                String[] fractionString = split[i].split("/");
                fraction = 0;
                try {
                    fraction = Double.parseDouble(fractionString[0]) / Double.parseDouble(fractionString[1]);
                }
                catch (NumberFormatException e) {
                }
                quantity += fraction;
            }
            else {
                try {
                    double num = Double.parseDouble(split[i]);
                    quantity += num;
                }
                catch  (NumberFormatException e){
                    food += split[i];
                    food += " ";
                }
            }

        }
        this.name = food.substring(0, food.length() - 1);

        for(int j = 0; j < measurements.length; ++j ) {

            if(name.contains(measurements[j])) {

                String tempWord = measurements[j] + " ";
                this.name = this.name.replaceAll(measurements[j], "");

                //covers case if the word is at the end of the string
                tempWord = " " + measurements[j];
                this.name = this.name.replaceAll(tempWord, "");
            }
        }
        this.name = this.name.trim();
        this.quantity = quantity;
        krisIngredient matchingIngredient = krisIngredientList.getMatchingKrisIngredient(this);
        if (!matchingIngredient.equals(null)) {
            this.CO2 = matchingIngredient.getCO2();
            this.serving_size = matchingIngredient.getServingSize();
            this.price_per_serving = matchingIngredient.getPricePerServing();
        }
    }

    public void setQuantity(Double newQuantity) {
        this.quantity = newQuantity;
    }

    public void setCO2(Double newCO2) {
        this.CO2 = newCO2;
    }

    public double getCO2() {
        return this.CO2;
    }

    public String getName() {
        String name = new String(this.name);
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeDouble(quantity);
        out.writeDouble(CO2);

    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    private Ingredient(Parcel in) {
        name = in.readString();
        quantity = in.readDouble();
        CO2 = in.readDouble();
    }


}
