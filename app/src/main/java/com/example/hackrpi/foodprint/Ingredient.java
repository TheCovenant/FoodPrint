package com.example.hackrpi.foodprint;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Ingredient implements Parcelable {

    String name;
    double quantity;
    double CO2 = 0.0;


    public Ingredient(String name, Double quantity ) {
        this.name = name;
        this.quantity = quantity;
    }

    public void setQuantity(Double newQuantity) {
        this.quantity = newQuantity;
    }

    public void setCO2(Double newCO2) {
        this.CO2 = newCO2;
    }

    public Double getCO2() {
        return this.CO2;
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
