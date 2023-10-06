package com.kaas.svjmchitfund.Api.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kaas.svjmchitfund.Module.FilterModel;
import com.kaas.svjmchitfund.R;
import com.kaas.svjmchitfund.databinding.FilterItemBinding;

import java.util.List;


public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {
    public List<FilterModel.Datum> psy;
    public Context context;

    public FilterAdapter(List<FilterModel.Datum> list, Context context) {
        this.psy = list;
        this.context = context;
    }


    @Override
    public FilterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_item, parent, false);
        return new FilterAdapter.MyViewHolder(FilterItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(final FilterAdapter.MyViewHolder holder, final int position) {
        holder.b.tvCustomersId.setText(psy.get(position).customers_id);
        holder.b.tvClosingAmount.setText(psy.get(position).total_amount);
        holder.b.tvCoustmerName.setText(psy.get(position).name);
         holder.b.tvBill.setText(psy.get(position).route);
        holder.b.tvGroup.setText(psy.get(position).group_id);
    }

    @Override
    public int getItemCount() {
        return this.psy.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private FilterItemBinding b;

        public MyViewHolder(FilterItemBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}
