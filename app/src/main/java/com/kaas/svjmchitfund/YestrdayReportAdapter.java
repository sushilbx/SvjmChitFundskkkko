package com.kaas.svjmchitfund;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kaas.svjmchitfund.Module.MonthlyreportModel;
import com.kaas.svjmchitfund.Module.YestrdayreportModel;

import java.util.List;


public class YestrdayReportAdapter extends RecyclerView.Adapter<YestrdayReportAdapter.MyViewHolder> {
    public List<YestrdayreportModel.Yesterday_report> psy;
    public Context context;

    public YestrdayReportAdapter(List<YestrdayreportModel.Yesterday_report> list, Context context) {
        this.psy = list;
        this.context = context;
    }

    @Override
    public YestrdayReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.yestrdayreport, parent, false);
        return new YestrdayReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final YestrdayReportAdapter.MyViewHolder holder, final int position) {
        holder.billno.setText(psy.get(position).billings_id);
        holder.code.setText(psy.get(position).customers_id);
      holder.name.setText(psy.get(position).customer.name);
       holder.total.setText(psy.get(position).customer.total_amount);


    }


    @Override
    public int getItemCount() {
        return this.psy.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView billno,code,name,total;

        MyViewHolder(View itemView) {
            super(itemView);
            billno = itemView.findViewById(R.id.billno);
            code = itemView.findViewById(R.id.code);
            name = itemView.findViewById(R.id.name);
            total = itemView.findViewById(R.id.total);



        }

    }
}