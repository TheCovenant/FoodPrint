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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;

public class Dish implements Parcelable{

    private String name;
    private List<Ingredient> ingredients;
    private int servingCount;
    private Drawable image;
    private int imageId;
    private String imageUrl;
    private Context context;
    private static krisIngredientList krisList;

    public Dish(String name, List<Ingredient> ingredients, int servingCount, String imageUrl, Context current, krisIngredientList krisList) {

        this.name = name;
        this.ingredients = ingredients;
        this.servingCount = servingCount;
        this.imageUrl = imageUrl;
        this.image = LoadImageFromWebOperations(imageUrl);
        this.context = current;
        this.krisList = krisList;

    }

    public void setServingCount(int servingCount) {
        this.servingCount = servingCount;
    }

    public double getTotalCO2() {
        double total = 0.0;
        this.calculateCO2andServingSize();
        for (int i = 0; i < ingredients.size(); i++) {
           total += ingredients.get(i).getCO2();
        }
        return total;
    }

    public void giveContext(Context context)
    {
        this.context = context;
    }

    public void calculateCO2andServingSize() {
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
                Log.d("record", Boolean.toString(nextRecord == null) + "valid");
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
                Log.d("fC",food_item);
            }
//            Log.d("food_csv", (food_csv.toString()));
//            Log.d("KrisListSize", Integer.toString(krisList.getKrisIngredients().size()));
//            Log.d("servingCount", Integer.toString(servingCount));
        }
        for(HashMap.Entry<String, HashMap<String, krisIngredient>> category: food_csv.entrySet()){
            //Log.d("F_test", "Work damn it " + food_csv.toString());
            ArrayList<Double> m_CO2_list = new ArrayList<Double>();
            ArrayList<Double> m_pricing_list = new ArrayList<Double>();;
            ArrayList<Double> m_ss_list= new ArrayList<Double>();;
            //Log.d("F_test", "Made it passed the arrayList Initializations" + food_csv.get(category.getKey()));
            for(HashMap.Entry<String, krisIngredient> item: food_csv.get(category.getKey()).entrySet()){
                //Log.d("F_test","made it into the inner group \n"+ item.getKey());
                m_CO2_list.add(food_csv.get(category.getKey()).get(item.getKey()).getCO2());
                m_pricing_list.add(food_csv.get(category.getKey()).get(item.getKey()).getPricePerServing());
                m_ss_list.add(food_csv.get(category.getKey()).get(item.getKey()).convertToGrams());
                //Log.d("F_test","added inner group");
            }
            //Log.d("F_test","made it to the end");
            Collections.sort(m_CO2_list);
            Collections.sort(m_pricing_list);
            Collections.sort(m_ss_list);
            double m_CO2 = m_CO2_list.get(m_CO2_list.size()/2);
            double m_price = m_pricing_list.get(m_pricing_list.size()/2);
            double m_ss = m_ss_list.get(m_ss_list.size()/2);
            krisIngredient n = new krisIngredient(category.getKey(), "g", m_ss, (double)1, m_price, m_CO2);
            //Log.d("testFoodCSV",category.getKey());
            food_csv.get(category.getKey()).put(category.getKey(), n);
        }

        for(int k = 0; k < this.ingredients.size(); ++k) {
            for(int j = 0; j < krisList.getKrisIngredients().size(); ++j) {
                if(krisList.getKrisIngredients().get(j).getName().contains(this.ingredients.get(k).getName().toLowerCase()) || this.ingredients.get(k).getName().toLowerCase().contains(krisList.getKrisIngredients().get(j).getName())) {

                    servings = (double)1/krisList.getKrisIngredients().get(j).convertToGrams()*this.ingredients.get(k).getWeight();
                    CO2_val = servings*krisList.getKrisIngredients().get(j).CO2;

                    CO2_val *= servingCount;
                    servings *= servingCount;

                    this.ingredients.get(k).setCO2(CO2_val);
                    this.ingredients.get(k).setServing_size(servings);
//                    Log.d("testCalc", "TestVal: " + krisList.getKrisIngredients().get(j).getName()+ ", " + Double.toString(1/krisList.getKrisIngredients().get(j).convertToGrams()));
//                    Log.d("testCalc", "weightVal: " + krisList.getKrisIngredients().get(j).getName()+ ", " + Double.toString(this.ingredients.get(k).getWeight()));
//                    Log.d("testCalc", "CO2Val: " +krisList.getKrisIngredients().get(j).getName()+ ", " + Double.toString(CO2_val));
//                    Log.d("testCalc", "Serving Size: " +krisList.getKrisIngredients().get(j).getName()+ ", " + Double.toString(servings));
                    found = true;
                    break;
                }
            }


            if(found == false){
                String foodGroup = this.ingredients.get(k).getFoodCategory();
                Log.d("foodGroup",foodGroup);
                String foodName = this.ingredients.get(k).getName();
                Log.d("wtfGuy",food_csv.get(foodGroup).toString());
                if(food_csv.containsKey(foodGroup) && food_csv.get(foodGroup).containsKey(foodGroup)) {
                    servings = 1/food_csv.get(foodGroup).get(foodGroup).convertToGrams() * this.ingredients.get(k).getWeight();

                    CO2_val = servings * food_csv.get(foodGroup).get(foodGroup).getCO2();
                    CO2_val *= servingCount;
                    servings *= servingCount;
                    //Log.d("testFalse", "TestVal: " + foodGroup+ ", " + Double.toString(1/krisList.getKrisIngredients().get(j).convertToGrams()));
//                    Log.d("testFalse", "weightVal: " + foodGroup+ ", " + Double.toString(this.ingredients.get(k).getWeight()));
//                    Log.d("testFalse", "CO2Val: " +foodGroup+ ", " + Double.toString(CO2_val));
//                    Log.d("testFalse", "Serving Size: " +foodGroup+ ", " + Double.toString(servings));
                    this.ingredients.get(k).setCO2(CO2_val);
                    this.ingredients.get(k).setServing_size(servings);
                }
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
        image = LoadImageFromWebOperations(imageUrl);
        return image;
    }


    public String getUrl() {
        String url = new String(this.imageUrl);
        return url;
    }
    public List<Ingredient> getIngredients()
    {
        return ingredients;
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

        //out.writeTypedList(krisList.getKrisIngredients());

        //out.writeParcelable(krisList, PARCELABLE_WRITE_RETURN_VALUE);


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

        //ArrayList<krisIngredient> listIngredient = new ArrayList<krisIngredient>();
        //in.readTypedList(listIngredient,krisIngredient.CREATOR);
        //krisList = new krisIngredientList(listIngredient);




        //this.image = LoadImageFromWebOperations(imageUrl);

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


