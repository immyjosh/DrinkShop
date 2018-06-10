package com.example.ijp.drinkshop.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ijp.drinkshop.Database.ModelDB.Cart;
import com.example.ijp.drinkshop.R;
import com.example.ijp.drinkshop.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    Context context;
    List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.cart_item_layout,parent,false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {

        Picasso.with(context)
                .load(cartList.get(position).link)
                .into(holder.imgProduct);

        holder.txtAmount.setNumber(String.valueOf(cartList.get(position).amount));
        holder.txtPrice.setText(new StringBuilder("$").append(cartList.get(position).price));
        holder.txtProductName.setText(cartList.get(position).name);
        holder.txtSugarIce.setText(new StringBuilder("Sugar: ")
                .append(cartList.get(position).sugar).append("%").append("\n")
                .append("%").toString());

        //Autosave Item when user save amount
        holder.txtAmount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart=cartList.get(position);
                cart.amount=newValue;

                Common.cartRepository.updateCart();
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView imgProduct;
        TextView txtProductName,txtSugarIce,txtPrice;
        ElegantNumberButton txtAmount;

        public CartViewHolder(View itemView) {
            super(itemView);

            imgProduct=itemView.findViewById(R.id.img_product);
            txtAmount=itemView.findViewById(R.id.txt_amount);
            txtProductName=itemView.findViewById(R.id.txt_product_name);
            txtSugarIce=itemView.findViewById(R.id.txt_sugar_ice);
            txtPrice=itemView.findViewById(R.id.txt_price);
            txtAmount=itemView.findViewById(R.id.txt_amount);
        }
    }
}