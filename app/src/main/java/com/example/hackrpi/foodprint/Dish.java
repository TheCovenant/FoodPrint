package com.example.hackrpi.foodprint;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;

public class Dish implements Parcelable{

    private String name;
    private List<Ingredient> ingredients;
    private int servingCount;
    private Drawable image;
    private int imageId;
    private String imageUrl;
    private Context context;

    public Dish(String name, List<Ingredient> ingredients, int servingCount, String imageUrl, Context current, krisIngredientList krisList) {

        this.name = name;
        this.ingredients = ingredients;
        this.servingCount = servingCount;
        this.imageUrl = imageUrl;
        this.image = LoadImageFromWebOperations(imageUrl);
        this.context = current;
        this.calculateCO2andServingSize(krisList);

    }

    public double getTotalCO2() {
        double total = 0.0;
        for (int i = 0; i < ingredients.size(); i++) {
           total += ingredients.get(i).getCO2();
        }
        return total;
    }
    public void calculateCO2andServingSize(krisIngredientList Kris_list) {
    //    ArrayList<Dish> dishes_list =  new ArrayList<>();
        //ArrayList<krisIngredient> Kris_list = new ArrayList<>();
       // Kris_list = krisIngredientList;
        double servings;
        double CO2_val;
        boolean found = false;

        InputStream is = this.context.getResources().openRawResource(R.raw.data);
        CSVReader csvReader = new CSVReader(new InputStreamReader(is));

        HashMap<String, HashMap<String, krisIngredient>> food_csv = new HashMap<>();


        String[] nextRecord;
        String unit;
        int line = 0;
        food_csv.put("fruit", new HashMap<String, krisIngredient>());
        food_csv.put("vegetable", new HashMap<String, krisIngredient>());
        food_csv.put("grain", new HashMap<String, krisIngredient>());
        food_csv.put("meat", new HashMap<String, krisIngredient>());
        food_csv.put("nut/seed/legume", new HashMap<String, krisIngredient>());
        food_csv.put("dairy", new HashMap<String, krisIngredient>());
        food_csv.put("oil", new HashMap<String, krisIngredient>());
        food_csv.put("other", new HashMap<String, krisIngredient>());

        while (true == true) {
            nextRecord = null;
            try {
                nextRecord = csvReader.readNext();
            }
            catch(IOException e) {
                }
            if(nextRecord == null) {
               break;
            }
            if(line == 0) {
                line += 1;
                continue;
            }
            if(nextRecord[5].equals("g") || nextRecord[5].equals("grams")) {
                unit = "g";
            } else if(nextRecord[5].equals("pounds") || nextRecord[5].equals("th pound (http://www.ers.usda.gov/data/foodconsumption/FoodAvailDoc.htm") || nextRecord[5].equals("portion of pound (http://www.ers.usda.gov/data/foodconsumption/FoodAvailDoc.htm)") || nextRecord[5].equals("th pound (http://www.ers.usda.gov/data/foodconsumption/FoodAvailDoc.htm)")) {
                unit = "lbs";
            } else {
                unit = nextRecord[5];
            }
            String food_item = nextRecord[0].toLowerCase();
            String food_category = nextRecord[1].toLowerCase();
            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
            String a = nextRecord[4];
            String b = nextRecord[6];
            String c = nextRecord[8];
            a = a.replaceAll(",", "");
            b = b.replaceAll(",", "");
            c = c.replaceAll(",", "");
            double serving_size = Double.parseDouble(a);
            double price_per_serving = Double.parseDouble(b);
            double co2_output = Double.parseDouble(c);

            krisIngredient ingred = new krisIngredient(food_item, unit, serving_size, (double)1, price_per_serving, co2_output);

            if(! food_csv.get(food_category).containsKey(food_item)) {
                food_csv.get(food_category).put(food_item, ingred);
            }

        }





        for(int k = 0; k < this.ingredients.size(); ++k) {
            for(int j = 0; j < Kris_list.getKrisIngredients().size(); ++j) {
                if(Kris_list.getKrisIngredients().get(j).getName().contains(this.ingredients.get(k).getName().toLowerCase()) || this.ingredients.get(k).getName().toLowerCase().contains(Kris_list.getKrisIngredients().get(j).getName())) {

                    servings = 1/Kris_list.getKrisIngredients().get(j).convertToGrams()*this.ingredients.get(k).getWeight();
                    CO2_val = servings*Kris_list.getKrisIngredients().get(j).CO2;
                    this.ingredients.get(k).setCO2(CO2_val);
                    this.ingredients.get(k).setServing_size(servings);
                    found = true;
                }
            }


            if(found == false){
                String foodGroup = "None";

                foodGroup = this.ingredients.get(k).getFoodCategory();

                servings = food_csv.get(foodGroup).get(foodGroup).convertToGrams()*this.ingredients.get(k).getWeight();
                CO2_val = servings*food_csv.get(foodGroup).get(foodGroup).CO2;


                this.ingredients.get(k).setCO2(CO2_val);
                this.ingredients.get(k).setServing_size(servings);
            }
            else {
                found = false;
            }
        }
    }

    public String getName()
    {
        String name = new String(this.name);
        return name;
    }

    public Drawable getImage()
    {
        return image;
    }

    public String getUrl() {
        String url = new String(this.imageUrl);
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeTypedList(ingredients);
        out.writeInt(servingCount);
        out.writeString(imageUrl);

    }

    public static final Parcelable.Creator<Dish> CREATOR = new Parcelable.Creator<Dish>() {
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    private Dish(Parcel in) {
        name = in.readString();
        ingredients = new ArrayList<>();
        in.readTypedList(ingredients, Ingredient.CREATOR);
        servingCount = in.readInt();
        imageUrl = in.readString();
        this.image = LoadImageFromWebOperations(imageUrl);

    }

    public Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}


