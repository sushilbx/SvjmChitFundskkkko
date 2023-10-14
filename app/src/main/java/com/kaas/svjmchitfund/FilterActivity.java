package com.kaas.svjmchitfund;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.adapters.FilterAdapter;
import com.kaas.svjmchitfund.Module.FilterModel;
import com.kaas.svjmchitfund.Module.SessionModel;

import com.kaas.svjmchitfund.databinding.ActivityReeportBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {
    ActivityReeportBinding b;
    Context context;
    String url = "https://svjm-web.seomantras.in/api/customer-filter";
    String file = "";
    String route = "Ambari";
    String total_amount = "15000";
    String group_id = "1";
    SessionManager sessionManager;
    SessionModel sessionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityReeportBinding.inflate(getLayoutInflater());
        View view = b.getRoot();
        setContentView(view);
        context = FilterActivity.this;
        sessionManager = new SessionManager(FilterActivity.this);
        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);
        addProductVarient();
        b.mbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForm()) {
                    addProductVarient();
                }

            }
        });
        b.back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        b.back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        b.printTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://svjm-web.seomantras.in/api/export-customer"));
                startActivity(browserIntent);
            }
        });

    }

    void addProductVarient() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "Processing...", false, false);
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.e("response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        Gson gson = new Gson();
                       // Toast.makeText(context, "Added successfully.", Toast.LENGTH_SHORT).show();
                        FilterModel filterModel = gson.fromJson(response, FilterModel.class);


                        FilterAdapter adapter = new FilterAdapter(filterModel.data, context);
                        b.rvFilter.setAdapter(adapter);

                    } else {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Sorry, something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }

        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                Toast.makeText(context, " Please try again.", Toast.LENGTH_SHORT).show();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responseListener, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("route", route);
                params.put("total_amount", total_amount);
                params.put("group_id", group_id);

                Log.e("params", params.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                Log.e("VollyToken", "" + sessionModel.token);
                headers.put("Authorization", "Bearer " + sessionModel.token);
                return headers;
            }

        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(stringRequest);
    }

    private boolean checkForm() {
        route = b.etRoute.getText().toString().trim();
        group_id = b.etGroup.getText().toString().trim();
        total_amount = b.etAmount.getText().toString().trim();


       /* if (total_amount.isEmpty()) {
            Toast.makeText(context, "Enter Amount", Toast.LENGTH_SHORT).show();
            b.etAmount.setFocusableInTouchMode(true);
            b.etAmount.requestFocus();
            return false;
        }*/
        return true;
    }
}