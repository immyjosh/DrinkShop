package com.example.ijp.drinkshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ijp.drinkshop.DrinkActivity;
import com.example.ijp.drinkshop.Interface.IitemClickListner;
import com.example.ijp.drinkshop.Model.Category;
import com.example.ijp.drinkshop.R;
import com.example.ijp.drinkshop.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    List<Category> categories;

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.menu_item_layout,null);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, final int position) {
        // Load Image
        Picasso.with(context)
                .load(categories.get(position).Link)
                .into(holder.imgProduct);

        holder.txtProduct.setText(categories.get(position).Name);

        //Event
        holder.setItemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Common.currentCategory=categories.get(position);

                // Start new Activity
                context.startActivity(new Intent(context, DrinkActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
