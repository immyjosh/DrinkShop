package com.example.ijp.drinkshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ijp.drinkshop.Adapter.DrinkAdapter;
import com.example.ijp.drinkshop.Model.Drink;
import com.example.ijp.drinkshop.Retrofit.IDrinkShopAPI;
import com.example.ijp.drinkshop.Utils.Common;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DrinkActivity extends AppCompatActivity {

    TextView txt_banner_name;

    IDrinkShopAPI mService;

    RecyclerView lst_drink;

    //Rx Java
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);

        mService= Common.getAPI();

        lst_drink=findViewById(R.id.recycler_drinks);
        lst_drink.setLayoutManager(new GridLayoutManager(this,2));
        lst_drink.setHasFixedSize(true);

        txt_banner_name=findViewById(R.id.txt_menu_name);
        txt_banner_name.setText(Common.currentCategory.Name);

        loadListDrink(Common.currentCategory.ID);
    }

    private void loadListDrink(String menuId) {
        compositeDisposable.add(mService.getDrink(menuId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        displayDrinkList(drinks);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }));

    }

    private void displayDrinkList(List<Drink> drinks) {
        DrinkAdapter adapter=new DrinkAdapter(this,drinks);
        lst_drink.setAdapter(adapter);
    }

    // Exit App wen back button pressed
    boolean isBackButtonClicked=false;

//    @Override
//    public void onBackPressed()
//    {
//
//        if (isBackButtonClicked) {
//            super.onBackPressed();
//            return;
//        }
//        this.isBackButtonClicked=true;
//        Toast.makeText(this, "Please Click Again To Exit", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        isBackButtonClicked=false;
    }
}
