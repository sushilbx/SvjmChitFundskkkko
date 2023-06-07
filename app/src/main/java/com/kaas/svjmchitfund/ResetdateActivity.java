package com.kaas.svjmchitfund;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
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
import com.kaas.svjmchitfund.Module.CoustmerindexModel;
import com.kaas.svjmchitfund.Module.EditCoustmerModel;
import com.kaas.svjmchitfund.Module.EditbillnioModel;
import com.kaas.svjmchitfund.Module.EditdateModel;
import com.kaas.svjmchitfund.Module.SessionModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetdateActivity extends AppCompatActivity {
    List<CoustmerindexModel.Customer> customers=new ArrayList<>();
    EditText date,password;
    String Autocomplete = "";
    AutoCompleteTextView autocomplete;
    SessionManager sessionManager;
    SessionModel sessionModel;
    Context context;
    String Date = "";
    Button btnadd;
    LinearLayout ll_1;
    ArrayList<String> customercode= new ArrayList<>();
    int id;
    ArrayAdapter<String> adapter ;

    String[] arr = { "Paries,France", "PA,United States","Parana,Brazil",
            "Padua,Italy", "Pasadena,CA,United States"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetdate);
        context=this;
        autocomplete = findViewById(R.id.autoCompleteTextView1);
        date = findViewById(R.id.date);
        password = findViewById(R.id.password);
        btnadd = findViewById(R.id.btnadd);
        ll_1 = findViewById(R.id.ll_1);
        sessionManager = new SessionManager(ResetdateActivity.this);
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
                for (int i=0;i<customers.size();i++)
                {
                    if (value.equalsIgnoreCase(customers.get(i).customers_id))
                    {


                        try {
                            String[] Date1= customers.get(i).created_at.split("T");
                            date.setText(Date1[0]);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }


                       // date.setText(String.valueOf(customers.get(i).created_at));
                        id= Integer.parseInt(String.valueOf(customers.get(i).id));

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
        Call<CoustmerindexModel> call = RetrofitClient.getInstance().getApi().indexCoustmer(String.format("Bearer %s", sessionModel.token));
        call.enqueue(new Callback<CoustmerindexModel>() {
            @Override
            public void onResponse(Call<CoustmerindexModel> call, Response<CoustmerindexModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());
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




                }
            }


            @Override
            public void onFailure(Call<CoustmerindexModel> call, Throwable t) {

                Toast.makeText(context, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updatebillno() {
        Log.e("sushiltoken", sessionModel.token);
        Call<EditdateModel> call = RetrofitClient.getInstance().getApi().editdate(String.format("Bearer %s", sessionModel.token) , id,date.getText().toString(),password.getText().toString());
        call.enqueue(new Callback<EditdateModel>() {
            @Override
            public void onResponse(Call<EditdateModel> call, Response<EditdateModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());
                if (response.isSuccessful()) {

                    Toast.makeText(ResetdateActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }


            @Override
            public void onFailure(Call<EditdateModel> call, Throwable t) {

                Toast.makeText(ResetdateActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkForm() {
        Date = date.getText().toString().trim();

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
