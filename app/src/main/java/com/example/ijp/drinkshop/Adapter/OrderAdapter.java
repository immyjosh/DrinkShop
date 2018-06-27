package com.example.ijp.drinkshop.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ijp.drinkshop.Model.Order;
import com.example.ijp.drinkshop.R;
import com.example.ijp.drinkshop.Utils.Common;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    Context context;
    List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.order_layout,parent,false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.txtOrderId.setText(new StringBuilder("#").append(orderList.get(position).getOrderId()));
        holder.txtOrderPrice.setText(new StringBuilder("$").append(orderList.get(position).getOrderPrice()));
        holder.txtOrderAddress.setText(orderList.get(position).getOrderAddress());
        holder.txtOrderComment.setText(orderList.get(position).getOrderComment());
        holder.txtOrderStatus.setText(new StringBuilder("Order Status: ").append(Common.convertCodeToStatus(orderList.get(position).getOrderStatus())));

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}
