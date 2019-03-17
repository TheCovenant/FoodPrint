package com.example.hackrpi.foodprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import com.opencsv.CSVReader;
import com.opencsv.*;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Locale;
import java.util.HashMap;
import java.util.Collections;
import java.util.ArrayList;
import java.text.NumberFormat;


public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        readIngredients();
    }


    public void readIngredients() {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
         //   FileReader filereader = new FileReader(file);

            InputStream is = getResources().openRawResource(R.raw.data);
            CSVReader csvReader = new CSVReader(new InputStreamReader(is));
            // file reader as a parameter
           // CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            String unit;
            int line = 0;
            int counter = 0;
            ArrayList<krisIngredient> ingred_list = new ArrayList<>();
            HashMap<String, HashMap<String, krisIngredient>> food_csv = new HashMap<>();
            food_csv.put("fruit", new HashMap<String, krisIngredient>());
            food_csv.put("vegetable", new HashMap<String, krisIngredient>());
            food_csv.put("grain", new HashMap<String, krisIngredient>());
            food_csv.put("meat", new HashMap<String, krisIngredient>());
            food_csv.put("nut/seed/legume", new HashMap<String, krisIngredient>());
            food_csv.put("dairy", new HashMap<String, krisIngredient>());
            food_csv.put("oil", new HashMap<String, krisIngredient>());
            food_csv.put("other", new HashMap<String, krisIngredient>());
            // we are going to read data line by line
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
    }
}
