package com.kaas.svjmchitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.BilllistModel;
import com.kaas.svjmchitfund.Module.EditCoustmerModel;
import com.kaas.svjmchitfund.Module.EditbillnioModel;
import com.kaas.svjmchitfund.Module.ListCoustmerModel;
import com.kaas.svjmchitfund.Module.SessionModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetbillActivity extends AppCompatActivity {
EditText billno,paswword;
Button btnadd;
    List<BilllistModel.Billing> billing=new ArrayList<>();
    String Autocomplete = "";
    AutoCompleteTextView autocomplete;
    SessionManager sessionManager;
    SessionModel sessionModel;
    Context context;
    String Billno = "";
    String Paswword = "";
    ArrayList<String> customercode= new ArrayList<>();
    String billings_id;
    int id;
    ArrayAdapter<String> adapter ;
LinearLayout ll_1;
    String[] arr = { "Paries,France", "PA,United States","Parana,Brazil",
            "Padua,Italy", "Pasadena,CA,United States"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetbill);
        billno = findViewById(R.id.billno);
        btnadd = findViewById(R.id.btnadd);
        paswword = findViewById(R.id.paswword);
        ll_1 = findViewById(R.id.ll_1);
        context=this;
        autocomplete = findViewById(R.id.autoCompleteTextView1);
        sessionManager = new SessionManager(ResetbillActivity.this);
        // connect();

        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);


        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });





        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                String value=adapter.getItem(position).toString();
                autocomplete.setText(value);

                autocomplete.setError(null);
                for (int i=0;i<billing.size();i++)
                {
                    if (value.equalsIgnoreCase(billing.get(i).customers_id))
                    {
                        billno.setText(String.valueOf(billing.get(i).billings_id));
                        id= Integer.parseInt(billing.get(i).id);
                        billings_id= billing.get(i).billings_id;
                        break;

                    }
                }
            }
        });
        indexCoustmer();
        checkForm();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {

                    updatebillno();


                }
            }


        });
    }
    private void indexCoustmer() {
        Log.e("sushiltoken", sessionModel.token);
        Call<BilllistModel> call = RetrofitClient.getInstance().getApi().billingindex(String.format("Bearer %s", sessionModel.token));
        call.enqueue(new Callback<com.kaas.svjmchitfund.Module.BilllistModel>() {
            @Override
            public void onResponse(Call<BilllistModel> call, Response<BilllistModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());
                if (response.isSuccessful()) {


                    billing.clear();
                    billing=(response.body().billing);
                    for (int i=0;i<response.body().billing.size();i++)
                    {
                        customercode.add(response.body().billing.get(i).customers_id);
                    }

                    adapter = new ArrayAdapter<String>
                            (context,android.R.layout.select_dialog_item, customercode);

                    autocomplete.setThreshold(2);
                    autocomplete.setAdapter(adapter);
                    autocomplete.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            autocomplete.showDropDown();
                            return false;
                        }
                    });




                }
            }


            @Override
            public void onFailure(Call<BilllistModel> call, Throwable t) {

                Toast.makeText(context, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updatebillno() {
        Log.e("sushiltoken", sessionModel.token);
        Call<EditbillnioModel> call = RetrofitClient.getInstance().getApi().editbillno(String.format("Bearer %s", sessionModel.token) , id, billings_id,Paswword);
        call.enqueue(new Callback<EditbillnioModel>() {
            @Override
            public void onResponse(Call<EditbillnioModel> call, Response<EditbillnioModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());
                if (response.isSuccessful()) {

                    Toast.makeText(ResetbillActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }


            @Override
            public void onFailure(Call<EditbillnioModel> call, Throwable t) {

                Toast.makeText(ResetbillActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean checkForm() {
        Billno = billno.getText().toString().trim();
        Paswword = paswword.getText().toString().trim();

        //status = etRoute.getText().toString().trim();
        Autocomplete = autocomplete.getText().toString().trim();

        if (Autocomplete.isEmpty()) {
            autocomplete.setError("Enter a Customer_id");
            autocomplete.requestFocus();
            return false;
        }

        return true;
    }

}