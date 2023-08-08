package com.kaas.svjmchitfund.Api.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kaas.svjmchitfund.Module.DateModel;
import com.kaas.svjmchitfund.R;
import com.kaas.svjmchitfund.databinding.DateItemBinding;

import java.util.List;

public class MonthAdapters extends RecyclerView.Adapter<MonthAdapters.MyViewHolder> {
    public List<DateModel.Datum> psy;
    public Context context;

    public MonthAdapters(List<DateModel.Datum> list, Context context) {
        this.psy = list;
        this.context = context;
    }


    @Override
    public MonthAdapters.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_item, parent, false);
        return new MonthAdapters.MyViewHolder(DateItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(final MonthAdapters.MyViewHolder holder, final int position) {
        holder.b.tvCustomersId.setText(psy.get(position).customers_id);
        holder.b.tvNameDate.setText(psy.get(position).name);
        holder.b.tvPlaceDate.setText(psy.get(position).place);
        holder.b.tvTotalAmountDate.setText(psy.get(position).total_amount);
        // holder.b.tvCustomersId.setText(psy.get(position).customers_id);
    }

    @Override
    public int getItemCount() {
        return this.psy.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private DateItemBinding b;

        public MyViewHolder(DateItemBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
