package com.example.ijp.drinkshop.Utils;

import com.example.ijp.drinkshop.Model.Category;
import com.example.ijp.drinkshop.Model.User;
import com.example.ijp.drinkshop.Retrofit.IDrinkShopAPI;
import com.example.ijp.drinkshop.Retrofit.RetrofitClient;

import java.util.IdentityHashMap;

public class Common {

    private static final String BASE_URL="http://10.0.2.2/drinkshop/";

    public static User currentUser=null;

    public static Category currentCategory=null;

    public static IDrinkShopAPI getAPI(){
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);
    }
}
