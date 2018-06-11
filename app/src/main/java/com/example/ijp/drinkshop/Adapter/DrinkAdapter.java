package com.example.ijp.drinkshop.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.ijp.drinkshop.Database.ModelDB.Cart;
import com.example.ijp.drinkshop.Database.ModelDB.Favorites;
import com.example.ijp.drinkshop.Interface.IitemClickListner;
import com.example.ijp.drinkshop.Model.Drink;
import com.example.ijp.drinkshop.R;
import com.example.ijp.drinkshop.Utils.Common;
import com.google.gson.Gson;
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
    public void onBindViewHolder(@NonNull final DrinkViewHolder holder, final int position) {

        holder.txt_price.setText(new StringBuilder("$").append(drinkList.get(position).Price).toString());
        holder.txt_drink_name.setText(drinkList.get(position).Name);

        holder.btnAddtoCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddtocartDialogue(position);
            }
        });

        Picasso.with(context)
                .load(drinkList.get(position).Link)
                .into(holder.img_product);

        holder.setIitemClickListner(new IitemClickListner() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Favorite System
        if(Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(position).ID))==1)
            holder.btnFavourites.setImageResource(R.drawable.ic_favorite_white_24dp);
        else
            holder.btnFavourites.setImageResource(R.drawable.ic_favorite_border_white_24dp);

        holder.btnFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.favoriteRepository.isFavorite(Integer.parseInt(drinkList.get(position).ID))!=1) {
                    addOrRemoveFavorite(drinkList.get(position),true);
                    holder.btnFavourites.setImageResource(R.drawable.ic_favorite_white_24dp);
                }
                else {
                    addOrRemoveFavorite(drinkList.get(position),false);
                    holder.btnFavourites.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                }
            }
        });

    }

    private void addOrRemoveFavorite(Drink drink, boolean isAdd) {
        Favorites favorites=new Favorites();
        favorites.id=drink.ID;
        favorites.link=drink.Link;
        favorites.name=drink.Name;
        favorites.price=drink.Price;
        favorites.menuId=drink.MenuId;

        if(isAdd)
            Common.favoriteRepository.insertFav(favorites);
        else
            Common.favoriteRepository.delete(favorites);

    }


    private void showAddtocartDialogue(final int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View itemView=LayoutInflater.from(context)
                .inflate(R.layout.add_to_cart_layout,null);

        //View

        ImageView imgproductdialogue=itemView.findViewById(R.id.img_cart_product);
        final ElegantNumberButton txtCount=itemView.findViewById(R.id.txt_count);
        TextView txtProductDialogue=itemView.findViewById(R.id.txt_cart_productname);

        EditText edtComment=itemView.findViewById(R.id.edt_comment);

        RadioButton rdiSizeM=itemView.findViewById(R.id.rdi_sizeM);
        RadioButton rdiSizeL=itemView.findViewById(R.id.rdi_sizeL);

        rdiSizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.sizeOfCup=0;
            }
        });

        rdiSizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.sizeOfCup=1;
            }
        });

        RadioButton rdiSugar100=itemView.findViewById(R.id.rdi_sugar100);
        RadioButton rdiSugar70=itemView.findViewById(R.id.rdi_sugar70);
        RadioButton rdiSugar50=itemView.findViewById(R.id.rdi_sugar50);
        RadioButton rdiSugar30=itemView.findViewById(R.id.rdi_sugar30);
        RadioButton rdiSugarfree=itemView.findViewById(R.id.rdi_sugarfree);

        rdiSugar30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.sugar=30;
            }
        });

        rdiSugar50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.sugar=50;
            }
        });

        rdiSugar70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.sugar=70;
            }
        });

        rdiSugar100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.sugar=100;
            }
        });

        rdiSugarfree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    Common.sugar=0;
            }
        });

        RadioButton rdiIce100=itemView.findViewById(R.id.rdi_ice100);
        RadioButton rdiIce70=itemView.findViewById(R.id.rdi_ice70);
        RadioButton rdiIce50=itemView.findViewById(R.id.rdi_ice50);
        RadioButton rdiIce30=itemView.findViewById(R.id.rdi_ice30);
        RadioButton rdiIcefree=itemView.findViewById(R.id.rdi_icefree);

        rdiIce30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Common.ice=30;
            }
        });

        rdiIce50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Common.ice=50;
            }
        });


        rdiIce70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Common.ice=70;
            }
        });

        rdiIce100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Common.ice=100;
            }
        });

        rdiIcefree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Common.ice=0;
            }
        });

        RecyclerView recyclerTopping=itemView.findViewById(R.id.recycler_topping);
        recyclerTopping.setLayoutManager(new LinearLayoutManager(context));
        recyclerTopping.setHasFixedSize(true);

        MultiChoiceAdapter adapter=new MultiChoiceAdapter(context, Common.toppingList);
        recyclerTopping.setAdapter(adapter);

        // Set Data
        Picasso.with(context)
                .load(drinkList.get(position).Link)
                .into(imgproductdialogue);
        txtProductDialogue.setText(drinkList.get(position).Name);

        builder.setView(itemView);
        builder.setNegativeButton("Add To Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Common.sizeOfCup==-1)
                {
                    Toast.makeText(context, "Please Choose Size Of Cup", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Common.sugar==-1)
                {
                    Toast.makeText(context, "Please Choose Sugar", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Common.ice==-1)
                {
                    Toast.makeText(context, "Please Choose Ice", Toast.LENGTH_SHORT).show();
                    return;
                }

                showConfirmDialog(position,txtCount.getNumber());
                dialog.dismiss();

            }
        });
        builder.show();
    }

    private void showConfirmDialog(final int position, final String number) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View itemView=LayoutInflater.from(context)
                .inflate(R.layout.confirm_add_to_cart_layout,null);

        // View
        ImageView img_product_dialogue=itemView.findViewById(R.id.img_product);
        final TextView txt_Product_Dialogue= itemView.findViewById(R.id.txt_cart_product_name);
        TextView txtProductPrice=itemView.findViewById(R.id.txt_cart_product_price);
        TextView txtSugar=itemView.findViewById(R.id.txt_sugar);
        TextView txtIce=itemView.findViewById(R.id.txt_ice);
        final TextView txtToppingExtra=itemView.findViewById(R.id.txt_topping_extra);

        // Set Data
        Picasso.with(context).load(drinkList.get(position).Link).into(img_product_dialogue);
        txt_Product_Dialogue.setText(new StringBuilder(drinkList.get(position).Name).append(" x")
                .append(Common.sizeOfCup == 0 ? " Size M":" Size L")
                .append(number).toString());

        txtIce.setText(new StringBuilder("Ice: ").append(Common.ice).append("%").toString());
        txtSugar.setText(new StringBuilder("Sugar: ").append(Common.sugar).append("%").toString());

        double price=(Double.parseDouble(drinkList.get(position).Price)* Double.parseDouble(number))
                + Common.toppingPrice;

        if(Common.sizeOfCup==1) //SizeL
            price+=(3.0)*Double.parseDouble(number);


        StringBuilder toppingFinalComment=new StringBuilder("");
        for(String line:Common.toppingAdded)
            toppingFinalComment.append(line).append("\n");

        txtToppingExtra.setText(toppingFinalComment);

        final double finalPrice = Math.round(price);

        txtProductPrice.setText(new StringBuilder("$").append(finalPrice));

        builder.setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

                try {
                    //Add to Sqlite
                    // Create New Cart Item
                    Cart cartItem = new Cart();
                    cartItem.name = drinkList.get(position).Name;
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.ice = Common.ice;
                    cartItem.sugar = Common.sugar;
                    cartItem.size=Common.sizeOfCup;
                    cartItem.price = finalPrice;
                    cartItem.toppingExtras = txtToppingExtra.getText().toString();
                    cartItem.link=drinkList.get(position).Link;

                    // Add to DB
                    Common.cartRepository.insertToCart(cartItem);

                    Log.d("DEBUG", new Gson().toJson(cartItem));

                    Toast.makeText(context, "Save Item To Cart Success", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setView(itemView);
        builder.show();


    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
