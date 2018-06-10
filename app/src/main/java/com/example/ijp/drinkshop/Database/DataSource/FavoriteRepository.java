package com.example.ijp.drinkshop.Database.DataSource;

import com.example.ijp.drinkshop.Database.ModelDB.Favorites;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteRepository implements IFavoriteDataSource {

    private IFavoriteDataSource favoriteDataSource;

    public FavoriteRepository(IFavoriteDataSource favoriteDataSource) {
        this.favoriteDataSource = favoriteDataSource;
    }

    public static FavoriteRepository instance;
    public static FavoriteRepository getInstance(IFavoriteDataSource favoriteDataSource)
    {
        if(instance==null)
            instance=new FavoriteRepository(favoriteDataSource);
        return instance;
    }

    @Override
    public Flowable<List<Favorites>> getFavItem() {
        return favoriteDataSource.getFavItem();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDataSource.isFavorite(itemId);
    }

    @Override
    public void insertFav(Favorites... favorites) {
        favoriteDataSource.insertFav(favorites);
    }

    @Override
    public void delete(Favorites favorites) {
        favoriteDataSource.delete(favorites);
    }
}
