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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.AddCoustmerModel;
import com.kaas.svjmchitfund.Module.GrouplistModel;
import com.kaas.svjmchitfund.Module.SessionModel;
import com.kaas.svjmchitfund.Module.StaffModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddcustomerActivity extends AppCompatActivity {
    Button btnadd;
    ImageView back;
    EditText etGroup, etCoustmerCode, etName, etMobile, etMonth, etPlace, etAmount, etInstallment, installment;
    SessionManager sessionManager;
    SessionModel sessionModel;
    Context context;
    boolean customer = false;
    String group_id = "";
    String group_Amount = "";
    Spinner sStaff;
    String staff_id;
    int Group_Amount;

    LinearLayout ll_1;
    List<GrouplistModel.Group> group = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String customers_id = "";
    String name = "";
    String mobile = "";
    String month = "";
    String place = "";
    String installmentamt = "";
    String route = "";

    String status = "";
    String Autocomplete = "";

    AutoCompleteTextView autocomplete;
    ArrayList<String> groupcode = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcustomer);
        context = this;
        btnadd = findViewById(R.id.btnadd);
        sStaff = findViewById(R.id.sStaff);
        // etGroup = findViewById(R.id.etGroup);
        etCoustmerCode = findViewById(R.id.etCoustmerCode);
        etName = findViewById(R.id.etName);
        etMobile = findViewById(R.id.etMobile);
        //  etMonth = findViewById(R.id.etMonth);
        etPlace = findViewById(R.id.etPlace);
        etAmount = findViewById(R.id.etAmount);
        installment = findViewById(R.id.installment2);
        ll_1 = findViewById(R.id.ll_1);
        etInstallment = findViewById(R.id.etInstallment);
        autocomplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        // etRoute = findViewById(R.id.etRoute);
        sessionManager = new SessionManager(AddcustomerActivity.this);
        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);

        Log.e("okkkktoken", sessionModel.token);
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getStaff();
        grouplist();
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int installamt = etAmount.getText().length();
                int opebal = Integer.parseInt((installment.getText().toString()));
                if (opebal >= installamt) {

                } else {
                    installment.setError("Please Enter amount Is Greater then Group Amount");
                    installment.requestFocus();
                    return;
                }
                for (int i = 0; i < group.size(); i++) {
                    if (group.get(i).name.equalsIgnoreCase(autocomplete.getText().toString())) {
                        customer = true;
                        break;
                    }

                }
                if (customer) {

                } else {
                    customer = false;
                    Toast.makeText(context, "Group Is Not Exist", Toast.LENGTH_SHORT).show();
                    return;
                }
                customer = false;
                if (checkForm()) {
                    addCoustmer();
                }
            }
        });
        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                String value = adapter.getItem(position).toString();
                autocomplete.setText(value);

                for (int i = 0; i < group.size(); i++) {
                    if (value.equalsIgnoreCase(group.get(i).name)) {
                        etAmount.setText(Html.fromHtml(String.valueOf(group.get(i).amount)));

                        name = String.valueOf(group.get(i).name);
                        group_id = group.get(i).id;
                        group_Amount = String.valueOf(group.get(i).amount);

                        //  Array.from(group_Amount)[0];

                        String s = group_Amount.substring(0, 1);
                        //    etMonth.setText(s);
                    }

                }
                autocomplete.setError(null);
            }
        });

    }

    private void getStaff() {
        Call<StaffModel> call = RetrofitClient.getInstance().getApi().staff("Bearer " + sessionModel.token);
        call.enqueue(new Callback<StaffModel>() {
            @Override
            public void onResponse(Call<StaffModel> call, Response<StaffModel> response) {
                ArrayList<String> countryList = new ArrayList<>();
                //countryList.add("Country");
                for (StaffModel.Datum countryData : response.body().data) {
                    countryList.add(countryData.name);
                }
                ArrayAdapter aa = new ArrayAdapter(AddcustomerActivity.this, android.R.layout.simple_spinner_item, countryList);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sStaff.setAdapter(aa);
                sStaff.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        staff_id = String.valueOf(response.body().data.get(i).id);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
            }

            @Override
            public void onFailure(Call<StaffModel> call, Throwable t) {

                Log.e("sushil", t.getMessage());
                Toast.makeText(AddcustomerActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addCoustmer() {
        Log.e("sushiltoken", sessionModel.token);
        Call<AddCoustmerModel> call = RetrofitClient.getInstance().getApi().addCoustmer(String.format("Bearer %s", sessionModel.token), group_id, customers_id, name, mobile, place, installmentamt, route, "1");
        call.enqueue(new Callback<AddCoustmerModel>() {
            @Override
            public void onResponse(Call<AddCoustmerModel> call, Response<AddCoustmerModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());


                if (response.isSuccessful()) {
                    Toast.makeText(AddcustomerActivity.this, response.body().message, Toast.LENGTH_SHORT).show();

                    finish();
                    //      amount.setText(response.body().group.amount);
                    //   groupname.setText(Html.fromHtml(response.body().group().get(0).name()));

                } else {
                    try {
                        setError(response.errorBody());
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<AddCoustmerModel> call, Throwable t) {

                Toast.makeText(AddcustomerActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        Toast.makeText(AddcustomerActivity.this, errors, Toast.LENGTH_SHORT).show();
    }


    private void grouplist() {
        Log.e("sushiltoken", sessionModel.token);
        Call<GrouplistModel> call = RetrofitClient.getInstance().getApi().grouplist(String.format("Bearer %s", sessionModel.token));
        call.enqueue(new Callback<com.kaas.svjmchitfund.Module.GrouplistModel>() {
            @Override
            public void onResponse(Call<GrouplistModel> call, Response<GrouplistModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());
                if (response.isSuccessful()) {


                    group.clear();
                    group = (response.body().group);
                    for (int i = 0; i < response.body().group.size(); i++) {
                        groupcode.add(response.body().group.get(i).name);
                    }

                    adapter = new ArrayAdapter<String>
                            (context, android.R.layout.select_dialog_item, groupcode);

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
            public void onFailure(Call<GrouplistModel> call, Throwable t) {

                Toast.makeText(AddcustomerActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkForm() {
        // group_id = etGroup.getText().toString().trim();
        customers_id = etCoustmerCode.getText().toString().trim();
        name = etName.getText().toString().trim();
        mobile = etMobile.getText().toString().trim();
        // month = etMonth.getText().toString().trim();
        place = etPlace.getText().toString().trim();
        //  installment = etAmount.getText().toString().trim();
        installmentamt = installment.getText().toString().trim();
        route = etInstallment.getText().toString().trim();
        Autocomplete = autocomplete.getText().toString().trim();

        // status = etRoute.getText().toString().trim();
        if (Autocomplete.isEmpty()) {
            autocomplete.setError("Enter a group id");
            autocomplete.requestFocus();
            return false;
        }

        if (name.isEmpty()) {
            etName.setError("Name is empty");
            etName.requestFocus();
            return false;
        }


        if (etMobile.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Customer mobileno", Toast.LENGTH_SHORT).show();
            etMobile.setError("Customer mobile is required");
            return false;
        }



/*
        if (month.isEmpty()) {
            etMonth.setError("month is empty");
            etMonth.requestFocus();
            return false;
        }
*/


        if (place.isEmpty()) {
            etPlace.setError("place is empty");
            etPlace.requestFocus();
            return false;
        }


        if (route.isEmpty()) {
            etInstallment.setError("route is empty");
            etInstallment.requestFocus();
            return false;
        }

        if (installmentamt.isEmpty()) {
            installment.setError("customeramount is empty");
            installment.requestFocus();
            return false;
        }


        return true;
    }

}