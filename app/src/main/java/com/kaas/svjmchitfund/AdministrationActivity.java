package com.kaas.svjmchitfund;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.BannerModel;
import com.kaas.svjmchitfund.Module.SessionModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdministrationActivity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout sliderDotspanel,ll_1;
    private int dotscount;
    private ImageView[] dots;
    CardView cardview,cardview2,cardview3,cardview4,cardview5,cardview6;
    SessionManager sessionManager;
    SessionModel sessionModel;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administration);
        cardview = findViewById(R.id.cardview);
        cardview2 = findViewById(R.id.cardview2);
       // cardview3 = findViewById(R.id.cardview3);
       // cardview4 = findViewById(R.id.cardview4);
        cardview5 = findViewById(R.id.cardview5);
        cardview6 = findViewById(R.id.cardview6);
        back = findViewById(R.id.back);
        ll_1 = findViewById(R.id.ll_1);
        sessionManager = new SessionManager(AdministrationActivity.this);
        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);












        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdministrationActivity.this, AddcustomerActivity.class);
                startActivity(intent);
            }
        });


        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





/*
        cardview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdministrationActivity.this, ResetdateActivity.class);
                startActivity(intent);
            }
        });
*/
        banner();
        cardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdministrationActivity.this, EditcustomerActivity.class);
                startActivity(intent);
            }
        });

/*
        cardview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdministrationActivity.this, ResetbillActivity.class);
                startActivity(intent);
            }
        });
*/

        cardview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdministrationActivity.this, AddbulkActivity.class);
                startActivity(intent);
            }
        });

        cardview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdministrationActivity.this, AddgroupActivity.class);
                startActivity(intent);
            }
        });


        viewPager = (ViewPager) findViewById(R.id.viewPager);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);

    }

    private void banner() {
        Call<BannerModel> call = RetrofitClient.getInstance().getApi().banners("Bearer " + sessionModel.token);
        call.enqueue(new Callback<BannerModel>() {
            @Override
            public void onResponse(Call<BannerModel> call, Response<BannerModel> response) {
                Log.e("bannerjyoti", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

               /*     ViewPagerAdapter bannerAdapter = new ViewPagerAdapter(response.body().banners, DashbordActivity.this);
                    b.imageSlider.setSliderAdapter(bannerAdapter);
*/
                    if (response.body().banners!=null&&response.body().banners.size()>0) {

                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(response.body().banners, AdministrationActivity.this);

                        viewPager.setAdapter(viewPagerAdapter);

                        dotscount = viewPagerAdapter.getCount();
                        dots = new ImageView[dotscount];

                        for (int i = 0; i < dotscount; i++) {

                            dots[i] = new ImageView(AdministrationActivity.this);
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins(8, 0, 8, 0);

                            sliderDotspanel.addView(dots[i], params);

                        }

                        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activedot));

                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {

                                for (int i = 0; i < dotscount; i++) {
                                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                                }

                                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activedot));

                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }


                        });
                    }
                    else
                    {

                    }
                }
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                Toast.makeText(AdministrationActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    };



};