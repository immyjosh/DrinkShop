package com.example.ijp.drinkshop.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ijp.drinkshop.Interface.IitemClickListner;
import com.example.ijp.drinkshop.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView imgProduct;
    TextView txtProduct;

    IitemClickListner itemClickListner;

    public void setItemClickListner(IitemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    public CategoryViewHolder(View itemView) {
        super(itemView);

        imgProduct=itemView.findViewById(R.id.imageproduct);
        txtProduct=itemView.findViewById(R.id.txt_menu_name);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v);
    }
}
