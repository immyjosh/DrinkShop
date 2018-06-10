package com.example.ijp.drinkshop.Database.Local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


import com.example.ijp.drinkshop.Database.ModelDB.Cart;
import com.example.ijp.drinkshop.Database.ModelDB.Favorites;

@Database(entities = {Cart.class, Favorites.class},version = 1)
public abstract class IJPRoomDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();
    public abstract FavoriteDAO favoriteDAO();

    private static IJPRoomDatabase instance;

    public static IJPRoomDatabase getInstance(Context context)
    {
        if(instance==null)
            instance= Room.databaseBuilder(context,IJPRoomDatabase.class,"DrinkShop")
                    .allowMainThreadQueries()
                    .build();
        return instance;
    }
}
