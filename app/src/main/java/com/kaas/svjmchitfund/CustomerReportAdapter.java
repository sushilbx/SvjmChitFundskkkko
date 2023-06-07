package com.kaas.svjmchitfund;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.kaas.svjmchitfund.Module.MonthlyreportModel;
import com.kaas.svjmchitfund.Module.TodayreportModel;

import java.util.List;




public class CustomerReportAdapter extends RecyclerView.Adapter<CustomerReportAdapter.MyViewHolder> {
    public List<MonthlyreportModel.Today> psy;
    public Context context;

    public CustomerReportAdapter(List<MonthlyreportModel.Today> list, Context context) {
        this.psy = list;
        this.context = context;
    }

    @Override
    public CustomerReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customerreport, parent, false);
        return new CustomerReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CustomerReportAdapter.MyViewHolder holder, final int position) {
        holder.billno.setText(psy.get(position).billings_id);
        holder.date.setText(psy.get(position).created);
        holder.name.setText(psy.get(position).customer.name);
        holder.total.setText(psy.get(position).customer.total_amount);


    }


    @Override
    public int getItemCount() {
        return this.psy.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView billno,date,month,total,name;

        MyViewHolder(View itemView) {
            super(itemView);
            billno = itemView.findViewById(R.id.billno);
            date = itemView.findViewById(R.id.date);
           // month = itemView.findViewById(R.id.month);
            total = itemView.findViewById(R.id.total);
            name = itemView.findViewById(R.id.name);



        }

    }
}