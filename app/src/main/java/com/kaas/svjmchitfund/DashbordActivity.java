package com.kaas.svjmchitfund;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.BannerModel;
import com.kaas.svjmchitfund.Module.LoguserdModel;
import com.kaas.svjmchitfund.Module.SessionModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashbordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public DrawerLayout drawerLayout;
    NavigationView navigationView;
    ViewPager viewPager,viewPager1;
    LinearLayout sliderDotspanel,sliderDotspanel1;
    private int dotscount;
    private ImageView[] dots;
    LinearLayout ll_1,ll2;
ImageView menuicone,welcome;
TextView textname,profilename;
    SessionManager sessionManager;
    SessionModel sessionModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashbordhome);
        sessionManager = new SessionManager(DashbordActivity.this);
        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);

        menuicone =findViewById(R.id.menuicone);
        ll_1 =findViewById(R.id.ll_1);
        ll2 =findViewById(R.id.ll2);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        textname = findViewById(R.id.textname);
        profilename = findViewById(R.id.profilename);
        banner();
        bannersecond();
        getProfile();
     //   getProfile();
        View headerview = navigationView.getHeaderView(0);

        textname = (TextView) headerview.findViewById(R.id.textname);
        profilename = (TextView) headerview.findViewById(R.id.profilename);

      //  textname.setText(sessionManager.geFirstName());


           Constant.log("token:",sessionModel.token);
           Constant.log("name:",sessionManager.geFirstName());








        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                Intent intent = new Intent(DashbordActivity.this, SchemebillingActivity.class);
                startActivity(intent);
            }
        });



        ll2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {

                Intent intent = new Intent(DashbordActivity.this, PrintbillingActivity.class);
                startActivity(intent);
            }
        });










        menuicone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START, true);

            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager1 = (ViewPager) findViewById(R.id.viewPager1);

        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        sliderDotspanel1 = (LinearLayout) findViewById(R.id.SliderDots1);



        }

    private void banner() {
        Constant.log("Token:",sessionModel.token);
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
                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(response.body().banners, DashbordActivity.this);

                        viewPager.setAdapter(viewPagerAdapter);

                        dotscount = viewPagerAdapter.getCount();
                        dots = new ImageView[dotscount];

                        for (int i = 0; i < dotscount; i++) {

                            dots[i] = new ImageView(DashbordActivity.this);
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
                }
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                Toast.makeText(DashbordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void bannersecond() {
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
                        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(response.body().banners, DashbordActivity.this);

                        viewPager1.setAdapter(viewPagerAdapter);

                        dotscount = viewPagerAdapter.getCount();
                        dots = new ImageView[dotscount];

                        for (int i = 0; i < dotscount; i++) {

                            dots[i] = new ImageView(DashbordActivity.this);
                            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                            params.setMargins(8, 0, 8, 0);

                            sliderDotspanel.addView(dots[i], params);

                        }

                        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.activedot));

                        viewPager1.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                }
            }

            @Override
            public void onFailure(Call<BannerModel> call, Throwable t) {
                Toast.makeText(DashbordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void getProfile() {



        try {
            //  mProgressDialog= Constant.showProgressBar("Please Wait..",context,mProgressDialog);
            Call<LoguserdModel> call = RetrofitClient.getInstance().getApi().loguser("Bearer " + sessionModel.token);
            call.enqueue(new Callback<LoguserdModel>() {
                @Override
                public void onResponse(Call<LoguserdModel> call, final Response<LoguserdModel> response) {
                    int statusCode = response.code();
                    //  Constant.hideProgressBar(mProgressDialog);

                    try {
                        if (response.body().status.equalsIgnoreCase("success")) {

                            profilename.setText(response.body().user.name);
                            sessionManager.setFirstName(response.body().user.name);
                         //   email.setText(response.body().getUser().getEmail());
                          //  mobileno.setText(response.body().getUser().getPhone());
                          //  titalName.setText(response.body().getUser().getName());

                        }


                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<LoguserdModel> call, Throwable t) {
                    Constant.log("API Error", t.toString());
                    // Constant.hideProgressBar(mProgressDialog);

                    // Log error here since request failed
                    // Toast.makeText(getActivity(), "Failure " + t.toString(), Toast.LENGTH_LONG).show();


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home: {
                Intent intent=new Intent(DashbordActivity.this, DashbordActivity.class);
                startActivity(intent);                return true;
            }

            case R.id.trasation: {
                Intent intent = new Intent(DashbordActivity.this, SchemebillingActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.addministration:{
                Intent intent=new Intent(DashbordActivity.this, AdministrationActivity.class);
                startActivity(intent);
                return true;
            }


            case R.id.reports:{
                Intent intent=new Intent(DashbordActivity.this, ReportActivity.class);
                startActivity(intent);
                return true;
            }






            case R.id.logout:{
                Intent intent=new Intent(DashbordActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            }





        }




        return super.onOptionsItemSelected(item);
    }

}