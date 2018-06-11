package com.example.ijp.drinkshop.Utils;

import android.support.v7.widget.RecyclerView;

public interface RecyclerItemTouchHelperListner {

    void onSwiped(RecyclerView.ViewHolder viewHolder,int direction,int position);
}
