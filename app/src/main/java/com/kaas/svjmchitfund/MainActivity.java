package com.kaas.svjmchitfund;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_BLUETOOTH = 1;
    // will show the statuses like bluetooth open, close or data sent
    TextView myLabel;

    // will enable user to enter any text to be printed
    EditText myTextbox;

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // we are going to have three buttons for specific functions
        Button openButton = (Button) findViewById(R.id.open);
        Button sendButton = (Button) findViewById(R.id.send);
        Button closeButton = (Button) findViewById(R.id.close);

// text label and input box
        myLabel = (TextView) findViewById(R.id.label);
        myTextbox = (EditText) findViewById(R.id.entry);

        openButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    findBT();
                    openBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        try {
            // more codes will be here
        } catch (Exception e) {
            e.printStackTrace();
        }
// send data typed by the user to be printed
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    sendData();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // close bluetooth connection
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    closeBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                myLabel.setText("No bluetooth adapter available");
            }

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice device : pairedDevices) {

                        // RPP300 is the name of the bluetooth printer device
                        // we got this name from the list of paired devices
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        if (device.getName().equals("RPP300")) {
                            mmDevice = device;
                            break;
                        }
                    }
                }

                myLabel.setText("Bluetooth device found.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void openBT() throws IOException {
            try {

                // Standard SerialPortService ID
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);

                mmSocket.connect();
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();

                beginListenForData();

                myLabel.setText("Bluetooth Opened");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void beginListenForData() {
            try {
                final Handler handler = new Handler();

                // this is the ASCII code for a newline character
                final byte delimiter = 10;

                stopWorker = false;
                readBufferPosition = 0;
                readBuffer = new byte[1024];

                workerThread = new Thread(new Runnable() {
                    public void run() {

                        while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                            try {

                                int bytesAvailable = mmInputStream.available();

                                if (bytesAvailable > 0) {

                                    byte[] packetBytes = new byte[bytesAvailable];
                                    mmInputStream.read(packetBytes);

                                    for (int i = 0; i < bytesAvailable; i++) {

                                        byte b = packetBytes[i];
                                        if (b == delimiter) {

                                            byte[] encodedBytes = new byte[readBufferPosition];
                                            System.arraycopy(
                                                    readBuffer, 0,
                                                    encodedBytes, 0,
                                                    encodedBytes.length
                                            );

                                            // specify US-ASCII encoding
                                            final String data = new String(encodedBytes, "US-ASCII");
                                            readBufferPosition = 0;

                                            // tell the user data were sent to bluetooth printer device
                                            handler.post(new Runnable() {
                                                public void run() {
                                                    myLabel.setText(data);
                                                }
                                            });

                                        } else {
                                            readBuffer[readBufferPosition++] = b;
                                        }
                                    }
                                }

                            } catch (IOException ex) {
                                stopWorker = true;
                            }

                        }
                    }
                });

                workerThread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void sendData() throws IOException {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, MainActivity.PERMISSION_BLUETOOTH);
            } else {
                // Your code HERE
            }
        }
        // close the connection to bluetooth printer.
        void closeBT() throws IOException {
            try {
                stopWorker = true;
                mmOutputStream.close();
                mmInputStream.close();

                mmSocket.close();
                myLabel.setText("Bluetooth Closed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }