package com.kaas.svjmchitfund;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kaas.svjmchitfund.Module.SessionModel;
import com.kaas.svjmchitfund.Module.TodayreportModel;

import java.util.List;



public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.MyViewHolder> {
    public List<TodayreportModel.Today> psy;
    public Context context;
    SessionManager sessionManager;
    SessionModel sessionModel;
    public DailyReportAdapter(List<TodayreportModel.Today> list, Context context,SessionManager  sessionManager1) {
        this.psy = list;
        this.context = context;
        this.sessionManager = sessionManager1;

    }

    @Override
    public DailyReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailyreport, parent, false);
        return new DailyReportAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DailyReportAdapter.MyViewHolder holder, final int position) {
        holder.billno.setText(psy.get(position).billings_id);
        holder.code.setText(psy.get(position).customer.customers_id);
     //   holder.month.setText(psy.get(position).customer.name);
        holder.total.setText(psy.get(position).customer.total_amount);
        holder.person.setText(sessionManager.geFirstName());

    }


    @Override
    public int getItemCount() {
        return this.psy.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        TextView billno,code,month,total,person;

        MyViewHolder(View itemView) {
            super(itemView);
            billno = itemView.findViewById(R.id.billno);
            code = itemView.findViewById(R.id.code);
            month = itemView.findViewById(R.id.month);
            total = itemView.findViewById(R.id.total);
            person = itemView.findViewById(R.id.person);




        }

    }
}