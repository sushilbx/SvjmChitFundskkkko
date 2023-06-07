package com.kaas.svjmchitfund;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kaas.svjmchitfund.Module.CoustmerindexModel;
import com.kaas.svjmchitfund.Module.CustomerreportModel;
import com.kaas.svjmchitfund.Module.MonthlyreportModel;

import java.util.List;


public class CustomernewReportAdapter extends RecyclerView.Adapter<CustomernewReportAdapter.MyViewHolder> {
    public List<CustomerreportModel.Customer_report> psy;
    public Context context;

    public CustomernewReportAdapter(List<CustomerreportModel.Customer_report> list, Context context) {
        this.psy = list;
        this.context = context;
    }

    @Override
    public CustomernewReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customerreportnew, parent, false);
        return new CustomernewReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomernewReportAdapter.MyViewHolder holder, final int position) {
        holder.billno.setText(psy.get(position).billings_id);
        holder.code.setText(psy.get(position).customer.customers_id);
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