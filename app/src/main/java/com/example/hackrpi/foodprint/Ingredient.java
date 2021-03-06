package com.example.hackrpi.foodprint;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Ingredient implements Parcelable {

    private String name;
    private double quantity;
    private double weight;
    private double CO2 = 0.0;
    private double serving_size;
    private double price_per_serving;
    private String category = new String("vegetable");

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

                //covers case if the word is at the end of the string
                String tempWord = " " + measurements[j];
                this.name = this.name.replaceAll(tempWord, "");

                this.name = this.name.replaceAll(measurements[j], "");

            }
        }
        this.name = this.name.trim();
        this.quantity = quantity;
        krisIngredient matchingIngredient = krisIngredientList.getMatchingKrisIngredient(this);
        if (!(matchingIngredient == null)) {
            this.CO2 = matchingIngredient.getCO2();
            this.serving_size = matchingIngredient.getServingSize();
            this.price_per_serving = matchingIngredient.getPricePerServing();
        }

    }

    public void giveCategory()
    {
        new AsyncCaller().execute(this);
    }
    public void setServing_size(double serving_size) {
        this.serving_size = serving_size;
    }


    public void setCO2(double CO2) {
        this.CO2 = CO2;
    }


    public String getFoodCategory() {
        //return this.checkFoodGroup(this.name);
        return this.category;
    }
    public double getWeight() {
        double weight = new Double(this.weight);
        return weight;
    }
    public void setWeight(double weight){
        this.weight = new Double(weight);
    }

    public double getQuantity() {
        double quantity = new Double(this.quantity);
        return quantity;
    }

    public double getCO2() {
        double CO2 = new Double(this.CO2);
        return CO2;
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
        out.writeDouble(weight);
        out.writeDouble(CO2);
        out.writeDouble(serving_size);
        out.writeDouble(price_per_serving);
        out.writeString(category);





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
        weight = in.readDouble();
        CO2 = in.readDouble();
        serving_size = in.readDouble();
        price_per_serving = in.readDouble();
        category = in.readString();

    }



    private String checkFoodGroup(String query){
        String api_key = "MmDv3nmqjdaGaeFv5PRopinUeCX2vmP45mB0Chcv"; //do not change these values
        if(query.contains(" ")) {
            query = query.replaceAll(" ", "%20");
        }
        String[] fg_IDs = new String[] {"0200", "0100", "0500", "0700", "1000", "1500", "1100", "0900", "1600", "1200", "2000", "0400"};
        String[] fg_names = new String[] {"other", "dairy", "meat", "meat", "meat", "meat", "vegetables", "fruit", "nuts/seeds/legume", "nuts/seeds/legume", "grain", "oil"};
        HashMap<String, Integer> count = new HashMap<>();

        String url;
        int max_hit = 0;
        String fg_name = "";
        url = "https://api.nal.usda.gov/ndb/?format=json&q="+query+"&sort=n&max=400&offset=0&api_key=" + api_key;
        try {
            URL obj = new URL(url);

            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray items = (JSONArray)  ((JSONObject) new JSONObject(response.toString()).get("list")).get("item");
            Log.d("CheckFoodGroup","Created JSONArray: " + items.toString());
            for(int i = 0; i < items.length(); ++i) {

                JSONObject val = items.getJSONObject(i);
                String group = val.get("group").toString();
                if (group.equals("Branded Food Products Database")) {
                    continue;
                }

                //  System.out.println(group);
                for(int j = 0; j < fg_IDs.length; ++j) {
                    if (group.toLowerCase().contains(fg_names[j])) {
                        if (!count.containsKey(fg_names[j])) {
                            count.put(fg_names[j], 0);
                        }
                        count.put(fg_names[j], count.get(fg_names[j]) + 1);
                    }

                }

            }
            Log.d("CheckFoodGroup","Past the items List " + count.toString());

            for(int i = 0; i < fg_IDs.length; ++i) {
                if (count.containsKey(fg_names[i]) && count.get(fg_names[i]) > max_hit) {
                    max_hit = count.get(fg_names[i]);
                    fg_name = fg_names[i];
                }
            }

            Log.d("CheckFoodGroup","Max checker " + fg_name.toString());


        }
        catch(JSONException e){
        }
        catch(MalformedURLException e) {
        }
        catch(IOException e) {
        }


        if(max_hit == 0) {
            Log.d("CheckFoodGroup","at final return other (expect empty)" + fg_name.toString() + "\n");
            return "other";
        }
        System.currentTimeMillis();
        Log.d("CheckFoodGroup","at final return" + fg_name.toString() + "\n");
        return fg_name;
    }

    private class AsyncCaller extends AsyncTask<Ingredient, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread

        }
        @Override
        protected Void doInBackground(Ingredient... params)
        {
            params[0].category = checkFoodGroup(params[0].getName());
            Log.d("category: ", params[0].category);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);


        }

    }


}
