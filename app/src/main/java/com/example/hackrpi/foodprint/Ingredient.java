package com.example.hackrpi.foodprint;

public class Ingredient {

    String name;
    double quantity;
    double weight;
    double CO2 = 0.0;


    public Ingredient(String name, double weight ) {
        this.weight = weight;
        double quantity = 0;
        String food = "";
        String[] split = name.split("\\s+");
        double fraction = 0;
        for(int i = 0; i < split.length; i++) {
            //System.out.println("\n"+ split[i]);

            if(split[i].contains("/")) {
                String[] fractionString = split[i].split("/");
                //	System.out.println("Left: " + fractionString[0]);
                //	System.out.println("Right: " + fractionString[1]);
                fraction = Double.parseDouble(fractionString[0]) / Double.parseDouble(fractionString[1]);
                quantity += fraction;
            }
            else {
                try {
                    double num = Double.parseDouble(split[i]);
                    //System.out.println("Double: " + num);
                    quantity += num;
                }
                catch  (NumberFormatException e){
                    //	System.out.println("Word");
                    food += split[i];
                    food += " ";

                }
            }
        }
        this.name = food.substring(0, food.length() - 1);

        this.quantity = quantity;


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

}
