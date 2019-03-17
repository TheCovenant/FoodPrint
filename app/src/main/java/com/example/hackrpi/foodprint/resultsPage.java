package com.example.hackrpi.foodprint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;

public class resultsPage extends AppCompatActivity {
    Dish dish;
    int serving;
    TableLayout table;
    TextView co2Text;
    CardView imageCard;
    ImageView realImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_page);
        Intent intent = getIntent();
        imageCard = findViewById(R.id.imageCard);
        realImage = findViewById(R.id.realImage);
        co2Text = findViewById(R.id.totalCo2Text);
        dish = (Dish)intent.getParcelableExtra("object");
        realImage.setImageDrawable(dish.getImage());
        serving = intent.getIntExtra("serving", 1);
        table = findViewById(R.id.resultsTable);
        dish.giveContext(this);
        co2Text.setText("Total(g): " + Double.toString(dish.getTotalCO2()));
        new DownloadImageTask(realImage).execute(dish.getUrl());




        for(Ingredient ingredient : dish.getIngredients())
        {
            TableRow row =  (TableRow)getLayoutInflater().inflate(R.layout.rowlayout,null);
            TextView name = row.findViewById(R.id.nameRow);
            TextView size = row.findViewById(R.id.sizeRow);
            TextView Co2 = row.findViewById(R.id.Co2Row);
            name.setText(ingredient.getName());
            size.setText(Double.toString(ingredient.getWeight()));
            Co2.setText(Double.toString(ingredient.getCO2()));
            table.addView(row);

        }



    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
