package com.example.ijp.drinkshop.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ijp.drinkshop.Interface.IitemClickListner;
import com.example.ijp.drinkshop.Model.Drink;
import com.example.ijp.drinkshop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {

    Context context;
    List<Drink> drinkList;

    public DrinkAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.drink_item_layout,null);

        return new DrinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int position) {

        holder.txt_price.setText(new StringBuilder("$").append(drinkList.get(position).Price).toString());
        holder.txt_drink_name.setText(drinkList.get(position).Name);

        Picasso.with(context)
                .load(drinkList.get(position).Link)
                .into(holder.img_product);

        holder.setIitemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
