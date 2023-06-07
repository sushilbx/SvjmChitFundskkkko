package com.kaas.svjmchitfund;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.kaas.svjmchitfund.Module.BannerModel;

import java.util.List;


public class CustompageAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //  private Integer [] images = {R.drawable.coating,R.drawable.coating,R.drawable.coating,R.drawable.coating};
    List<BannerModel.banners> mList;
    public CustompageAdapter(List<BannerModel.banners> banners, Context context) {
        this.context = context;
        this.mList = banners;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.customblog, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Glide.with(context).load(mList.get(position).image).into(imageView);


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}


