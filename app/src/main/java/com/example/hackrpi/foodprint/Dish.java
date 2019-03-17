package com.example.hackrpi.foodprint;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Dish implements Parcelable{

    private String name;
    private List<Ingredient> ingredients;
    private int servingCount;
    private Drawable image;
    private int imageId;
    private String imageUrl;

    public Dish(String name, List<Ingredient> ingredients, int servingCount, String imageUrl) {

        this.name = name;
        this.ingredients = ingredients;
        this.servingCount = servingCount;
        this.imageUrl = imageUrl;
        this.image = LoadImageFromWebOperations(imageUrl);

    }

    public double getTotalCO2() {
        double total = 0.0;
        for (int i = 0; i < ingredients.size(); i++) {
           total += ingredients.get(i).getCO2();
        }
        return total;
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


