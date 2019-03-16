package com.example.hackrpi.foodprint;

public class Ingredient {

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

}
