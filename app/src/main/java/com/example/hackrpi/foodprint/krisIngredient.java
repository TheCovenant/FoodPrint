package com.example.hackrpi.foodprint;

public class krisIngredient {

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
            return serving_size * 12;
        }else if(serving_unit.equals("ounce")) {
            return serving_size * 28.3495;
        } return serving_size;
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
        return "(" + this.quantity +", " + this.CO2 +"g, " + this.serving_size + this.serving_unit+")";
    }

}
