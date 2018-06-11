package com.example.ijp.drinkshop.Utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.ijp.drinkshop.Adapter.CartAdapter;
import com.example.ijp.drinkshop.Adapter.FavoriteAdapter;
import com.example.ijp.drinkshop.Database.ModelDB.Favorites;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    RecyclerItemTouchHelperListner listner;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs,RecyclerItemTouchHelperListner listner) {
        super(dragDirs, swipeDirs);
        this.listner=listner;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if(listner!=null)
            listner.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());


    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
       if(viewHolder instanceof FavoriteAdapter.FavoriteViewHolder)
       {
           View foregroundView=((FavoriteAdapter.FavoriteViewHolder)viewHolder).viewForeground;
           getDefaultUIUtil().clearView(foregroundView);
       }
       else if(viewHolder instanceof CartAdapter.CartViewHolder)
       {
           View foregroundView=((CartAdapter.CartViewHolder)viewHolder).viewforeground;
           getDefaultUIUtil().clearView(foregroundView);
       }

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null){

            if(viewHolder instanceof FavoriteAdapter.FavoriteViewHolder)
            {
                View foregroundView=((FavoriteAdapter.FavoriteViewHolder)viewHolder).viewForeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
            else if(viewHolder instanceof CartAdapter.CartViewHolder)
            {
                View foregroundView=((CartAdapter.CartViewHolder)viewHolder).viewforeground;
                getDefaultUIUtil().onSelected(foregroundView);
            }

        }

    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        if(viewHolder instanceof FavoriteAdapter.FavoriteViewHolder) {
            View foregroundView = ((FavoriteAdapter.FavoriteViewHolder) viewHolder).viewForeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }else if(viewHolder instanceof CartAdapter.CartViewHolder) {
            View foregroundView = ((CartAdapter.CartViewHolder) viewHolder).viewforeground;
            getDefaultUIUtil().onDraw(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
        }
    }


    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
       if(viewHolder instanceof FavoriteAdapter.FavoriteViewHolder)
       {
           View foregroundView=((FavoriteAdapter.FavoriteViewHolder)viewHolder).viewForeground;
           getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
       }else if(viewHolder instanceof CartAdapter.CartViewHolder)
       {
           View foregroundView=((CartAdapter.CartViewHolder)viewHolder).viewforeground;
           getDefaultUIUtil().onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
       }
    }
}
