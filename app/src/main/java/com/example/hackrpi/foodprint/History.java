package com.example.hackrpi.foodprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.opencsv.CSVReader;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;
import java.text.NumberFormat;


public class History extends AppCompatActivity {


    krisIngredientList temp;
    String search_query;
    EditText search_box;
    Button submit_box;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        search_box = findViewById(R.id.searchBar);
        submit_box = findViewById(R.id.searchButton);
        readIngredients();


    }

    public void toSearch(View view) {
        Intent intent = new Intent(this, searchResult.class);
        search_query = search_box.getText().toString();
        ArrayList<Dish> dishes = searchRecipe(search_query);
        //Toast.makeText(getApplicationContext(), Integer.toString(dishes.size()),
        //        Toast.LENGTH_SHORT).show();
        //intent.putParcelableArrayListExtra("dishes", dishes);
        //startActivity(intent);
    }

    public ArrayList<Dish> searchRecipe(String search) {
        ArrayList<Dish> dish_list = new ArrayList<>();
        String app_id = "6c5d04e2"; //do not change these values
        String app_key = "efc4f5c31a452257862f8a8153d2c6d4"; //do not change these values
        String user_input = search; //Default value is chicken

        if(user_input.contains(" ")){
            user_input = user_input.replaceAll(" ", "%20");
        }
        String url = "https://api.edamam.com/search?q=" + user_input + "&from=0&to=20&app_id=" + app_id + "&app_key="+ app_key;

        try {
            URL obj = new URL(url);


            HttpURLConnection con = (HttpURLConnection)obj.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            search_box.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "yo",
                    Toast.LENGTH_SHORT).show();


            String inputLine;
            StringBuffer response = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject myresponse = new JSONObject(response.toString());
            JSONArray hits = (JSONArray) myresponse.get("hits");



            for ( int i = 0; i < hits.length(); i++) {

                JSONObject val = hits.getJSONObject(i);

                JSONObject recipe = val.getJSONObject("recipe");

                JSONArray ingredients_list = recipe.getJSONArray("ingredients");
                String image_url = recipe.get("image").toString();

                List<Ingredient> ingredients_list_final = new ArrayList<>();

                for(int t = 0; t < ingredients_list.length(); ++t) {

                    JSONObject ingredient = ingredients_list.getJSONObject(t);


                    String ingredient_name = ingredient.get("text").toString();
                    double amount = 0;
                    try {
                        amount = Double.parseDouble(ingredient.get("weight").toString());
                    }
                    catch (NumberFormatException e){
                        System.out.println("NUMBER ERROR");
                    }
                    Ingredient temp_ing = new Ingredient(ingredient_name, amount, temp); //create new ingredient object
                    ingredients_list_final.set(t, temp_ing);						   //add ingredient to recipe list
                }
                Dish dish_info = new Dish(recipe.getString("label"), ingredients_list_final, recipe.getInt("yield") , image_url);
                dish_list.add(dish_info);
            }

        }
        catch(Exception JSONException){
            System.out.println("HI IF YOU SEE PLEASE REPORT TO ERROR TEAM");
        }
        return dish_list;

    }


    public void readIngredients() {

        ArrayList<krisIngredient> ingred_list = new ArrayList<>();
        try {


            InputStream is = getResources().openRawResource(R.raw.data);
            CSVReader csvReader = new CSVReader(new InputStreamReader(is));

            String[] nextRecord;
            String unit;
            int line = 0;
            int counter = 0;
            HashMap<String, HashMap<String, krisIngredient>> food_csv = new HashMap<>();
            food_csv.put("fruit", new HashMap<String, krisIngredient>());
            food_csv.put("vegetable", new HashMap<String, krisIngredient>());
            food_csv.put("grain", new HashMap<String, krisIngredient>());
            food_csv.put("meat", new HashMap<String, krisIngredient>());
            food_csv.put("nut/seed/legume", new HashMap<String, krisIngredient>());
            food_csv.put("dairy", new HashMap<String, krisIngredient>());
            food_csv.put("oil", new HashMap<String, krisIngredient>());
            food_csv.put("other", new HashMap<String, krisIngredient>());
            while ((nextRecord = csvReader.readNext()) != null) {
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
                double serving_size = format.parse(nextRecord[4]).doubleValue();
                double co2_output = format.parse(nextRecord[8]).doubleValue();
                double price_per_serving = format.parse(nextRecord[6]).doubleValue();

                krisIngredient ingred = new krisIngredient(food_item, unit, serving_size, (double)1, price_per_serving, co2_output);
                ingred_list.add(ingred);
                if(! food_csv.get(food_category).containsKey(food_item)) {
                    food_csv.get(food_category).put(food_item, ingred);
                }

            }

            for(HashMap.Entry<String, HashMap<String, krisIngredient>> category: food_csv.entrySet()){
                ArrayList<Double> m_CO2_list = new ArrayList<Double>();
                ArrayList<Double> m_pricing_list = new ArrayList<Double>();;
                ArrayList<Double> m_ss_list= new ArrayList<Double>();;
                for(HashMap.Entry<String, krisIngredient> item: food_csv.get(category.getKey()).entrySet()){
                    m_CO2_list.add(food_csv.get(category.getKey()).get(item.getKey()).getCO2());
                    m_pricing_list.add(food_csv.get(category.getKey()).get(item.getKey()).getPricePerServing());
                    m_ss_list.add(food_csv.get(category.getKey()).get(item.getKey()).convertToGrams());
                }
                Collections.sort(m_CO2_list);
                Collections.sort(m_pricing_list);
                Collections.sort(m_ss_list);
                double m_CO2 = m_CO2_list.get(m_CO2_list.size()/2);
                double m_price = m_pricing_list.get(m_pricing_list.size()/2);
                double m_ss = m_ss_list.get(m_ss_list.size()/2);
                krisIngredient n = new krisIngredient(category.getKey(), "g", m_ss, (double)1, m_price, m_CO2);
                food_csv.get(category.getKey()).put(category.getKey(), n);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        temp = new krisIngredientList(ingred_list);
    }

}

