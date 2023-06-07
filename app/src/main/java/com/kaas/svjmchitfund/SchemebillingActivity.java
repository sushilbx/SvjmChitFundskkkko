package com.kaas.svjmchitfund;

import static android.Manifest.permission.BLUETOOTH_CONNECT;
import static android.Manifest.permission.BLUETOOTH_SCAN;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.BillModel;
import com.kaas.svjmchitfund.Module.CoustmerindexModel;
import com.kaas.svjmchitfund.Module.EditCoustmerModel;
import com.kaas.svjmchitfund.Module.SessionModel;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchemebillingActivity extends Activity implements Runnable  {
    final Calendar myCalendar= Calendar.getInstance();
    private static final int PERMISSION_REQUEST_CODE = 200;
    protected static final String TAG = "EditschemebillingActivity";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    Button mScan, mPrint;
    TextView stat;
    ImageView arrow,calender;
    int printstat;
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



   String printpaidamt="";
    byte FONT_TYPE;
    private static BluetoothSocket btsocket;
    private static OutputStream btoutputstream;
    EditText edtDateBilling,edtname,edtmobileno,edtpalse,edtmonth,edtprvoius,edtbillno,edtamount,edttotal, shopname,gstno;
Button cansel,printbtn;
LinearLayout ll_1;
    DatePickerDialog.OnDateSetListener date;
      String customers_id;

    SessionManager sessionManager;
    SessionModel sessionModel;
    Context context;
    ArrayAdapter<String> adapter ;
    String[] arr = { "Paries,France", "PA,United States","Parana,Brazil",
            "Padua,Italy", "Pasadena,CA,United States"};
    List<CoustmerindexModel.Customer> customers=new ArrayList<>();
    ArrayList<String> customercode= new ArrayList<>();
    String   Customercode="",name="",phone="",plase="",Month="",Prvoius="",Billno="",Amount="",Total="";
    AutoCompleteTextView autocomplete;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemebilling);
        context = this;
       // edtcustomercode = findViewById(R.id.edtcustomercode);
        edtname = findViewById(R.id.edtname);
        arrow = findViewById(R.id.arrow);
        edtmobileno = findViewById(R.id.edtmobileno);
        ll_1 = findViewById(R.id.ll_1);
        edtpalse = findViewById(R.id.edtpalse);
      //  edtmonth = findViewById(R.id.edtmonth);
      //  edtprvoius = findViewById(R.id.edtprvoius);
       // edtbillno = findViewById(R.id.edtbillno);
        edtamount = findViewById(R.id.edtamount);
        edttotal = findViewById(R.id.edttotal);
        printbtn = findViewById(R.id.printbtn);
        cansel = findViewById(R.id.cansel);
        edtDateBilling = findViewById(R.id.edtDateBilling);



        stat = findViewById(R.id.bpstatus);
        mScan = findViewById(R.id.Scan);
        mPrint = findViewById(R.id.mPrint);
        autocomplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);

        sessionManager = new SessionManager(SchemebillingActivity.this);
        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);



           date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        edtDateBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showrdialogpassword();
            }
        });






        ll_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View mView) {

          finish();


            }
        });

        cansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showruledialog();

            }


        });









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
                        Toast.makeText(SchemebillingActivity.this, "Message1", Toast.LENGTH_SHORT).show();
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

                            if (ActivityCompat.checkSelfPermission(SchemebillingActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
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
                            Intent connectIntent = new Intent(SchemebillingActivity.this,
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













/*
        edtamount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                  @Override
                  public void onFocusChange(View v, boolean hasFocus) {
                      if (!hasFocus)
                      {
                          int y=0;
                          int x=Integer.parseInt(edtprvoius.getText().toString());

                          if (!edtamount.getText().toString().isEmpty())
                          {
                            y =Integer.parseInt(edtamount.getText().toString());
                          }


                          //sum these two numbers
                          int z=x+y;
                          edttotal.setText(String.valueOf(z));
                      }
                  }
              });
*/


/*
        edttotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    int x=Integer.parseInt(edtprvoius.getText().toString());
                    int y=Integer.parseInt(edtamount.getText().toString());

                    //sum these two numbers
                    int z=x+y;
                    edttotal.setText(String.valueOf(z));
                }
            }
        });
*/

        indexCoustmer();

        printbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {

                    billadd();


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


                        edtname.setText(Html.fromHtml(String.valueOf(customers.get(i).name)));
                        edtmobileno.setText(Html.fromHtml(String.valueOf(customers.get(i).mobile)));
                        edtpalse.setText(Html.fromHtml(String.valueOf(customers.get(i).place)));
                      // edtmonth.setText(Html.fromHtml(String.valueOf(customers.get(i).month)));
                       // edtbillno.setText(Html.fromHtml(String.valueOf(customers.get(i).customer_id)));
                        edtamount.setText(Html.fromHtml(String.valueOf(customers.get(i).group.amount)));
                        printpaidamt=customers.get(i).total_amount;

                        int x=Integer.parseInt(customers.get(i).group.amount);
                        int y=Integer.parseInt(customers.get(i).total_amount);

                        //sum these two numbers
                        int z=x+y;
                        edttotal.setText(String.valueOf(z));
                       // edtprvoius.setText(Html.fromHtml(String.valueOf(customers.get(i).installment)));




                      //  Date.setText(Html.fromHtml(String.valueOf(customers.get(i).updated_at)));




                        customers_id= String.valueOf(customers.get(i).id);


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
    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        edtDateBilling.setText(dateFormat.format(myCalendar.getTime()));
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

                    header1 = " Sri Venkateshwara Jewellery Mart Kollegal +  \"\\n\"+\"";
                    Shopname = Shopname
                    ;


                    header2 = " GSTIN-AARPV6283B1Z2 ";
                    //Gstno = gstno.getText().toString() + "\n"+"--------------------------------";
                    Gstno = Gstno
                    ;


                    time = formattedDate + "\n"+"--------------------------------\n\n\n";






                    //   he = he + "********************************\n";
                    header5 = "Bill No. : ";
                 //   billno = edtbillno.getText().toString() + "\n";
                    billno = billno;


                    header7 = "Account No. : ";
                    accountno ="14337";
                    accountno = accountno;



                    header3 = "Name : ";
                    vio = edtname.getText().toString() + "\n";
                    vio = vio
                    ;



                    header6 = "Oepn Balance : ";
                    previous = edtamount.getText().toString() + "\n";
                    previous = previous
                    ;



                   header8 = "Paid Amount. : ";
                    amount = printpaidamt + "\n";
                    amount = amount
                          ;



                    header9 = "Grand Total. : ";
                    total = edttotal.getText().toString() + "\n";
                    total = total
                    ;




                  //  time = formattedDate + "\n"+"--------------------------------\n\n\n";




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

                    os.write(header8.getBytes());
                    os.write(amount.getBytes());

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
                    Intent connectIntent = new Intent(SchemebillingActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toast.makeText(SchemebillingActivity.this, "Not connected to any device", Toast.LENGTH_SHORT).show();
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





    private void billadd() {

        Call<BillModel> call = RetrofitClient.getInstance().getApi().
                billing("Bearer " + sessionManager.gettokan(),customers_id,"SRI VENKATESHWARA JEWELLERYMARTKOLLEG","GST-AARPV6283B1Z2","14337");
        call.enqueue(new Callback<BillModel>() {
            @Override
            public void onResponse(Call<BillModel> call, Response<BillModel> response) {

                Log.e("sushiltest", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Toast.makeText(context,response.body().message, Toast.LENGTH_SHORT).show();

                    //      amount.setText(response.body().group.amount);
                    //   groupname.setText(Html.fromHtml(response.body().group().get(0).name()));

                }
            }

            @Override
            public void onFailure(Call<BillModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

   private void showruledialog() {

        Dialog contacts_dialog = new Dialog(SchemebillingActivity.this, R.style.theme_sms_receive_dialog);
        contacts_dialog.setContentView(R.layout.privewbill);
        contacts_dialog.setCancelable(true);
        contacts_dialog.setCanceledOnTouchOutside(true);

        contacts_dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        contacts_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Button continuebutton = contacts_dialog.findViewById(R.id.continuedbtn);
        final TextView customerdtname = contacts_dialog.findViewById(R.id.customerdtname);
        final TextView customercode = contacts_dialog.findViewById(R.id.customercodepreview);
        final TextView previousbalance = contacts_dialog.findViewById(R.id.previousbalance);
        final TextView closingbalance = contacts_dialog.findViewById(R.id.closingbalance);
      //  final TextView insallmentno = contacts_dialog.findViewById(R.id.insallmentno);

       customerdtname.setText("Customer Name :"+ " "+edtname.getText().toString());
       customercode.setText("Customer Code :"+ " "+customers_id);
       previousbalance.setText("Customer Mobile No :"+ " "+edtmobileno.getText().toString());
       closingbalance.setText("Total Amount :"+ " "+edttotal.getText().toString());
     //  insallmentno.setText("Installment No :"+ " "+edtmonth.getText().toString());


        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contacts_dialog.dismiss();
            }
        });

        contacts_dialog.show();


    }
   private void showrdialogpassword() {

        Dialog contacts_dialog = new Dialog(SchemebillingActivity.this, R.style.theme_sms_receive_dialog);
        contacts_dialog.setContentView(R.layout.password);
        contacts_dialog.setCancelable(true);
        contacts_dialog.setCanceledOnTouchOutside(true);



        contacts_dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        contacts_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final Button continuebutton = contacts_dialog.findViewById(R.id.continuedbtn);
        final EditText edtpass = contacts_dialog.findViewById(R.id.edtpass);






        continuebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (edtpass.getText().toString().isEmpty())
                {
                    Toast.makeText(context,"Please Enter password",Toast.LENGTH_SHORT).show();
                }
                else if (!edtpass.getText().toString().equalsIgnoreCase(sessionManager.getPasswordID()))
                {
                    Toast.makeText(context,"Please Enter Correct password",Toast.LENGTH_SHORT).show();
                }
                else {
                    new DatePickerDialog(SchemebillingActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    contacts_dialog.dismiss();
                    hideKeyboard(SchemebillingActivity.this
                    );
                }


            }
        });

        contacts_dialog.show();


    }

    public static void hideKeyboard(Activity act) {
        if (act != null && act.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) act.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), 0);
        }
    }

    boolean checkForm() {
        Amount = edtamount.getText().toString().trim();
      //  Customercode = edtcustomercode.getText().toString().trim();
        phone = edtmobileno.getText().toString().trim();
        name = edtname.getText().toString().trim();
        plase = edtpalse.getText().toString().trim();
     //   Month = edtmonth.getText().toString().trim();
      //  Prvoius = edtprvoius.getText().toString().trim();
      //  Billno = edtbillno.getText().toString().trim();
        Total = edttotal.getText().toString().trim();

        if (edtname.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Customer name", Toast.LENGTH_SHORT).show();
            edtname.setError("Customer name is required");
            return false;
        }


        if (edtamount.getText().toString().isEmpty()) {
           Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
            edtamount.setError("Amount is required");
            return false;
        }


        if (edtmobileno.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Customer mobileno", Toast.LENGTH_SHORT).show();
            edtmobileno.setError("Customer mobile is required");
           return false;
        }

        if(!Constant.isIndianNumberValid(phone))
        {
          //  Toast.makeText(this, getString(R.string.error_mobile_invalid_india), Toast.LENGTH_SHORT).show();
            edtmobileno.setError(getString(R.string.error_mobile_invalid_india));
            //  cancel = true;
            return false;
        }


        if (edtpalse.getText().toString().isEmpty()) {
            Toast.makeText(this, "Enter Customer place", Toast.LENGTH_SHORT).show();
            edtpalse.setError("Customer place is required");
            return false;
        }



        return true;
    }


}