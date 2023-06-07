package com.kaas.svjmchitfund;

import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.BilllistModel;
import com.kaas.svjmchitfund.Module.SessionModel;

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

public class PrintbillingActivity extends Activity implements Runnable {
    LinearLayout ll_1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    protected static final String TAG = "EditschemebillingActivity";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint;
    /* Get time and date */
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm a", Locale.US);
    final String formattedDate = df.format(c.getTime());
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;

    TextView stat;
    int printstat;

    LinearLayout layout;

    TextView edtname, edtmobileno, edtplase, edtmonth, edtprvoius, edtbillno, edtamount, edttotal,Accountno,shopname,gstno;

    SessionManager sessionManager;
    SessionModel sessionModel;
    List<BilllistModel.Billing> billing = new ArrayList<>();
    Context context;
    String Autocomplete = "";
    // String   Customercode="",name="",phone="",plase="",Month="",Prvoius="",Billno="",Amount="",Total="";
    ProgressDialog mProgressDialog;
    AutoCompleteTextView autocomplete;

    ArrayList<String> customer_idlist = new ArrayList<>();

    ArrayAdapter<String> adapter;

    String[] arr = {"Paries,France", "PA,United States", "Parana,Brazil",
            "Padua,Italy", "Pasadena,CA,United States"};


    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editschemebilling);
        context = this;
        edtname = findViewById(R.id.edtname);
        // edtcustomercode = findViewById(R.id.edtcustomercode);
        edtmobileno = findViewById(R.id.edtmobileno);
        edtplase = findViewById(R.id.edtplase);
        edtprvoius = findViewById(R.id.edtprvoius);
        edtbillno = findViewById(R.id.edtbillno);
        edtamount = findViewById(R.id.edtamount);
        edttotal = findViewById(R.id.edttotal);
        ll_1 = findViewById(R.id.ll_1);

      //  Accountno = findViewById(R.id.Accountno);

        stat = findViewById(R.id.bpstatus);
        mScan = findViewById(R.id.Scan);
        mPrint = findViewById(R.id.mPrint);

        autocomplete =
                findViewById(R.id.autoCompleteTextView1);
        sessionManager = new SessionManager(PrintbillingActivity.this);
        // connect();

        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);
        ll_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {

                finish();


            }
        });


        autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id1) {
                String value = adapter.getItem(position).toString();
                autocomplete.setText(value);

                for (int i = 0; i < billing.size(); i++) {
                    if (value.equalsIgnoreCase(String.valueOf(billing.get(i).billings_id))) {
                        edtname.setText(Html.fromHtml(String.valueOf(billing.get(i).customer.name)));


                        //   edtcustomercode.setText(Html.fromHtml(String.valueOf(billing.get(i).customer_id)));
                        edtplase.setText(Html.fromHtml(String.valueOf(billing.get(i).customer.place)));
                       // edtmonth.setText(Html.fromHtml(String.valueOf(billing.get(i).customer.month)));


                        edtbillno.setText(Html.fromHtml(String.valueOf(String.valueOf(billing.get(i).billings_id))));

                     //   Accountno.setText(Html.fromHtml(String.valueOf(String.valueOf(billing.get(i).account_no))));

                        edtmobileno.setText(Html.fromHtml(String.valueOf(billing.get(i).customer.mobile)));
                     //   edtamount.setText(Html.fromHtml(String.valueOf(billing.get(i).customer.installment)));
                        edttotal.setText(Html.fromHtml(String.valueOf(billing.get(i).customer.total_amount)));


                       edtprvoius.setText(Html.fromHtml(String.valueOf(billing.get(i).customer.installment)));


                        //   id=customers.get(i).id;
                        //group_id=customers.get(i).group_id;
                    }
                }
                autocomplete.setError(null);
            }
        });

        indexCoustmer();

        mPrint.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {

                p1();


            }
        });


        mScan = findViewById(R.id.Scan);

        mScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {

                if (mScan.getText().equals("Connect")) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(PrintbillingActivity.this, "Message1", Toast.LENGTH_SHORT).show();
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

                            if (ActivityCompat.checkSelfPermission(PrintbillingActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
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
                            Intent connectIntent = new Intent(PrintbillingActivity.this,
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
                }
            }
        });

    }


    private void indexCoustmer() {
        Log.e("sushiltoken", sessionModel.token);
        Call<BilllistModel> call = RetrofitClient.getInstance().getApi().billingindex(String.format("Bearer %s", sessionModel.token));
        call.enqueue(new Callback<BilllistModel>() {
            @Override
            public void onResponse(Call<BilllistModel> call, Response<BilllistModel> response) {
                Log.d("sushil", "ok" + response.isSuccessful() + ", code: " + response.code());
                if (response.isSuccessful()) {


                    billing.clear();
                    billing = (response.body().billing);
                    for (int i = 0; i < response.body().billing.size(); i++) {
                        customer_idlist.add(String.valueOf(response.body().billing.get(i).billings_id));
                    }

                    adapter = new ArrayAdapter<String>
                            (context, android.R.layout.select_dialog_item, customer_idlist);

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
            public void onFailure(Call<BilllistModel> call, Throwable t) {

                Toast.makeText(PrintbillingActivity.this, "On Failure " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkForm() {
        //  group_id = etGroup.getText().toString().trim();


        Autocomplete = autocomplete.getText().toString().trim();

        if (Autocomplete.isEmpty()) {
            autocomplete.setError("Enter a group id");
            autocomplete.requestFocus();
            return false;
        }

        return true;
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
                    String vio = "";
                    String header3 = "";
                    String mvdtail = "";
                    String header4 = "";
                    String header5 = "";
                    String header6 = "";
                    String header7 = "";
                    String header8 = "";
                    String header9 = "";
                    String header1 = "";

                    String offname = "";
                    String month = "";
                    String previous = "";
                    String billno = "";
                    String accountno = "";
                    String amount = "";
                    String total = "";
                    String Shopname = "";
                    String Gstno = "";
                    String p = "";
                    String time = "";
                    String copy = "";
                    String checktop_status = "";

                    blank = "\n";

                    header1 = " shop name ";
                    Shopname = shopname.getText().toString() + "\n";
                    Shopname = Shopname
                    ;


                    header2 = " Gst NO ";
                    Gstno = gstno.getText().toString() + "\n"+"--------------------------------";
                    Gstno = Gstno
                    ;



                    //   he = he + "********************************\n";
                    header5 = "Bill No. : ";
                    billno = edtbillno.getText().toString() + "\n";
                    billno = billno;


                    header7 = "Account No. : ";
                 //   accountno = Accountno.getText().toString() + "\n";
                    accountno = accountno;



                    header3 = "Name : ";
                    vio = edtname.getText().toString() + "\n";
                    vio = vio
                         ;



                    header6 = "Oepn Blance : ";
                    previous = edtprvoius.getText().toString() + "\n";
                    previous = previous
                         ;
           /*         header8 = "Paid Amount. : ";
                    amount = edtamount.getText().toString() + "\n";
                    amount = amount
                          ;*/
                    header9 = "Grand Val. : ";
                    total = edttotal.getText().toString() + "\n";
                    total = total
                          ;




                    time = formattedDate + "\n"+"--------------------------------\n\n\n";




                    os.write(blank.getBytes());
                    os.write(header1.getBytes());
                    os.write(Shopname.getBytes());




                    os.write(header2.getBytes());
                    os.write(Gstno.getBytes());


                    os.write(header7.getBytes());
                    os.write(accountno.getBytes());

                    os.write(header5.getBytes());
                    os.write(billno.getBytes());


                    os.write(header3.getBytes());
                    os.write(vio.getBytes());


                    os.write(header6.getBytes());
                    os.write(previous.getBytes());
/*
                    os.write(header8.getBytes());
                    os.write(amount.getBytes());*/
                    os.write(header9.getBytes());
                    os.write(total.getBytes());


























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
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(PrintbillingActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(PrintbillingActivity.this, "Not connected to any device", Toast.LENGTH_SHORT).show();
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

        requestPermissions(new String[]{BLUETOOTH_CONNECT,BLUETOOTH_SCAN}, PERMISSION_REQUEST_CODE);

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

