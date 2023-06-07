package com.kaas.svjmchitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.GroupModel;
import com.kaas.svjmchitfund.Module.SessionModel;
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

public class AddgroupActivity extends AppCompatActivity {
EditText amount,groupname;
Button btnadd;
    SessionManager sessionManager;
    SessionModel sessionModel;
    Context context;
    ProgressDialog mProgressDialog;
    String Amount="",Groupname="";
    LinearLayout ll_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgroup);
        sessionManager = new SessionManager(AddgroupActivity.this);
        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);
        amount =findViewById(R.id.amount);
        groupname =findViewById(R.id.groupname);
        btnadd =findViewById(R.id.btnadd);
        ll_1 = findViewById(R.id.ll_1);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {

                    groupadd();
                }
            }


        });

        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    private void groupadd() {
        mProgressDialog = Constant.showProgressBar("Please Wait..", this, mProgressDialog);
        Call<GroupModel> call = RetrofitClient.getInstance().getApi().group("Bearer " + sessionManager.gettokan(),groupname.getText().toString(),amount.getText().toString());
        call.enqueue(new Callback<GroupModel>() {
            @Override
            public void onResponse(Call<GroupModel> call, Response<GroupModel> response)
            {

                Constant.hideProgressBar(mProgressDialog);
                int statusCode = response.code();
                //  Log.e("data", new Gson().toJson(call.request().body()));
                if (statusCode==400){
                    Toast.makeText(getApplicationContext(),"A group with this name already exist",Toast.LENGTH_SHORT).show();
                }

             //   Log.e("sushiltest", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),response.body().message,Toast.LENGTH_SHORT).show();
                    finish();
              //      amount.setText(response.body().group.amount);
                 //   groupname.setText(Html.fromHtml(response.body().group().get(0).name()));

                }
            }

            @Override
            public void onFailure(Call<GroupModel> call, Throwable t) {
              //  Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Constant.hideProgressBar(mProgressDialog);
            }
        });
    }




    boolean checkForm() {
        Amount = amount.getText().toString().trim();
        // gender = b.nsCartQtyAddSubtract.getText().toString().trim();
        //age = b.radioGroup.getText().toString().trim();
        Groupname = groupname.getText().toString().trim();



        if (groupname.getText().toString().isEmpty()) {
            groupname.setError("group no is required");
            return false;
        }




        if (amount.getText().toString().isEmpty()) {
            amount.setError("group no is required");

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
        Toast.makeText(AddgroupActivity.this, errors, Toast.LENGTH_SHORT).show();
    }

}