package com.kaas.svjmchitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.SessionModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
Button tvSignup,btn_login;
    EditText etPassword,etPhone;
    TextView text2;
    String Phone = "";
    String password = "";

    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(LoginActivity.this);

        tvSignup = findViewById(R.id.tvSignup);
        btn_login = findViewById(R.id.btn_login);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
        text2 = findViewById(R.id.text2);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });


        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ChangepasswordActivity.class);
                startActivity(intent);
            }
        });






        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForm()) {
                    login();
                }

            }
        });


    }

    private void login() {
        Log.e("sushil", Phone);
        Log.e("sushil", password);



        Call<SessionModel> call = RetrofitClient.getInstance().getApi().login(etPhone.getText().toString(),etPassword.getText().toString());
        call.enqueue(new Callback<SessionModel>() {
            @Override
            public void onResponse(Call<SessionModel> call, Response<SessionModel> response) {
                Log.e("sushil login", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    sessionManager.createLoginSession(response.body());
                    int statusCode = response.code();
                    if (statusCode == 200) {
                        if (response.body().status.equalsIgnoreCase("success")) {
                            sessionManager.createLoginSession(response.body());
                            sessionManager.setLoginFirsttime(true);
                            sessionManager.setFirstName(response.body().name);

                            sessionManager.settokan(response.body().token);
                            sessionManager.setUserID(response.body().data.id);
                            sessionManager.setPasswordID(etPassword.getText().toString());
                            Constant.log("nanana",response.body().token);
                            Constant.log("name",response.body().name);
                            Constant.log("nanana",response.body().data.id);


                           // sessionManager.setFirstName(etname.getText().toString());
                          //  sessionManager.setUserID(response.body().data.id);
                            Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, DashbordActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

                    }


                } else {

                    Toast.makeText(LoginActivity.this, "The Provided Credentials are incorrect", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SessionModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkForm() {
        Phone = etPhone.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (Phone.isEmpty()) {
            Toast.makeText(this, "Enter Phone no", Toast.LENGTH_SHORT).show();
            etPhone.setFocusableInTouchMode(true);
            etPhone.requestFocus();
            return false;
        } else if (!Utils.myMobileValid(Phone)) {
            Toast.makeText(this, "Invalid phone no", Toast.LENGTH_SHORT).show();
            etPhone.setFocusableInTouchMode(true);
            etPhone.requestFocus();
            return false;


        }
        if (password.isEmpty()) {
            etPassword.setError("Password is empty");
            etPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            etPassword.setError("Password should be minimum 6 characters");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

}