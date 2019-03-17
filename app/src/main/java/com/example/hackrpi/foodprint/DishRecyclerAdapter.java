package com.example.hackrpi.foodprint;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class DishRecyclerAdapter extends RecyclerView.Adapter<DishRecyclerAdapter.ViewHolder>
{
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView dishImage;
        TextView dishText;
        LinearLayout dishLinear;
        CardView dishCard;

        public ViewHolder(View itemView)
        {
            super(itemView);
            dishImage =
                    (ImageView)itemView.findViewById(R.id.dishImage);
            dishText =
                    (TextView)itemView.findViewById(R.id.dishText);
            dishLinear =
                    (LinearLayout)itemView.findViewById(R.id.dishLinear);
            dishCard = (CardView)itemView.findViewById(R.id.dishCard);

        }
    }

    List<Dish> dishes;
    DishRecyclerAdapter(List<Dish> dishes)
    {
        this.dishes = dishes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.dishText.setText(dishes.get(i).getName());
        //viewHolder.dishImage.setImageDrawable(dishes.get(i).getImage());
        new DownloadImageTask(viewHolder.dishImage).execute(dishes.get(i).getUrl());
        viewHolder.dishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int x = 0; x < dishes.get(i).getIngredients().size(); ++x)
                {
                    dishes.get(i).getIngredients().get(x).giveCategory();
                }
                Intent intent = new Intent(v.getContext(), selectServing.class);
                intent.putExtra("object", dishes.get(i));
                v.getContext().startActivity(intent);
            }
        });

        viewHolder.dishText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), selectServing.class);
                intent.putExtra("object", dishes.get(i));
                v.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dishes.size();
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
