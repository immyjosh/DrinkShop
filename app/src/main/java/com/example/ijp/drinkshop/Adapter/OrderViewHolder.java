package com.example.ijp.drinkshop.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.ijp.drinkshop.R;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOrderId,txtOrderPrice,txtOrderAddress,txtOrderComment,txtOrderStatus;

    public OrderViewHolder(View itemView) {
        super(itemView);

        txtOrderAddress=itemView.findViewById(R.id.txt_order_address);
        txtOrderId=itemView.findViewById(R.id.txt_order_id);
        txtOrderPrice=itemView.findViewById(R.id.txt_order_price);
        txtOrderComment=itemView.findViewById(R.id.txt_order_comment);
        txtOrderStatus=itemView.findViewById(R.id.txt_order_status);
    }
}
