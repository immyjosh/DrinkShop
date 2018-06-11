package com.example.ijp.drinkshop;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ijp.drinkshop.Adapter.CartAdapter;
import com.example.ijp.drinkshop.Adapter.FavoriteAdapter;
import com.example.ijp.drinkshop.Database.ModelDB.Cart;
import com.example.ijp.drinkshop.Database.ModelDB.Favorites;
import com.example.ijp.drinkshop.Retrofit.IDrinkShopAPI;
import com.example.ijp.drinkshop.Utils.Common;
import com.example.ijp.drinkshop.Utils.RecyclerItemTouchHelper;
import com.example.ijp.drinkshop.Utils.RecyclerItemTouchHelperListner;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListner {

    RecyclerView recyclerCart;
    Button btnPlaceOrder;

    CartAdapter cartAdapter;

    RelativeLayout rootLayout;

    List<Cart> cartList=new ArrayList<>();

    CompositeDisposable compositeDisposable;

    IDrinkShopAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable=new CompositeDisposable();

        mService=Common.getAPI();

        recyclerCart=findViewById(R.id.recycler_cart);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerCart);

        btnPlaceOrder=findViewById(R.id.btn_place_order);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
            }
        });

        rootLayout=findViewById(R.id.root_layout);

        loadCartItems();

    }

    private void placeOrder() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Submit Order");

        View submitOrderLayout= LayoutInflater.from(this).inflate(R.layout.submit_order_layout,null);

        final EditText edtComment=submitOrderLayout.findViewById(R.id.edt_comment);
        final EditText edtOtherAddress=submitOrderLayout.findViewById(R.id.edt_other_address);

        final RadioButton rdiUserAddress=submitOrderLayout.findViewById(R.id.rdi_user_address);
        final RadioButton rdiOtherAddress=submitOrderLayout.findViewById(R.id.rdi_other_address);

        //Event
        rdiUserAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    edtOtherAddress.setEnabled(false);
            }
        });

        rdiOtherAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    edtOtherAddress.setEnabled(true);
            }
        });

        builder.setView(submitOrderLayout);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String orderComment=edtComment.getText().toString();
                final String orderAddress;
                if(rdiUserAddress.isChecked())
                    orderAddress=Common.currentUser.getAddress();
                else if(rdiOtherAddress.isChecked())
                    orderAddress=edtOtherAddress.getText().toString();
                else
                    orderAddress="";

                // Submit Order
                compositeDisposable.add(
                        Common.cartRepository.getCartItems()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                if (!TextUtils.isEmpty(orderAddress))
                                    sendOrderToServer(Common.cartRepository.sumPrice(),
                                            carts,orderComment
                                    ,orderAddress);
                                else
                                    Toast.makeText(CartActivity.this, "Order Address Cant Be Empty", Toast.LENGTH_SHORT).show();
                            }
                        })
                );
            }
        });
        builder.show();
    }

    private void sendOrderToServer(float sumPrice,List<Cart> carts, String orderComment, String orderAddress) {
        if(carts.size()>0)
        {
            String orderDetail=new Gson().toJson(carts);

            mService.submitOrder(sumPrice,orderDetail,orderComment,orderAddress,Common.currentUser.getPhone())
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            Toast.makeText(CartActivity.this, "Order Submitted", Toast.LENGTH_SHORT).show();

                            //Clear cart
                            Common.cartRepository.emptyCart();
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                            Log.i("ERROR",t.getMessage());

                        }
                    });
        }
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

