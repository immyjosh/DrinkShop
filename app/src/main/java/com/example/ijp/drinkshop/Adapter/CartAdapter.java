package com.example.ijp.drinkshop.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ijp.drinkshop.Database.ModelDB.Cart;
import com.example.ijp.drinkshop.Database.ModelDB.Favorites;
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
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {

        Picasso.with(context)
                .load(cartList.get(position).link)
                .into(holder.imgProduct);

        holder.txtAmount.setNumber(String.valueOf(cartList.get(position).amount));
        holder.txtPrice.setText(new StringBuilder("$").append(cartList.get(position).price));
        holder.txtProductName.setText(new StringBuilder(cartList.get(position).name)
        .append(" x")
        .append(cartList.get(position).amount)
        .append(cartList.get(position).amount==0 ? " Size M":"Size L"));
        holder.txtSugarIce.setText(new StringBuilder("Sugar: ")
                .append(cartList.get(position).sugar).append("%").append("\n")
                .append("%").toString());

        //Get Price of one cup with Options
        final double priceOneCup=cartList.get(position).price/cartList.get(position).amount;

        //Autosave Item when user save amount
        holder.txtAmount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart=cartList.get(position);
                cart.amount=newValue;
                cart.price=Math.round(priceOneCup*newValue);

                Common.cartRepository.updateCart();

                holder.txtPrice.setText(new StringBuilder("$").append(cartList.get(position).price));
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView imgProduct;
        TextView txtProductName,txtSugarIce,txtPrice;
        ElegantNumberButton txtAmount;

        public RelativeLayout viewBackground;
        public LinearLayout viewforeground;

        public CartViewHolder(View itemView) {
            super(itemView);

            imgProduct=itemView.findViewById(R.id.img_product);
            txtAmount=itemView.findViewById(R.id.txt_amount);
            txtProductName=itemView.findViewById(R.id.txt_product_name);
            txtSugarIce=itemView.findViewById(R.id.txt_sugar_ice);
            txtPrice=itemView.findViewById(R.id.txt_price);
            txtAmount=itemView.findViewById(R.id.txt_amount);

            viewBackground=itemView.findViewById(R.id.view_background);
            viewforeground=itemView.findViewById(R.id.view_foreground);
        }
    }

    public void removeItem(int position)
    {
        cartList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Cart item, int position)
    {
        cartList.add(position,item);
        notifyItemInserted(position);
    }
}