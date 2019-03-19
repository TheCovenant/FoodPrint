package com.example.hackrpi.foodprint;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class krisIngredient implements Parcelable {

    String name;
    String serving_unit;
    double quantity;
    double CO2 = 0.0;
    double serving_size;
    double price_per_serving;


    public krisIngredient(String name, String serving_unit, double serving_size, double quantity, double price_per_serving, double CO2 ) {
        this.name = name;
        this.quantity = quantity;
        this.CO2 = CO2;
        this.serving_unit = serving_unit;
        this.serving_size = serving_size;
        this.price_per_serving = price_per_serving;
    }


    public double convertToGrams(){
        if(serving_unit.equals("lbs")) {
            return serving_size * 453.592;
        }else if(serving_unit.equals("tbsp")) {
            return serving_size * 14.3;
        }else if(serving_unit.equals("ounce")) {
            return serving_size * 28.3495;
        }else if(serving_unit.equals("egg")) {
            return serving_size * 50;
        }else if(serving_unit.equals("fl. Oz.")) {
            return serving_size * 29.5735296875;
        }
        return serving_size;
    }

    public double getPricePerServing() {
        return this.price_per_serving;
    }
    public void setQuantity(double newQuantity) {
        this.quantity = newQuantity;
    }

    public void setServingSize(double newServingSize){
        this.serving_size = newServingSize;
    }
    public double getServingSize(){
        return this.serving_size;
    }

    public void setCO2(double newCO2) {
        this.CO2 = newCO2;
    }

    public double getCO2() {
        return this.CO2;
    }

    public String getName() {
        String name = new String(this.name);
        return name;
    }

    public String toString(){
        return "(" + this.name + this.quantity +", " + this.CO2 +"g, " + this.serving_size + this.serving_unit+")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(serving_unit);
        out.writeDouble(quantity);
        out.writeDouble(CO2);
        out.writeDouble(serving_size);
        out.writeDouble(price_per_serving);



    }

    public static final Parcelable.Creator<krisIngredient> CREATOR = new Parcelable.Creator<krisIngredient>() {
        public krisIngredient createFromParcel(Parcel in) {
            return new krisIngredient(in);
        }

        public krisIngredient[] newArray(int size) {
            return new krisIngredient[size];
        }
    };

    private krisIngredient(Parcel in) {
        name = in.readString();
        serving_unit = in.readString();
        quantity = in.readDouble();
        CO2 = in.readDouble();
        serving_size = in.readDouble();
        price_per_serving = in.readDouble();



    }

}
