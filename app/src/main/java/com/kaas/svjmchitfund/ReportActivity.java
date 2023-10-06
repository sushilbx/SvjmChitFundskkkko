package com.kaas.svjmchitfund;

import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Api.adapters.AmountAdapters;
import com.kaas.svjmchitfund.Api.adapters.DateAdapters;
import com.kaas.svjmchitfund.Module.CoustmerindexModel;
import com.kaas.svjmchitfund.Module.CustomerreportModel;
import com.kaas.svjmchitfund.Module.DateModel;
import com.kaas.svjmchitfund.Module.MonthlyreportModel;
import com.kaas.svjmchitfund.Module.SessionModel;
import com.kaas.svjmchitfund.Module.TodayreportModel;
import com.kaas.svjmchitfund.Module.TotalAmountModel;
import com.kaas.svjmchitfund.Module.YestrdayreportModel;
import com.kaas.svjmchitfund.databinding.ActivityReportBinding;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends Activity implements Runnable {

    public List<TodayreportModel.Today> todays = new ArrayList<>();
    public List<MonthlyreportModel.Today> monthY = new ArrayList<>();
    public List<CustomerreportModel.Customer_report> customer_report = new ArrayList<>();
    public List<YestrdayreportModel.Yesterday_report> yesterday_report = new ArrayList<>();
    private static final int PERMISSION_REQUEST_CODE = 200;
    protected static final String TAG = "ReportActivity";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    String date;
    String monthReport;
    String amountTotal;
    ActivityReportBinding b;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint, Scan2, mPrint2, mPrint1, Scan3, mPrint12, Scan12, createpdf, createpdfmonth, createpdfcustomer, createpdfyestrday;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
    final String formattedDate = df.format(c.getTime());
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    TextView stat, bpstatus1, bpstatus2, bpstatus12, tvDaily;
    int printstat;
    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;
    LinearLayout layout, ll_1, llDe;
    RecyclerView recordRecycleview, monthelyreport, customerreport, yestrdayrecordRecycleview;
    SessionManager sessionManager;
    SessionModel sessionModel;
    AutoCompleteTextView autocomplete;
    List<CoustmerindexModel.Customer> customers = new ArrayList<>();
    String group_id = "";
    String Month = "";
    String Amount = "";
    RecyclerView rvCoustmer;
    ArrayAdapter<String> adapter;
    EditText name;
    TextView month, amount, total, ivDailyImage, code, billno;
    int id;
    RelativeLayout ivDaily, ivMonthly, ivCustomer, ivCustomer1, ivyestrday, ivDate;
    LinearLayout llDaily, llMonthly, llcustomer, llyestrday;
    Context context;
    ArrayList<String> customercode = new ArrayList<>();
    ProgressDialog downloadDialog;
    SessionManager sessionManagement;
    static File incomefile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityReportBinding.inflate(getLayoutInflater());
        View view = b.getRoot();
        setContentView(view);
        sessionManager = new SessionManager(ReportActivity.this);
        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);
        autocomplete = (AutoCompleteTextView)
                findViewById(R.id.autoCompleteTextView1);
        llDe = findViewById(R.id.llDetails);
        recordRecycleview = findViewById(R.id.recordRecycleview);
        rvCoustmer = findViewById(R.id.rvCoustmer);
        customerreport = findViewById(R.id.customerreport);
        yestrdayrecordRecycleview = findViewById(R.id.yestrdayrecordRecycleview);
        name = findViewById(R.id.name);
        ivCustomer1 = findViewById(R.id.ivCustomer1);
        tvDaily = findViewById(R.id.tvDaily);
        ivDate = findViewById(R.id.ivDate);
        createpdfmonth = findViewById(R.id.createpdfmonth);
        createpdfcustomer = findViewById(R.id.createpdfcustomer);
        createpdfyestrday = findViewById(R.id.createpdfyestrday);


        ivDaily = findViewById(R.id.ivDaily);
        createpdf = findViewById(R.id.createpdf);
        llDaily = findViewById(R.id.llDaily);

        llMonthly = findViewById(R.id.llMonthly);
        ivMonthly = findViewById(R.id.ivMonthly);


        ivCustomer = findViewById(R.id.ivCustomer);
        llcustomer = findViewById(R.id.llcustomer);

        ivyestrday = findViewById(R.id.ivyestrday);
        llyestrday = findViewById(R.id.llyestrday);


        month = findViewById(R.id.month);
        amount = findViewById(R.id.amount);
        total = findViewById(R.id.total);
        //   etmonth = findViewById(R.id.etmonth);
        code = findViewById(R.id.code);
        billno = findViewById(R.id.billno);
        monthelyreport = findViewById(R.id.monthelyreport);
        context = this;
        stat = findViewById(R.id.bpstatus);
        bpstatus1 = findViewById(R.id.bpstatus1);

        bpstatus2 = findViewById(R.id.bpstatus2);

        mPrint12 = findViewById(R.id.mPrint12);
        bpstatus12 = findViewById(R.id.bpstatus12);
        Scan12 = findViewById(R.id.Scan12);


        mScan = findViewById(R.id.Scan);
        Scan2 = findViewById(R.id.Scan2);

        Scan3 = findViewById(R.id.Scan3);
        mPrint = findViewById(R.id.mPrint);
        mPrint1 = findViewById(R.id.mPrint1);

        mPrint2 = findViewById(R.id.mPrint2);
        ll_1 = findViewById(R.id.ll_1);


        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        b.rlNextFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportActivity.this, FilterActivity.class));
            }
        });


        // ReportAdapter adaptersblog = new ReportAdapter(ReportActivity.this);
        //  recordRecycleview1.setAdapter(adaptersblog);
        // LinearLayoutManager layoutManager6 = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        // recordRecycleview1.setLayoutManager(layoutManager6);

        //   DailyReportAdapter adaptersreport = new DailyReportAdapter(ReportActivity.this);
        //   recordRecycleview.setAdapter(adaptersreport);
        //   LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true);
        //   recordRecycleview.setLayoutManager(layoutManager);


        ivMonthly.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                // Toast.makeText(ReportActivity.this, "jyOTI", Toast.LENGTH_SHORT).show();
                if (llMonthly.getVisibility() == View.VISIBLE) {
                    llMonthly.setVisibility(View.GONE);
                    // ivMonthly.setImageResource(R.mipmap.arrowup);
                } else {
                    llMonthly.setVisibility(View.VISIBLE);
                }
                //   ivMonthly.setImageResource(R.mipmap.arrowdown);


            }
        });
        b.mbClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (checkForm()) {
                    dateWiseReport();
                }*/
                dateWiseReport();
            }
        });
        b.mbClickMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (checkForm()) {
                    dateWiseReport();
                }*/
                monthWiseReport();
            }
        });
        b.mbClickAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    amountWiseReport();
                }
            }
        });
        ivDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                if (b.llDateDetails.getVisibility() == View.VISIBLE) {
                    b.llDateDetails.setVisibility(View.GONE);
                } else {
                    b.llDateDetails.setVisibility(View.VISIBLE);

                }


            }
        });
        b.ivMonth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                if (b.llMonthDetails.getVisibility() == View.VISIBLE) {
                    b.llMonthDetails.setVisibility(View.GONE);
                } else {
                    b.llMonthDetails.setVisibility(View.VISIBLE);

                }


            }
        });
        b.ivAmount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                if (b.llAccountDetails.getVisibility() == View.VISIBLE) {
                    b.llAccountDetails.setVisibility(View.GONE);
                } else {
                    b.llAccountDetails.setVisibility(View.VISIBLE);

                }


            }
        });

        b.tvEnterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        date = String.format("%s-%s-%s", year, String.format("%02d", month + 1), String.format("%02d", dayOfMonth));
                        b.tvEnterDate.setText(date);
                    }
                }, mYear, mMonth, mDay);
//                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();

            }
        });
        b.tvEnterMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        monthReport = String.format("%s-%s", year, String.format("%02d", month + 1)/*, String.format("%02d", dayOfMonth)*/);
                        b.tvEnterMonth.setText(monthReport);
                    }
                }, mYear, mMonth, mDay);
//                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();

            }
        });


        ivDaily.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                // Toast.makeText(ReportActivity.this, "jyOTI", Toast.LENGTH_SHORT).show();
                if (llDaily.getVisibility() == View.VISIBLE) {
                    llDaily.setVisibility(View.GONE);
                    // ivDaily.setImageResource(R.mipmap.arrowup);
                } else {
                    llDaily.setVisibility(View.VISIBLE);
                    //  ivDaily.setImageResource(R.mipmap.arrowdown);
                }


            }
        });

        ivCustomer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                // Toast.makeText(ReportActivity.this, "jyOTI", Toast.LENGTH_SHORT).show();
                if (llcustomer.getVisibility() == View.VISIBLE) {
                    llcustomer.setVisibility(View.GONE);
                    // ivCustomer.setImageResource(R.mipmap.arrowup);
                } else {
                    llcustomer.setVisibility(View.VISIBLE);
                }
                // ivCustomer.setImageResource(R.mipmap.arrowdown);


            }
        });


        ivyestrday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                // Toast.makeText(ReportActivity.this, "jyOTI", Toast.LENGTH_SHORT).show();
                if (llyestrday.getVisibility() == View.VISIBLE) {
                    llyestrday.setVisibility(View.GONE);
                    // ivyestrday.setImageResource(R.mipmap.arrowup);
                } else {
                    llyestrday.setVisibility(View.VISIBLE);
                }
                //  ivyestrday.setImageResource(R.mipmap.arrowdown);


            }
        });


        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                String value = adapter.getItem(position).toString();
                autocomplete.setText(value);

                for (int i = 0; i < customers.size(); i++) {
                    if (value.equalsIgnoreCase(customers.get(i).customers_id)) {
                        name.setText(Html.fromHtml(String.valueOf(customers.get(i).name)));
                        month.setText(Html.fromHtml(String.valueOf(customers.get(i).created_at)));
                        amount.setText(Html.fromHtml(String.valueOf(customers.get(i).installment)));
                        //  etRoute.setText(Html.fromHtml(String.valueOf(customers.get(i).status)));

                        id = Integer.parseInt(customers.get(i).id);
                        group_id = customers.get(i).group_id;
                    }
                }
                autocomplete.setError(null);
            }
        });
        indexCoustmer();
        // weeklyReport();
        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {

                p1();


            }
        });


        mPrint1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                // Toast.makeText(ReportActivity.this, "jyOTI", Toast.LENGTH_SHORT).show();
                p2();


            }
        });


        mPrint2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                // Toast.makeText(ReportActivity.this, "jyOTI", Toast.LENGTH_SHORT).show();
                p3();


            }
        });


        mPrint12.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {
                // Toast.makeText(ReportActivity.this, "jyOTI", Toast.LENGTH_SHORT).show();
                p4();


            }
        });


        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {

                if (mScan.getText().equals("Connect")) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(ReportActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                requestpermission();
                                return;
                            }
                            startActivityForResult(enableBtIntent,
                                    REQUEST_ENABLE_BT);
                        } else {

                            if (ActivityCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                requestpermission();

                                return;
                            }
                            ListPairedDevices();
                            Intent connectIntent = new Intent(ReportActivity.this,
                                    DeviceListActivity.class);
                            startActivityForResult(connectIntent,
                                    REQUEST_CONNECT_DEVICE);

                        }
                    }

                } else if (mScan.getText().equals("Disconnect")) {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    stat.setText("");
                    stat.setText("Disconnected");
                    stat.setTextColor(Color.rgb(199, 59, 59));
                    mPrint.setEnabled(false);
                    mScan.setEnabled(true);
                    mScan.setText("Connect");
                } else if (Scan2.getText().equals("Disconnect")) {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    bpstatus1.setText("");
                    bpstatus1.setText("Disconnected");
                    bpstatus1.setTextColor(Color.rgb(199, 59, 59));
                    Scan2.setEnabled(false);
                    Scan2.setEnabled(true);
                    Scan2.setText("Connect");
                } else if (Scan3.getText().equals("Disconnect")) {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    bpstatus2.setText("");
                    bpstatus2.setText("Disconnected");
                    bpstatus2.setTextColor(Color.rgb(199, 59, 59));
                    Scan3.setEnabled(false);
                    Scan3.setEnabled(true);
                    Scan3.setText("Connect");
                }
            }
        });


        Scan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {

                if (mScan.getText().equals("Connect")) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(ReportActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                requestpermission();
                                return;
                            }
                            startActivityForResult(enableBtIntent,
                                    REQUEST_ENABLE_BT);
                        } else {

                            if (ActivityCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                requestpermission();

                                return;
                            }
                            ListPairedDevices();
                            Intent connectIntent = new Intent(ReportActivity.this,
                                    DeviceListActivity.class);
                            startActivityForResult(connectIntent,
                                    REQUEST_CONNECT_DEVICE);

                        }
                    }

                } else if (mScan.getText().equals("Disconnect")) {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    stat.setText("");
                    stat.setText("Disconnected");
                    stat.setTextColor(Color.rgb(199, 59, 59));
                    mPrint.setEnabled(false);
                    mScan.setEnabled(true);
                    mScan.setText("Connect");
                } else if (Scan2.getText().equals("Disconnect")) {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    bpstatus1.setText("");
                    bpstatus1.setText("Disconnected");
                    bpstatus1.setTextColor(Color.rgb(199, 59, 59));
                    Scan2.setEnabled(false);
                    Scan2.setEnabled(true);
                    Scan2.setText("Connect");
                } else if (Scan3.getText().equals("Disconnect")) {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    bpstatus2.setText("");
                    bpstatus2.setText("Disconnected");
                    bpstatus2.setTextColor(Color.rgb(199, 59, 59));
                    Scan3.setEnabled(false);
                    Scan3.setEnabled(true);
                    Scan3.setText("Connect");
                }
            }
        });
        Scan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {

                if (Scan3.getText().equals("Connect")) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(ReportActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                requestpermission();
                                return;
                            }
                            startActivityForResult(enableBtIntent,
                                    REQUEST_ENABLE_BT);
                        } else {

                            if (ActivityCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                requestpermission();

                                return;
                            }
                            ListPairedDevices();
                            Intent connectIntent = new Intent(ReportActivity.this,
                                    DeviceListActivity.class);
                            startActivityForResult(connectIntent,
                                    REQUEST_CONNECT_DEVICE);

                        }
                    }

                } else if (mScan.getText().equals("Disconnect")) {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    stat.setText("");
                    stat.setText("Disconnected");
                    stat.setTextColor(Color.rgb(199, 59, 59));
                    mPrint.setEnabled(false);
                    mScan.setEnabled(true);
                    mScan.setText("Connect");
                } else if (Scan3.getText().equals("Disconnect")) {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    bpstatus2.setText("");
                    bpstatus2.setText("Disconnected");
                    bpstatus2.setTextColor(Color.rgb(199, 59, 59));
                    Scan3.setEnabled(false);
                    Scan3.setEnabled(true);
                    Scan3.setText("Connect");
                }
            }
        });
        Scan12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {

                if (Scan12.getText().equals("Connect")) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(ReportActivity.this, "Message1", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!mBluetoothAdapter.isEnabled()) {
                            Intent enableBtIntent = new Intent(
                                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                requestpermission();
                                return;
                            }
                            startActivityForResult(enableBtIntent,
                                    REQUEST_ENABLE_BT);
                        } else {

                            if (ActivityCompat.checkSelfPermission(ReportActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                requestpermission();

                                return;
                            }
                            ListPairedDevices();
                            Intent connectIntent = new Intent(ReportActivity.this,
                                    DeviceListActivity.class);
                            startActivityForResult(connectIntent,
                                    REQUEST_CONNECT_DEVICE);

                        }
                    }

                } else if (Scan12.getText().equals("Disconnect")) {
                    if (mBluetoothAdapter != null)
                        mBluetoothAdapter.disable();
                    stat.setText("");
                    stat.setText("Disconnected");
                    stat.setTextColor(Color.rgb(199, 59, 59));
                    mPrint12.setEnabled(false);
                    Scan12.setEnabled(true);
                    Scan12.setText("Connect");
                }


            }
        });


        createpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (todays == null) {
                    Toast.makeText(context, "No Records found.", Toast.LENGTH_SHORT).show();
                } else {

                    downloadDialog = new ProgressDialog(context);
                    downloadDialog.setMessage("Downloading file...");
                    downloadDialog.setIndeterminate(false);
                    downloadDialog.setMax(100);
                    downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    downloadDialog.show();

                    // Constant.log("NAAN",myPurchases.getReceipt_no());
                    savePdfFile(context, todays);


                    boolean status = savePdfFile(context, todays);
                    if (status == false) {
                        Toast.makeText(context, "Unable to download", Toast.LENGTH_SHORT).show();
                        downloadDialog.dismiss();
                    } else {

                        Toast.makeText(ReportActivity.this, "File downloaded", Toast.LENGTH_SHORT).show();
                        downloadDialog.dismiss();
/*
                        try {
                            Uri uri = Uri.fromFile(incomefile).normalizeScheme();
                            String mime = get_mime_type(uri.toString());

                            Constant.log("mime", mime);
                            Constant.log("uri", uri.getPath());

                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "dailyreport" + ".pdf");

                            Uri uri1 = Uri.fromFile(file);


                            Intent share = new Intent();
                            share.setAction(Intent.ACTION_SEND);
                            share.setType("application/pdf");

                            share.putExtra(Intent.EXTRA_STREAM, uri1);


                            startActivity(Intent.createChooser(share, "Share"));



















                        } catch (Exception e) {
                            Constant.log("NAna", e.toString());
                            //   continue;
                        }
*/
                    }
                }
                //  }

            }
        });


        createpdfmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (monthY == null) {
                    Toast.makeText(context, "No Records found.", Toast.LENGTH_SHORT).show();
                } else {

                    downloadDialog = new ProgressDialog(context);
                    downloadDialog.setMessage("Downloading file...");
                    downloadDialog.setIndeterminate(false);
                    downloadDialog.setMax(100);
                    downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    downloadDialog.show();

                    // Constant.log("NAAN",myPurchases.getReceipt_no());
                    savePdfFile1(context, monthY);


                    boolean status = savePdfFile1(context, monthY);
                    if (status == false) {
                        Toast.makeText(context, "Unable to download", Toast.LENGTH_SHORT).show();
                        downloadDialog.dismiss();
                    } else {

                        Toast.makeText(ReportActivity.this, "File downloaded", Toast.LENGTH_SHORT).show();
                        downloadDialog.dismiss();
/*
                        try {



                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Monthlyreport" + ".pdf");



                            Uri uri = Uri.fromFile(incomefile).normalizeScheme();
                            String mime = get_mime_type(uri.toString());

                            Constant.log("mime", mime);
                            Constant.log("uri", uri.getPath());

                            Intent share = new Intent();
                            share.setAction(Intent.ACTION_SEND);
                            share.setType("application/pdf");


                            share.putExtra(Intent.EXTRA_STREAM,  file.getAbsoluteFile());
                            startActivity(Intent.createChooser(share, "Share"));

                        } catch (Exception e) {
                            Constant.log("NAna", e.toString());
                            //   continue;
                        }
*/
                    }
                }
                //  }

            }
        });

        createpdfcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (monthY == null) {
                    Toast.makeText(context, "No Records found.", Toast.LENGTH_SHORT).show();
                } else {

                    downloadDialog = new ProgressDialog(context);
                    downloadDialog.setMessage("Downloading file...");
                    downloadDialog.setIndeterminate(false);
                    downloadDialog.setMax(100);
                    downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    downloadDialog.show();

                    // Constant.log("NAAN",myPurchases.getReceipt_no());
                    savePdfFile2(context, customer_report);


                    boolean status = savePdfFile2(context, customer_report);
                    if (status == false) {
                        Toast.makeText(context, "Unable to download", Toast.LENGTH_SHORT).show();
                        downloadDialog.dismiss();
                    } else {

                        Toast.makeText(ReportActivity.this, "File downloaded", Toast.LENGTH_SHORT).show();
                        downloadDialog.dismiss();
/*
                        try {
                            Uri uri = Uri.fromFile(incomefile).normalizeScheme();
                            String mime = get_mime_type(uri.toString());

                            Constant.log("mime", mime);
                            Constant.log("uri", uri.getPath());


                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Monthlyreport" + ".pdf");



                            Intent share = new Intent();
                            share.setAction(Intent.ACTION_SEND);
                            share.setType("application/pdf");
                            share.putExtra(Intent.EXTRA_STREAM,  file.getAbsoluteFile());
                            startActivity(Intent.createChooser(share, "Share"));

                        } catch (Exception e) {
                            Constant.log("NAna", e.toString());
                            //   continue;
                        }
*/
                    }
                }
                //  }

            }
        });

        createpdfyestrday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (monthY == null) {
                    Toast.makeText(context, "No Records found.", Toast.LENGTH_SHORT).show();
                } else {

                    downloadDialog = new ProgressDialog(context);
                    downloadDialog.setMessage("Downloading file...");
                    downloadDialog.setIndeterminate(false);
                    downloadDialog.setMax(100);
                    downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    downloadDialog.show();

                    // Constant.log("NAAN",myPurchases.getReceipt_no());
                    savePdfFile3(context, yesterday_report);


                    boolean status = savePdfFile3(context, yesterday_report);
                    if (status == false) {
                        Toast.makeText(context, "Unable to download", Toast.LENGTH_SHORT).show();
                        downloadDialog.dismiss();
                    } else {

                        Toast.makeText(ReportActivity.this, "File downloaded", Toast.LENGTH_SHORT).show();
                        downloadDialog.dismiss();
/*
                        try {
                            Uri uri = Uri.fromFile(incomefile).normalizeScheme();
                            String mime = get_mime_type(uri.toString());

                            Constant.log("mime", mime);
                            Constant.log("uri", uri.getPath());


                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Monthlyreport" + ".pdf");



                            Intent share = new Intent();
                            share.setAction(Intent.ACTION_SEND);
                            share.setType("application/pdf");
                            share.putExtra(Intent.EXTRA_STREAM,  file.getAbsoluteFile());
                            startActivity(Intent.createChooser(share, "Share"));

                        } catch (Exception e) {
                            Constant.log("NAna", e.toString());
                            //   continue;
                        }
*/
                    }
                }
                //  }

            }
        });


        // CustomernewAdapter headlineAdapter1 = new CustomernewAdapter(ReportActivity.this);
        //  customerreport.setAdapter(headlineAdapter1);
/*
        LinearLayoutManager layoutManager09 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        customerreport.setLayoutManager(layoutManager09);
*/


        //   YestrdayAdapter yestrdayAdapter = new YestrdayAdapter(ReportActivity.this);
        //   yestrdayrecordRecycleview.setAdapter(yestrdayAdapter);
        //  LinearLayoutManager layoutManager091 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        //  yestrdayrecordRecycleview.setLayoutManager(layoutManager091);


        todayReport();
        monthlyreport();
        customerreport();
        Yestrdayreport();

    }

    private boolean checkForm() {
        amountTotal = b.tvEnterAmount.getText().toString().trim();

        if (amountTotal.isEmpty()) {
            Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
            b.tvEnterAmount.setError("Amount is required");
            return false;
        }
        return true;
    }

    private void amountWiseReport() {
        Log.e("sushiltoken", sessionModel.token);
        Call<TotalAmountModel> call = RetrofitClient.getInstance().getApi().amountReport("Bearer " + sessionModel.token, amountTotal);
        call.enqueue(new Callback<TotalAmountModel>() {
            @Override
            public void onResponse(Call<TotalAmountModel> call, Response<TotalAmountModel> response) {
                Log.d("sushil", "ok" + response.message() + ", code: " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().data.size() == 0) {
                        Toast.makeText(ReportActivity.this, "Dat Not Found", Toast.LENGTH_SHORT).show();

                    } else {
                        AmountAdapters amountAdapters = new AmountAdapters(response.body().data, ReportActivity.this);
                        b.rvAmount.setAdapter(amountAdapters);
                    }

                }
            }

            @Override
            public void onFailure(Call<TotalAmountModel> call, Throwable t) {

                Toast.makeText(ReportActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void monthWiseReport() {
        Log.e("sushiltoken", sessionModel.token);
        Call<DateModel> call = RetrofitClient.getInstance().getApi().monthReport("Bearer " + sessionModel.token, monthReport);
        call.enqueue(new Callback<DateModel>() {
            @Override
            public void onResponse(Call<DateModel> call, Response<DateModel> response) {
                Log.d("sushil", "ok" + response.message() + ", code: " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().data.size() == 0) {
                        Toast.makeText(ReportActivity.this, "Dat Not Found", Toast.LENGTH_SHORT).show();

                    } else {
                        DateAdapters dateAdapters = new DateAdapters(response.body().data, ReportActivity.this);
                        b.rvMonth.setAdapter(dateAdapters);
                    }

                }
            }

            @Override
            public void onFailure(Call<DateModel> call, Throwable t) {

                Toast.makeText(ReportActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dateWiseReport() {
        Log.e("sushiltoken", sessionModel.token);
        Call<DateModel> call = RetrofitClient.getInstance().getApi().dateReport("Bearer " + sessionModel.token, date);
        call.enqueue(new Callback<DateModel>() {
            @Override
            public void onResponse(Call<DateModel> call, Response<DateModel> response) {
                Log.d("sushil", "ok" + response.message() + ", code: " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().data.size() == 0) {
                        Toast.makeText(ReportActivity.this, "Dat Not Found", Toast.LENGTH_SHORT).show();

                    } else {
                        DateAdapters dateAdapters = new DateAdapters(response.body().data, ReportActivity.this);
                        b.rvDate.setAdapter(dateAdapters);
                    }

                }
            }

            @Override
            public void onFailure(Call<DateModel> call, Throwable t) {

                Toast.makeText(ReportActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean savePdfFile(Context context, List<TodayreportModel.Today> myPurchases) {


        boolean success = false;
        try {

            PDFCreater pdfCreater = new PDFCreater();
            try {

                String row = "";

                try {
                    row = getpayment(myPurchases, row);

                } catch (Exception e) {

                    e.printStackTrace();
                }

                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = "dailyreport" + ".pdf";

                File file = new File(path, name);
                //  file.createNewFile();
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] slide_836_bytes = row.getBytes();
                incomefile = new File(file.getAbsolutePath());
                success = pdfCreater.createPDF(name, String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), slide_836_bytes, "https://www.cpconverge.com/assets/admin/layout/img/converge-logo.png");
                return success;

            } catch (Exception e) {
                e.printStackTrace();
                Constant.log("NAANA", e.toString());
            }

            // return success;
        } catch (Exception e) {
            Constant.log(TAG, e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    private boolean savePdfFile1(Context context, List<MonthlyreportModel.Today> myPurchases) {


        boolean success = false;
        try {

            PDFCreater pdfCreater = new PDFCreater();
            try {

                String row = "";

                try {
                    row = getpayment1(myPurchases, row);

                } catch (Exception e) {

                    e.printStackTrace();
                }

                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = "Monthlyreport" + ".pdf";

                File file = new File(path, name);
                //  file.createNewFile();
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] slide_836_bytes = row.getBytes();
                incomefile = new File(file.getAbsolutePath());
                success = pdfCreater.createPDF(name, String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), slide_836_bytes, "https://www.cpconverge.com/assets/admin/layout/img/converge-logo.png");
                return success;

            } catch (Exception e) {
                e.printStackTrace();
                Constant.log("NAANA", e.toString());
            }

            // return success;
        } catch (Exception e) {
            Constant.log(TAG, e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    private boolean savePdfFile2(Context context, List<CustomerreportModel.Customer_report> myPurchases) {


        boolean success = false;
        try {

            PDFCreater pdfCreater = new PDFCreater();
            try {

                String row = "";

                try {
                    row = getpayment2(myPurchases, row);

                } catch (Exception e) {

                    e.printStackTrace();
                }

                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = "Customer report" + ".pdf";

                File file = new File(path, name);
                //  file.createNewFile();
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] slide_836_bytes = row.getBytes();
                incomefile = new File(file.getAbsolutePath());
                success = pdfCreater.createPDF(name, String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), slide_836_bytes, "https://www.cpconverge.com/assets/admin/layout/img/converge-logo.png");
                return success;

            } catch (Exception e) {
                e.printStackTrace();
                Constant.log("NAANA", e.toString());
            }

            // return success;
        } catch (Exception e) {
            Constant.log(TAG, e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    private boolean savePdfFile3(Context context, List<YestrdayreportModel.Yesterday_report> myPurchases) {


        boolean success = false;
        try {

            PDFCreater pdfCreater = new PDFCreater();
            try {

                String row = "";

                try {
                    row = getpayment3(myPurchases, row);

                } catch (Exception e) {

                    e.printStackTrace();
                }

                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                String name = "Yestrday report" + ".pdf";

                File file = new File(path, name);
                //  file.createNewFile();
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                byte[] slide_836_bytes = row.getBytes();
                incomefile = new File(file.getAbsolutePath());
                success = pdfCreater.createPDF(name, String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)), slide_836_bytes, "https://www.cpconverge.com/assets/admin/layout/img/converge-logo.png");
                return success;

            } catch (Exception e) {
                e.printStackTrace();
                Constant.log("NAANA", e.toString());
            }

            // return success;
        } catch (Exception e) {
            Constant.log(TAG, e.getMessage());
            e.printStackTrace();
        }

        return false;
    }


    @Nullable
    private String getpayment(List<TodayreportModel.Today> visaFinanceDataDetail, String rows) {
        try {


            String csvString = "";
            //  String csvString11="";

            csvString = "<html>" +
                    "    <head>" +
                    "        <style>\n" +
                    "table, th, td {\n" +
                    "  border:1px solid black;\n" +
                    "}" +

                    "        </style>" +
                    "    </head>" +
                    "    <body>" +

                    csvString + ("<h2>Daily Report</h2>") +
                    csvString + ("<table style=\"width:100%\">");


            csvString = csvString + ("<tr>\n" +
                    "    <th>Bill No</th>\n" +
                    "    <th>Customer Code</th>\n" +
                    "    <th>Name </th>\n" +
                    "    <th>Total Amount :</th>\n" +
                    "    <th>Collection Person Name</th>\n" +
                    "  </tr>");


            for (int i = 0; i < visaFinanceDataDetail.size(); i++) {
                csvString = csvString + (" <tr>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).billings_id + "</td>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).customer.customers_id + "</td>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).customer.name + "</td>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).customer.total_amount + "</td>" +
                        "    <td>" + sessionManager.geFirstName() + "</td>" +
                        "  </tr>");
            }


            csvString = csvString + ("<td style=\"height:10px\"></td>" + "  </table></body></html>");


            Constant.log(TAG, "Rows:  " + csvString);


            rows = rows + csvString;
            //  return csvString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    private String getpayment1(List<MonthlyreportModel.Today> visaFinanceDataDetail, String rows) {
        try {


            String csvString = "";
            //  String csvString11="";

            csvString = "<html>" +
                    "    <head>" +
                    "        <style>\n" +
                    "table, th, td {\n" +
                    "  border:1px solid black;\n" +
                    "}" +

                    "        </style>" +
                    "    </head>" +
                    "    <body>" +

                    csvString + ("<h2>Monthly Report</h2>") +
                    csvString + ("<table style=\"width:100%\">");


            csvString = csvString + ("<tr>\n" +
                    "    <th>Date</th>\n" +
                    "    <th>Bill No</th>\n" +
                    //   "    <th>Month</th>\n" +
                    "    <th>Collection Person Name</th>\n" +
                    "    <th>Total</th>\n" +
                    "  </tr>");


            for (int i = 0; i < visaFinanceDataDetail.size(); i++) {
                csvString = csvString + (" <tr>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).created + "</td>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).billings_id + "</td>\n" +
                        //   "    <td>"+visaFinanceDataDetail.get(i).customer.month+"</td>\n" +
                        "    <td>" + sessionManager.geFirstName() + "</td>" +
                        "    <td>" + visaFinanceDataDetail.get(i).customer.total_amount + "</td>" +
                        "  </tr>");
            }


            csvString = csvString + ("<td style=\"height:10px\"></td>" + "  </table></body></html>");


            Constant.log(TAG, "Rows:  " + csvString);


            rows = rows + csvString;
            //  return csvString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    private String getpayment2(List<CustomerreportModel.Customer_report> visaFinanceDataDetail, String rows) {
        try {


            String csvString = "";
            //  String csvString11="";

            csvString = "<html>" +
                    "    <head>" +
                    "        <style>\n" +
                    "table, th, td {\n" +
                    "  border:1px solid black;\n" +
                    "}" +

                    "        </style>" +
                    "    </head>" +
                    "    <body>" +

                    csvString + ("<h2>Customer Report</h2>") +
                    csvString + ("<table style=\"width:100%\">");


            csvString = csvString + ("<tr>\n" +
                    "    <th>Customer_id</th>\n" +
                    "    <th>Bill No</th>\n" +
                    "    <th>Name</th>\n" +
                    "    <th>Total</th>\n" +
                    "    <th>Collection Person Name</th>\n" +
                    "  </tr>");


            for (int i = 0; i < visaFinanceDataDetail.size(); i++) {
                csvString = csvString + (" <tr>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).customer.customers_id + "</td>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).billings_id + "</td>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).customer.name + "</td>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).customer.total_amount + "</td>" +
                        "    <td>" + sessionManager.geFirstName() + "</td>" +

                        "  </tr>");
            }


            csvString = csvString + ("<td style=\"height:10px\"></td>" + "  </table></body></html>");


            Constant.log(TAG, "Rows:  " + csvString);


            rows = rows + csvString;
            //  return csvString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }

    private String getpayment3(List<YestrdayreportModel.Yesterday_report> visaFinanceDataDetail, String rows) {
        try {


            String csvString = "";
            //  String csvString11="";

            csvString = "<html>" +
                    "    <head>" +
                    "        <style>\n" +
                    "table, th, td {\n" +
                    "  border:1px solid black;\n" +
                    "}" +

                    "        </style>" +
                    "    </head>" +
                    "    <body>" +

                    csvString + ("<h2>Yestrday Report</h2>") +
                    csvString + ("<table style=\"width:100%\">");


            csvString = csvString + ("<tr>\n" +
                    "    <th>Bill No</th>\n" +
                    "    <th>Customer_id </th>\n" +
                    "    <th>Name</th>\n" +
                    "    <th>Total</th>\n" +
                    "    <th>Collection Person Name</th>\n" +

                    "  </tr>");


            for (int i = 0; i < visaFinanceDataDetail.size(); i++) {
                csvString = csvString + (" <tr>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).billings_id + "</td>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).customers_id + "</td>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).customer.name + "</td>\n" +
                        "    <td>" + visaFinanceDataDetail.get(i).customer.total_amount + "</td>" +
                        "    <td>" + sessionManager.geFirstName() + "</td>" +

                        "  </tr>");
            }


            csvString = csvString + ("<td style=\"height:10px\"></td>" + "  </table></body></html>");


            Constant.log(TAG, "Rows:  " + csvString);


            rows = rows + csvString;
            //  return csvString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows;
    }


    public String get_mime_type(String url) {
        String ext = MimeTypeMap.getFileExtensionFromUrl(url);
        String mime = null;
        if (ext != null) {
            mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return mime;
    }


    private void todayReport() {

        Log.e("sushiltoken", sessionModel.token);
        Call<TodayreportModel> call = RetrofitClient.getInstance().getApi().customerreport(String.format("Bearer %s", sessionModel.token));
        call.enqueue(new Callback<TodayreportModel>() {
            @Override
            public void onResponse(Call<TodayreportModel> call, Response<TodayreportModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());
                if (response.isSuccessful()) {
                    Constant.log("snxkns", sessionManager.geFirstName());                 //   Toast.makeText(ReportActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    todays.clear();
                    todays = response.body().today_report;

                    DailyReportAdapter dailyReportAdapter = new DailyReportAdapter(response.body().today_report, ReportActivity.this, sessionManager);

                    recordRecycleview.setAdapter(dailyReportAdapter);
                    String total = String.valueOf(dailyReportAdapter.grandTotal());
                    tvDaily.setText("Total Collection of the Day :     " + "₹ " + total);


                }
            }

            @Override
            public void onFailure(Call<TodayreportModel> call, Throwable t) {

                Toast.makeText(ReportActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void monthlyreport() {
        Log.e("sushiltoken", sessionModel.token);
        Call<MonthlyreportModel> call = RetrofitClient.getInstance().getApi().monthlyreport(String.format("Bearer %s", sessionModel.token));
        call.enqueue(new Callback<MonthlyreportModel>() {
            @Override
            public void onResponse(Call<MonthlyreportModel> call, Response<MonthlyreportModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());
                if (response.isSuccessful()) {

                    //   Toast.makeText(ReportActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    monthY.clear();
                    monthY = response.body().monthly_report;
                    CustomerReportAdapter CustomerReportAdapter = new CustomerReportAdapter(response.body().monthly_report, ReportActivity.this);
                    monthelyreport.setAdapter(CustomerReportAdapter);


                }
            }

            @Override
            public void onFailure(Call<MonthlyreportModel> call, Throwable t) {

                Toast.makeText(ReportActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void customerreport() {

        Log.e("sushiltoken", sessionModel.token);
        Call<CustomerreportModel> call = RetrofitClient.getInstance().getApi().customersreports(String.format("Bearer %s", sessionModel.token));
        call.enqueue(new Callback<CustomerreportModel>() {
            @Override
            public void onResponse(Call<CustomerreportModel> call, Response<CustomerreportModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());
                if (response.isSuccessful()) {


                    //   Toast.makeText(ReportActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    customer_report.clear();
                    customer_report = response.body().customer_report;
                    CustomernewReportAdapter CustomerReportAdapter = new CustomernewReportAdapter(response.body().customer_report, ReportActivity.this);
                    customerreport.setAdapter(CustomerReportAdapter);


                }
            }

            @Override
            public void onFailure(Call<CustomerreportModel> call, Throwable t) {

                Toast.makeText(ReportActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Yestrdayreport() {

        Log.e("sushiltoken", sessionModel.token);
        Call<YestrdayreportModel> call = RetrofitClient.getInstance().getApi().yestradyreport(String.format("Bearer %s", sessionModel.token));
        call.enqueue(new Callback<YestrdayreportModel>() {
            @Override
            public void onResponse(Call<YestrdayreportModel> call, Response<YestrdayreportModel> response) {
                Log.d("sushil", "ok" + response.message() + ", code: " + response.code());
                if (response.isSuccessful()) {


                    //   Toast.makeText(ReportActivity.this, response.body().message, Toast.LENGTH_SHORT).show();
                    yesterday_report.clear();
                    yesterday_report = response.body().yesterday_report;
                    YestrdayReportAdapter CustomerReportAdapter1 = new YestrdayReportAdapter(response.body().yesterday_report, ReportActivity.this);
                    yestrdayrecordRecycleview.setAdapter(CustomerReportAdapter1);


                }
            }

            @Override
            public void onFailure(Call<YestrdayreportModel> call, Throwable t) {

                Toast.makeText(ReportActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void indexCoustmer() {
        // Log.e("sushiltoken", sessionModel.token);
        Call<CoustmerindexModel> call = RetrofitClient.getInstance().getApi().indexCoustmer(String.format("Bearer %s", sessionModel.token));
        call.enqueue(new Callback<com.kaas.svjmchitfund.Module.CoustmerindexModel>() {
            @Override
            public void onResponse(Call<CoustmerindexModel> call, Response<CoustmerindexModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());
                if (response.isSuccessful()) {
                    ivCustomer1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //  llDe.setVisibility(View.VISIBLE);

                            if (llDe.getVisibility() == View.VISIBLE) {
                                llDe.setVisibility(View.GONE);
                                rvCoustmer.setVisibility(View.GONE);
                                // ivCustomer.setImageResource(R.mipmap.arrowup);
                            } else {
                                llDe.setVisibility(View.VISIBLE);
                                rvCoustmer.setVisibility(View.VISIBLE);
                                CoustmerReportAdapter coustmerReportAdapter = new CoustmerReportAdapter(response.body().customer, ReportActivity.this);
                                rvCoustmer.setAdapter(coustmerReportAdapter);
                            }
                        }


                    });


                    customers.clear();
                    customers = (response.body().customer);
                    for (int i = 0; i < response.body().customer.size(); i++) {
                        customercode.add(response.body().customer.get(i).customers_id);
                    }

                    adapter = new ArrayAdapter<String>
                            (context, android.R.layout.select_dialog_item, customercode);

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

                Toast.makeText(ReportActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void p1() {

        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String header = "";
                    String he = "";
                    String blank = "";
                    String header2 = "";
                    String BILL = "";


                    String time = "";
                    String copy = "";
                    String checktop_status = "";

                    blank = "\n";
                    he = "     Daily Report\n";
                    he = he + "--------------------------------\n";


                    //   he = he + "********************************\n";

                    header = "B.No    Code    Month    Total\n";


                    os.write(blank.getBytes());
                    os.write(he.getBytes());
                    os.write(header.getBytes());


                    for (int i = 0; i < todays.size(); i++) {
                        BILL = todays.get(i).billings_id + "     " + " " + todays.get(i).customer.customers_id + "     " + todays.get(i).customer.month + "     " + todays.get(i).customer.total_amount + "\n";
                        os.write(BILL.getBytes());
                    }


                    time = "\n" + formattedDate + "\n" + "--------------------------------\n\n\n";

                    os.write(checktop_status.getBytes());
                    os.write(time.getBytes());
                    os.write(copy.getBytes());


                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 150;
                    os.write(intToByteArray(h));
                    int n = 170;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {
                    Log.e("PrintActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    public void p2() {

        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String header = "";
                    String he = "";
                    String blank = "";
                    String header2 = "";
                    String BILL = "";


                    String time = "";
                    String copy = "";
                    String checktop_status = "";

                    blank = "\n";
                    he = "     Monthly Report\n";
                    he = he + "--------------------------------\n";


                    //   he = he + "********************************\n";

                    header = "Date      B.No     Month    Total\n";


                    os.write(blank.getBytes());
                    os.write(he.getBytes());
                    os.write(header.getBytes());


                    for (int i = 0; i < monthY.size(); i++) {
                        BILL = monthY.get(i).created + " " + " " + monthY.get(i).id + "    " + monthY.get(i).customer.month + "    " + monthY.get(i).customer.total_amount + "\n";
                        os.write(BILL.getBytes());
                    }


                    time = "\n" + formattedDate + "\n" + "--------------------------------\n\n\n";

                    os.write(checktop_status.getBytes());
                    os.write(time.getBytes());
                    os.write(copy.getBytes());


                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 150;
                    os.write(intToByteArray(h));
                    int n = 170;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {
                    Log.e("PrintActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    public void p3() {

        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String header = "";
                    String he = "";
                    String blank = "";
                    String header2 = "";
                    String BILL = "";


                    String time = "";
                    String copy = "";
                    String checktop_status = "";

                    blank = "\n";
                    he = "     Customer Report\n";
                    he = he + "--------------------------------\n";


                    //   he = he + "********************************\n";

                    header = "Customer ID      B.No     Name    Total\n";


                    os.write(blank.getBytes());
                    os.write(he.getBytes());
                    os.write(header.getBytes());


                    for (int i = 0; i < customer_report.size(); i++) {
                        BILL = customer_report.get(i).customers_id + " " + " " + customer_report.get(i).id + "    " + customer_report.get(i).customer.name + "    " + customer_report.get(i).customer.total_amount + "\n";
                        os.write(BILL.getBytes());
                    }


                    time = "\n" + formattedDate + "\n" + "--------------------------------\n\n\n";

                    os.write(checktop_status.getBytes());
                    os.write(time.getBytes());
                    os.write(copy.getBytes());


                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 150;
                    os.write(intToByteArray(h));
                    int n = 170;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {
                    Log.e("PrintActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    public void p4() {

        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();
                    String header = "";
                    String he = "";
                    String blank = "";
                    String header2 = "";
                    String BILL = "";


                    String time = "";
                    String copy = "";
                    String checktop_status = "";

                    blank = "\n";
                    he = "     Yestrday Report\n";
                    he = he + "--------------------------------\n";


                    //   he = he + "********************************\n";

                    header = "Customer ID      B.No     Name    Total\n";


                    os.write(blank.getBytes());
                    os.write(he.getBytes());
                    os.write(header.getBytes());


                    for (int i = 0; i < yesterday_report.size(); i++) {
                        BILL = yesterday_report.get(i).customers_id + " " + " " + yesterday_report.get(i).billings_id + "    " + yesterday_report.get(i).customer.name + "    " + yesterday_report.get(i).customer.total_amount + "\n";
                        os.write(BILL.getBytes());
                    }


                    time = "\n" + formattedDate + "\n" + "--------------------------------\n\n\n";

                    os.write(checktop_status.getBytes());
                    os.write(time.getBytes());
                    os.write(copy.getBytes());


                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 150;
                    os.write(intToByteArray(h));
                    int n = 170;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));


                } catch (Exception e) {
                    Log.e("PrintActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /* Terminate bluetooth connection and close all sockets opened */
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        requestpermission();
                        return;
                    }
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    //
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(ReportActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(ReportActivity.this, "Not connected to any device", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        if (ActivityCompat.checkSelfPermission(this, BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestpermission();
            return;
        }
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            if (ActivityCompat.checkSelfPermission(this, BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestpermission();
                return;
            }
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    private void requestpermission() {

        requestPermissions(new String[]{BLUETOOTH_CONNECT, BLUETOOTH_SCAN}, PERMISSION_REQUEST_CODE);

    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();

            stat.setText("");
            stat.setText("Connected");
            stat.setTextColor(Color.rgb(97, 170, 74));
            mPrint.setEnabled(true);
            mScan.setText("Disconnect");

            bpstatus1.setText("Connected");
            bpstatus1.setTextColor(Color.rgb(97, 170, 74));
            mPrint1.setEnabled(true);
            Scan2.setText("Disconnect");


            bpstatus2.setText("Connected");
            bpstatus2.setTextColor(Color.rgb(97, 170, 74));
            mPrint2.setEnabled(true);
            Scan3.setText("Disconnect");


            bpstatus12.setText("Connected");
            bpstatus12.setTextColor(Color.rgb(97, 170, 74));
            mPrint12.setEnabled(true);
            Scan12.setText("Disconnect");


        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }


}