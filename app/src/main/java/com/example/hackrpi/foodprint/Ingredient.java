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


    public Ingredient(String name, double weight, krisIngredientList krisIngredientList ) {
        this.weight = weight;
        double quantity = 0;
        String food = "";
        String[] split = name.split("\\s+");
        double fraction = 0;
        for(int i = 0; i < split.length; i++) {
            //System.out.println("\n"+ split[i]);

            if(split[i].contains("/")) {
                String[] fractionString = split[i].split("/");
                fraction = Double.parseDouble(fractionString[0]) / Double.parseDouble(fractionString[1]);
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
