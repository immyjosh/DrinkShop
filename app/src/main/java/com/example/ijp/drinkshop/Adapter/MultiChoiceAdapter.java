package com.example.ijp.drinkshop.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.ijp.drinkshop.Model.Drink;
import com.example.ijp.drinkshop.R;
import com.example.ijp.drinkshop.Utils.Common;

import java.util.List;

public class MultiChoiceAdapter extends RecyclerView.Adapter<MultiChoiceAdapter.MultichoiceViewHolder>{

    Context context;
    List<Drink> optionsList;

    public MultiChoiceAdapter(Context context, List<Drink> optionsList) {
        this.context = context;
        this.optionsList = optionsList;
    }

    @NonNull
    @Override
    public MultichoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.multi_check_layout,null);

        return new MultichoiceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MultichoiceViewHolder holder, final int position) {

        holder.checkBox.setText(optionsList.get(position).Name);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Common.toppingAdded.add(buttonView.getText().toString());
                    Common.toppingPrice+=Double.parseDouble(optionsList.get(position).Price);
                }
                else
                {
                    Common.toppingAdded.remove(buttonView.getText().toString());
                    Common.toppingPrice-=Double.parseDouble(optionsList.get(position).Price);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    class MultichoiceViewHolder extends RecyclerView.ViewHolder{

        CheckBox checkBox;

        public MultichoiceViewHolder(View itemView) {
            super(itemView);

            checkBox=itemView.findViewById(R.id.chk_topping);
        }
    }
}
