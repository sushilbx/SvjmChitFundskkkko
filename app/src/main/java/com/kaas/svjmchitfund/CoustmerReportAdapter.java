package com.kaas.svjmchitfund;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.kaas.svjmchitfund.Module.CoustmerindexModel;
import com.kaas.svjmchitfund.Module.CoustomeReportModel;
import com.kaas.svjmchitfund.databinding.CoustmerReportItemBinding;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class CoustmerReportAdapter extends RecyclerView.Adapter<CoustmerReportAdapter.MyViewHolder> {
    public List<CoustmerindexModel.Customer> psy;
    public Context context;

    public CoustmerReportAdapter(List<CoustmerindexModel.Customer> list, Context context) {
        this.psy = list;
        this.context = context;
    }

    @Override
    public CoustmerReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coustmer_report_item, parent, false);
        return new CoustmerReportAdapter.MyViewHolder(CoustmerReportItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(final CoustmerReportAdapter.MyViewHolder holder, final int position) {


        holder.b.tvTotalAmount.setText(psy.get(position).total_amount);
        holder.b.tvGroup.setText(psy.get(position).group.name);
        holder.b.tvName.setText(psy.get(position).name);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd MMM yyyy ");
        try {
            holder.b.tvDate.setText(sdf.format(new Date(Instant.parse(psy.get(position).created_at).getEpochSecond()*1000)));
        }catch (Exception e){
            e.printStackTrace();
            holder.b.tvDate.setText(psy.get(position).created_at);
        }


    }

    @Override
    public int getItemCount() {
        return this.psy.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private CoustmerReportItemBinding b;

        public MyViewHolder(CoustmerReportItemBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }
}



