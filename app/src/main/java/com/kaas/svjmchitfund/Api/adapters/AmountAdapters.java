package com.kaas.svjmchitfund.Api.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kaas.svjmchitfund.Module.DateModel;
import com.kaas.svjmchitfund.Module.TotalAmountModel;
import com.kaas.svjmchitfund.R;
import com.kaas.svjmchitfund.databinding.AmountItemBinding;
import com.kaas.svjmchitfund.databinding.DateItemBinding;

import java.util.List;

public class AmountAdapters extends RecyclerView.Adapter<AmountAdapters.MyViewHolder> {
    public List<TotalAmountModel.Datum> psy;
    public Context context;

    public AmountAdapters(List<TotalAmountModel.Datum> list, Context context) {
        this.psy = list;
        this.context = context;
    }


    @Override
    public AmountAdapters.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.amount_item, parent, false);
        return new AmountAdapters.MyViewHolder(AmountItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(final AmountAdapters.MyViewHolder holder, final int position) {
        holder.b.tvgroup.setText("Group Id -                            "+psy.get(position).group_id);
        holder.b.tvroute.setText("Route -                                 "+psy.get(position).route);
        holder.b.tvAmount.setText(String.valueOf("Total Amount -                    "+psy.get(position).total_amount));
        holder.b.tvCoustmerCode.setText("Customer Id -                      "+psy.get(position).customers_id);
         holder.b.tvName.setText("Coustomer Name -             "+psy.get(position).name);
        holder.b.tvPlace.setText("Place -                                  "+psy.get(position).place);
        int a = psy.get(position).total_amount;
        int b = psy.get(position).installment;
        int number = a/b;

        holder.b.tvNumber.setText(String.valueOf("Number of installment -     "+number));
    }

    @Override
    public int getItemCount() {
        return this.psy.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private AmountItemBinding b;

        public MyViewHolder(AmountItemBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
