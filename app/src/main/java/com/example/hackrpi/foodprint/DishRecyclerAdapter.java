package com.example.hackrpi.foodprint;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DishRecyclerAdapter extends RecyclerView.Adapter<DishRecyclerAdapter.ViewHolder>
{
    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView dishImage;
        TextView dishText;
        LinearLayout dishLinear;
        CardView dishCard;

        public ViewHolder(View itemView) {
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.dishText.setText(dishes.get(i).name);
        viewHolder.dishImage.setImageDrawable(dishes.get(i).image);


    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

}
