package com.example.ijp.drinkshop.Database.Local;


import com.example.ijp.drinkshop.Database.DataSource.IFavoriteDataSource;
import com.example.ijp.drinkshop.Database.ModelDB.Favorites;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteDataSource implements IFavoriteDataSource {

    private FavoriteDAO favoriteDAO;
    private static FavoriteDataSource instance;

    public FavoriteDataSource(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    public static FavoriteDataSource getInstance(FavoriteDAO favoriteDAO)
    {
        if(instance==null)
            instance=new FavoriteDataSource(favoriteDAO);
        return instance;
    }

    @Override
    public Flowable<List<Favorites>> getFavItem() {
        return favoriteDAO.getFavItem();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDAO.isFavorite(itemId);
    }

    @Override
    public void insertFav(Favorites... favorites) {
        favoriteDAO.insertFav(favorites);
    }

    @Override
    public void delete(Favorites favorites) {
        favoriteDAO.delete(favorites);
    }
}
