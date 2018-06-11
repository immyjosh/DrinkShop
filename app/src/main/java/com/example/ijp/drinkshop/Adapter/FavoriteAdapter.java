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

import com.example.ijp.drinkshop.Database.ModelDB.Favorites;
import com.example.ijp.drinkshop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    Context context;
    List<Favorites> favoritesList;

    public FavoriteAdapter(Context context, List<Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.fav_item_layout,parent,false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {

        Picasso.with(context).load(favoritesList.get(position).link).into(holder.imgProduct);
        holder.txtPrice.setText(new StringBuilder("$").append(favoritesList.get(position).price).toString());
        holder.txtProductName.setText(favoritesList.get(position).name);

    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder
    {
        ImageView imgProduct;
        TextView txtProductName,txtPrice;

        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;


        public FavoriteViewHolder(View itemView) {
            super(itemView);

            imgProduct=itemView.findViewById(R.id.img_product);
            txtProductName=itemView.findViewById(R.id.txt_product_name);
            txtPrice=itemView.findViewById(R.id.txt_price);

            viewBackground=itemView.findViewById(R.id.view_background);
            viewForeground=itemView.findViewById(R.id.view_foreground);
        }
    }

    public void removeItem(int position)
    {
        favoritesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Favorites item,int position)
    {
        favoritesList.add(position,item);
        notifyItemInserted(position);
    }

}
