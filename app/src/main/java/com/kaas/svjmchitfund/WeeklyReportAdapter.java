package com.kaas.svjmchitfund;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kaas.svjmchitfund.Module.WeeklyReportModel;

import java.util.List;


public class WeeklyReportAdapter extends RecyclerView.Adapter<WeeklyReportAdapter.MyViewHolder> {
    public List<WeeklyReportModel.Weekly> psy;
    public Context context;

    public WeeklyReportAdapter(List<WeeklyReportModel.Weekly> list, Context context) {
        this.psy = list;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coustomer_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(psy.get(position).name);
        holder.mobileno.setText(psy.get(position).mobile);
        holder.month.setText(psy.get(position).month);
        holder.place.setText(psy.get(position).place);
        holder.amount.setText(psy.get(position).group_id);
        holder.installment.setText(psy.get(position).installment);
        holder.status.setText(psy.get(position).status);

       /* if (psy.get(position).image.isEmpty()) {
            holder.ivImage.setImageResource(R.drawable.bestsellerimage);
        } else {
            Picasso.get().load(psy.get(position).image).into(holder.ivImage);
        }*/
    }

    @Override
    public int getItemCount() {
        return this.psy.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView ivImage;
        TextView name, mobileno, month, place, amount, installment, status;

        MyViewHolder(View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.name);
            mobileno = itemView.findViewById(R.id.mobileno);
            month = itemView.findViewById(R.id.month);
            place = itemView.findViewById(R.id.place);
            amount = itemView.findViewById(R.id.amount);

            status = itemView.findViewById(R.id.status);


        }

    }
}



