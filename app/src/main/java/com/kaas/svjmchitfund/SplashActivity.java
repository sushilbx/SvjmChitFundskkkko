package com.kaas.svjmchitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler h = new Handler();
        SessionManager sessionManager = new SessionManager(SplashActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                {

                    if(sessionManager.getLoginFirsttime())
                    {
                        Intent intent = new Intent(SplashActivity.this, DashbordActivity.class);
                        intent.putExtra("key","home");
                        startActivity(intent);
                        finish();
                    }else
                    {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        intent.putExtra("key","home");
                        startActivity(intent);
                        finish();
                    }



                }


            }
        }, 3000);
    }

}