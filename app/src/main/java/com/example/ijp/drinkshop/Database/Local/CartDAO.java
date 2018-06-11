package com.example.ijp.drinkshop.Database.Local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.ijp.drinkshop.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItems();

    @Query("SELECT * FROM Cart WHERE id=:cartItemId")
    Flowable<List<Cart>> getCartItemById(int cartItemId);

    @Query("SELECT COUNT(*) from Cart")
    int countCartItems();

    @Query("SELECT SUM(Price) from Cart")
    float sumPrice();

    @Query("DELETE FROM Cart")
    void emptyCart();

    @Insert
    void insertToCart(Cart... carts);

    @Update
    void updateCart(Cart... carts);

    @Delete
    void deleteCartItem(Cart cart);




}
