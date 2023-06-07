package com.kaas.svjmchitfund;

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

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.CoustmerindexModel;
import com.kaas.svjmchitfund.Module.EditCoustmerModel;
import com.kaas.svjmchitfund.Module.ListCoustmerModel;
import com.kaas.svjmchitfund.Module.SessionModel;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class
EditcustomerActivity extends AppCompatActivity {
    SessionManager sessionManager;
    SessionModel sessionModel;
    List<CoustmerindexModel.Customer> customers=new ArrayList<>();
    String group_id = "";
    ArrayAdapter<String> adapter ;
    String customer_id = "";
   // String group_id = "";
    String name = "";
    String mobile = "";
    String month = "";
    String place = "";
    String installment = "";
    String route = "";
    String status = "";
    String Autocomplete = "";
    LinearLayout ll_1;
    int id;
EditText etCoustmerCode,etName,etMobile,etMonth,etPlace,etAmount,etInstallment;
    AutoCompleteTextView autocomplete;
Button btnadd;
    String[] arr = { "Paries,France", "PA,United States","Parana,Brazil",
            "Padua,Italy", "Pasadena,CA,United States"};

    ArrayList<String> customercode= new ArrayList<>();
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
               context=this;
        setContentView(R.layout.activity_editcustomer);
      //  etGroup = findViewById(R.id.etGroup);
        etCoustmerCode = findViewById(R.id.etCoustmerCode);
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
       // etMonth = findViewById(R.id.etMonth);
        etPlace = findViewById(R.id.etPlace);
        etAmount = findViewById(R.id.etAmount);
        btnadd = findViewById(R.id.btnadd);
        ll_1 = findViewById(R.id.ll_1);
        etInstallment = findViewById(R.id.etInstallment);
       // etRoute = findViewById(R.id.etRoute);
        sessionManager = new SessionManager(EditcustomerActivity.this);
        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);
        autocomplete = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView1);


        sessionManager = new SessionManager(EditcustomerActivity.this);
        indexCoustmer();
        checkForm();




        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });








        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {

                    updateCoustmer();


                }
            }


        });



        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                String value=adapter.getItem(position).toString();
                autocomplete.setText(value);

                for (int i=0;i<customers.size();i++)
                {
                    if (value.equalsIgnoreCase(customers.get(i).customers_id))
                    {
                  etAmount.setText(Html.fromHtml(String.valueOf(customers.get(i).installment)));


                    etName.setText(Html.fromHtml(String.valueOf(customers.get(i).name)));
                 //   etMonth.setText(Html.fromHtml(String.valueOf(customers.get(i).month)));
                    etPlace.setText(Html.fromHtml(String.valueOf(customers.get(i).place)));
                    etInstallment.setText(Html.fromHtml(String.valueOf(customers.get(i).route)));
                  //  etRoute.setText(Html.fromHtml(String.valueOf(customers.get(i).status)));

                    etCoustmerCode.setText(Html.fromHtml(String.valueOf(customers.get(i).group_id)));
                    etMobile.setText(Html.fromHtml(String.valueOf(customers.get(i).mobile)));
                        id= Integer.parseInt(customers.get(i).id);
                        group_id=customers.get(i).group_id;
                        customer_id=customers.get(i).customers_id;
                    }
                }
                autocomplete.setError(null);
            }
        });
    }

    private void indexCoustmer() {
        Log.e("sushiltoken", sessionModel.token);
        Call<CoustmerindexModel> call = RetrofitClient.getInstance().getApi().indexCoustmer(String.format("Bearer %s", sessionModel.token));
        call.enqueue(new Callback<com.kaas.svjmchitfund.Module.CoustmerindexModel>() {
            @Override
            public void onResponse(Call<CoustmerindexModel> call, Response<CoustmerindexModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", customer_id: " + response.code());
                if (response.isSuccessful()) {


                    customers.clear();
                    customers=(response.body().customer);
                    for (int i=0;i<response.body().customer.size();i++)
                    {
                        customercode.add(response.body().customer.get(i).customers_id);
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
                  //  etGroup.setText(Html.fromHtml(String.valueOf(response.body().customer.get(0).group_id)));
          /*          etAmount.setText(Html.fromHtml(String.valueOf(response.body().customer.get(0).installment)));
                    etName.setText(Html.fromHtml(String.valueOf(response.body().customer.get(0).name)));
                    etMonth.setText(Html.fromHtml(String.valueOf(response.body().customer.get(0).month)));
                    etPlace.setText(Html.fromHtml(String.valueOf(response.body().customer.get(0).place)));
                    etInstallment.setText(Html.fromHtml(String.valueOf(response.body().customer.get(0).route)));
                    etRoute.setText(Html.fromHtml(String.valueOf(response.body().customer.get(0).status)));

                    etCoustmerCode.setText(Html.fromHtml(String.valueOf(response.body().customer.get(0).code)));
                    etMobile.setText(Html.fromHtml(String.valueOf(response.body().customer.get(0).mobile)));

*/



                }
            }


            @Override
            public void onFailure(Call<CoustmerindexModel> call, Throwable t) {

                Toast.makeText(EditcustomerActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCoustmer() {
        Log.e("sushiltoken", sessionModel.token);
        Call<ListCoustmerModel> call = RetrofitClient.getInstance().getApi().editCoustmer(String.format("Bearer %s", sessionModel.token),id, group_id,customer_id,name,mobile,month,place,installment,route);
        call.enqueue(new Callback<ListCoustmerModel>() {
            @Override
            public void onResponse(Call<ListCoustmerModel> call, Response<ListCoustmerModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", customer_id: " + response.code());
                if (response.isSuccessful()) {

                    Toast.makeText(EditcustomerActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }


            @Override
            public void onFailure(Call<ListCoustmerModel> call, Throwable t) {

                Toast.makeText(EditcustomerActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private boolean checkForm() {
      //  group_id = etGroup.getText().toString().trim();
        customer_id = etCoustmerCode.getText().toString().trim();
       // group_id = etCoustmerCode.getText().toString().trim();
        name = etName.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
      //  month = etMonth.getText().toString().trim();
        place = etPlace.getText().toString().trim();
        installment = etAmount.getText().toString().trim();
        route = etInstallment.getText().toString().trim();
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

