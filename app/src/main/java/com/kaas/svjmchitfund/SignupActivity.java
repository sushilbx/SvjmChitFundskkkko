package com.kaas.svjmchitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.SignupModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    Button tvlogin, btn_signin;
    EditText etname, etPhone, etPassword, etemail, etConfirmpassword;
    SessionManager sessionManager;
    String name = "", phone = "", password = "", Email = "", Confirmpassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        tvlogin = findViewById(R.id.tvlogin);
        etname = findViewById(R.id.etname);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etemail = findViewById(R.id.etemail);
        etConfirmpassword = findViewById(R.id.etConfirmpassword);

        btn_signin = findViewById(R.id.btn_signin);

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    signUp();
                }
            }
        });


    }

    void signUp() {
        sessionManager = new SessionManager(SignupActivity.this);

        Call<SignupModel> call = RetrofitClient.getInstance().getApi().register(etname.getText().toString(), etPhone.getText().toString(), etPassword.getText().toString(), etemail.getText().toString(), etConfirmpassword.getText().toString());
        call.enqueue(new Callback<SignupModel>() {
            @Override
            public void onResponse(Call<SignupModel> call, Response<SignupModel> response) {
                int statusCode = response.code();
                if (statusCode == 201) {
                    if (response.body().status.equalsIgnoreCase("success")) {
                        sessionManager.setUserID(response.body().user.id);
                        sessionManager.settokan(response.body().token);
                        sessionManager.setFirstName(etname.getText().toString());
                        sessionManager.setPasswordID(etPassword.getText().toString());
                        Toast.makeText(SignupActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, DashbordActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            setError(response.errorBody());
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<SignupModel> call, Throwable t) {

                Toast.makeText(SignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    boolean checkForm() {
        name = etname.getText().toString().trim();
        // gender = b.nsCartQtyAddSubtract.getText().toString().trim();
        //age = b.radioGroup.getText().toString().trim();
        phone = etPhone.getText().toString().trim();

        password = etPassword.getText().toString().trim();
        Email = etemail.getText().toString().trim();
        Confirmpassword = etConfirmpassword.getText().toString().trim();


        if (etname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Name", Toast.LENGTH_SHORT).show();
            etname.setError("Name is required");
            return false;
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "Enter mobile number", Toast.LENGTH_SHORT).show();
            etPhone.setFocusableInTouchMode(true);
            etPhone.requestFocus();
            return false;
        } else if (!Utils.myMobileValid(phone)) {
            Toast.makeText(this, "Mobile Number should be 10 digit", Toast.LENGTH_SHORT).show();
            etPhone.setFocusableInTouchMode(true);
            etPhone.requestFocus();
            return false;
        }


        if (etPassword.getText().toString().trim().length() < 8) {
            Toast.makeText(this, "Enter  Password", Toast.LENGTH_SHORT).show();
            etPassword.setError("Password Should be minimum 8 character");
            return false;
        }
        if (Email.isEmpty()) {
            Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            etemail.setFocusableInTouchMode(true);
            etemail.requestFocus();
            return false;
        } else if (!Utils.myEmailValid(Email)) {
            Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show();
            etemail.setFocusableInTouchMode(true);
            etemail.requestFocus();
            return false;
        }

        return true;
    }

    private void setError(ResponseBody error) throws JSONException, IOException {
        String errorBody = error.string();

        JSONObject jsonObject = new JSONObject(errorBody.trim());
        Iterator<String> keys = jsonObject.keys();
        String errors = "";
        while (keys.hasNext()) {
            String key = keys.next();
            JSONArray arr = jsonObject.getJSONArray(key);
            for (int i = 0; i < arr.length(); i++) {
                errors += key + " : " + arr.getString(i) + "\n";
            }
        }
        Log.e("ERRORXV", errors);
        Toast.makeText(SignupActivity.this, errors, Toast.LENGTH_SHORT).show();
    }


}