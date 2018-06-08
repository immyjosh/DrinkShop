package com.example.ijp.drinkshop.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ijp.drinkshop.Interface.IitemClickListner;
import com.example.ijp.drinkshop.R;

public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView img_product;
    TextView txt_drink_name,txt_price;

    IitemClickListner iitemClickListner;

    Button addToCart;

    public void setIitemClickListner(IitemClickListner iitemClickListner) {
        this.iitemClickListner = iitemClickListner;
    }

    public DrinkViewHolder(View itemView) {
        super(itemView);

        img_product=itemView.findViewById(R.id.imageproduct);
        txt_drink_name=itemView.findViewById(R.id.txt_drink_name);
        txt_price=itemView.findViewById(R.id.txt_price);

        addToCart=itemView.findViewById(R.id.btn_add_cart);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        iitemClickListner.onClick(v);

    }
}
