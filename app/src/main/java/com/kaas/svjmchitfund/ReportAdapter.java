package com.kaas.svjmchitfund;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.FAQViewHolder> {

    private final String TAG = ReportAdapter.class.getSimpleName();


    //ArrayList<TimeLineModel> mPackageList;
    private LayoutInflater inflater;
    private Context mContext;

    //, ArrayList<TimeLineModel> mDataList

    public ReportAdapter(Context context)
    {
        mContext = context;
      //  mPackageList = mDataList;
    }

    @Override
    public FAQViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        return  new FAQViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_btn, parent, false), viewType);

    }

    @Override
    public void onBindViewHolder(FAQViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        holder.update(position,holder);
    }

    @Override
    public int getItemCount() {
      /*  if (mPackageList == null)
            return 0;
        else
            return mPackageList.size();*/
        return  20;
    }

    public class FAQViewHolder extends RecyclerView.ViewHolder {

        /*TextView  tv_Title;

        TextView txt_book;
        ImageView iv_logo;
        TextView txt_more;
     CardView ll_1;
*/
        LinearLayout ll_1;

        FAQViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

/*

            tv_Title = itemView.findViewById(R.id.tv_Title);
            //iv_logo = itemView.findViewById(R.id.iv_logo);
            timeline = itemView.findViewById(R.id.timeline);
*/
            ll_1 = itemView.findViewById(R.id.ll_1);


        }


        @RequiresApi(api = Build.VERSION_CODES.M)
        @SuppressLint("UseCompatLoadingForDrawables")
        public void update(final int position, FAQViewHolder holder)
        {
            {
                /*
            ll_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
*/

/*
            txt_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
*/

/*
            cardupcomingevent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
*/

            }
/*
            txt_book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
*/

/*
            txt_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
*/

/*
            cardupcomingevent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
*/

        }




        }

    }


