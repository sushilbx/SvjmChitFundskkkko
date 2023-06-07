package com.kaas.svjmchitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.ChangePasswordModel;
import com.kaas.svjmchitfund.Module.SessionModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangepasswordActivity extends AppCompatActivity {
Button btnsave;
    String url = RetrofitClient.BASE_URL + "change-password";
    EditText etCurrentPassword, etConfirmPassword;
    SessionManager sessionManager;
    SessionModel sessionModel;
    String current_password = "";
    String confirm_password = "";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        sessionManager = new SessionManager(ChangepasswordActivity.this);
        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);

        intent = getIntent();
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnsave = findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForm()) {
                    changePassword();
                }
            }
        });
    }
    private void changePassword() {

        String auth = "Bearer " + sessionModel.token;
        Log.e("sushilauth", auth);
        Call<ChangePasswordModel> call = RetrofitClient.getInstance().getApi().changePassword(auth, etCurrentPassword.getText().toString(),etConfirmPassword.getText().toString());
        call.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                Log.e("sushil login", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {

                    Toast.makeText(ChangepasswordActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(ChangepasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private boolean checkForm() {
        current_password = etCurrentPassword.getText().toString().trim();
        confirm_password = etConfirmPassword.getText().toString().trim();


        if (current_password.isEmpty()) {
            etCurrentPassword.setError("Password is empty");
            etCurrentPassword.requestFocus();
            return false;
        } else if (current_password.length() < 6) {
            etCurrentPassword.setError("Password should be minimum 6 characters");
            etCurrentPassword.requestFocus();
            return false;
        }

        if (confirm_password.isEmpty()) {
            etConfirmPassword.setError("Password is empty");
            etConfirmPassword.requestFocus();
            return false;
        } else if (confirm_password.length() < 6) {
            etConfirmPassword.setError("Password should be minimum 6 characters");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

}
