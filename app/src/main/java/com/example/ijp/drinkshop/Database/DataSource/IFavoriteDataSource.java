package com.example.ijp.drinkshop.Database.DataSource;

import com.example.ijp.drinkshop.Database.ModelDB.Favorites;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource {

    Flowable<List<Favorites>> getFavItem();

    int isFavorite(int itemId);

    void insertFav(Favorites... favorites);

    void delete(Favorites favorites);
}
