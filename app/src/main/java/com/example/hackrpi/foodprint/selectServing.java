package com.example.hackrpi.foodprint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class selectServing extends AppCompatActivity {

    Dish dish;
    int serving;
    EditText text;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_serving);
        Intent intent = getIntent();
        dish = (Dish) intent.getParcelableExtra("object");
        text = findViewById(R.id.servingText);
        submit = findViewById(R.id.submitButton);

    }

    public void submit(View view)
    {
        if(isInteger((text.getText().toString())))
        {
            Intent intent = new Intent(this, resultsPage.class);
            intent.putExtra("object", dish);
            intent.putExtra("size", serving);
            startActivity(intent);

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please enter a number",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        if (str.isEmpty()) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (str.length() == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
