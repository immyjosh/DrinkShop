package com.example.ijp.drinkshop;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ijp.drinkshop.Adapter.CartAdapter;
import com.example.ijp.drinkshop.Adapter.FavoriteAdapter;
import com.example.ijp.drinkshop.Database.ModelDB.Cart;
import com.example.ijp.drinkshop.Database.ModelDB.Favorites;
import com.example.ijp.drinkshop.Utils.Common;
import com.example.ijp.drinkshop.Utils.RecyclerItemTouchHelper;
import com.example.ijp.drinkshop.Utils.RecyclerItemTouchHelperListner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListner {

    RecyclerView recyclerCart;
    Button btnPlaceOrder;

    CartAdapter cartAdapter;

    RelativeLayout rootLayout;

    List<Cart> cartList=new ArrayList<>();

    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable=new CompositeDisposable();

        recyclerCart=findViewById(R.id.recycler_cart);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerCart);

        btnPlaceOrder=findViewById(R.id.btn_place_order);

        rootLayout=findViewById(R.id.root_layout);

        loadCartItems();

    }

    private void loadCartItems() {
        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                displayCartItems(carts);
                            }
                        })
        );
    }

    private void displayCartItems(List<Cart> carts) {
        cartList=carts;
        cartAdapter=new CartAdapter(this,carts);
        recyclerCart.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }



    @Override
    protected void onResume() {
        super.onResume();
        loadCartItems();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartAdapter.CartViewHolder)
        {
            String name=cartList.get(viewHolder.getAdapterPosition()).name;

            final Cart deletedItem=cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex=viewHolder.getAdapterPosition();

            // Delete item from adapter
            cartAdapter.removeItem(deletedIndex);

            //Delete item from Room Database
            Common.cartRepository.deleteCartItem(deletedItem);

            Snackbar snackbar=Snackbar.make(rootLayout,new StringBuilder(name).append(" removed from favorites list").toString(),
                    Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartAdapter.restoreItem(deletedItem,deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
}

