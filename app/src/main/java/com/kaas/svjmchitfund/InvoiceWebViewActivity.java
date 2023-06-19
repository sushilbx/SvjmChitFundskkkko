package com.kaas.svjmchitfund;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InvoiceWebViewActivity extends AppCompatActivity {

    ProgressDialog pd;
    String url, value;
    //object of print job
    PrintJob printJob;
    //a boolean to check the status of printing
    boolean printBtnPressed = false;
    private WebView Wv;
    private Bitmap bmp;
    private String orderid;
    private ImageView backIv, downloadIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_web_view);
        Intent i = getIntent();
        value = getIntent().getStringExtra("value");
        Log.e("value", ""+value);
        url = "https://docs.google.com/gview?embedded=true&url=svjm-web.seomantras.in/api/billing/user/report?customer_id=" + value;
        orderid = getIntent().getStringExtra("id");
        backIv = (ImageView) findViewById(R.id.backIv);

        Log.d("orderid>>>", "" + orderid);
        Log.d("orderid>>>url", "" + url);


        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);

        Wv = (WebView) findViewById(R.id.webViewAndroid);
        downloadIv = findViewById(R.id.downloadIv);


        WebSettings Ws = Wv.getSettings();
        Ws.setJavaScriptEnabled(true);
        Wv.getSettings().setBuiltInZoomControls(true);
        Wv.getSettings().setDisplayZoomControls(false);
        Ws.setUseWideViewPort(true);
        Wv.setInitialScale(1);

        //  Wv.setWebViewClient(new MainWebViewClient());
        //  Wv.loadUrl(url);

        Wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(Wv, url);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        Wv.loadUrl(url);


        downloadIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Wv != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        PrintTheWebPage(Wv);
                    } else {
                        Toast.makeText(getApplicationContext(), "Not available for device below Android LOLLIPOP", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.hide();
            }
        }, 3000);*/


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void PrintTheWebPage(WebView webView) {


        printBtnPressed = true;

        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        String jobName = getString(R.string.app_name) + " webpage" + webView.getUrl();

        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter(jobName);

        assert printManager != null;
        printJob = printManager.print(jobName, printAdapter,
                new PrintAttributes.Builder().build());

    }

    private void converttopdf(Bitmap bmp) {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels;
        float width = displaymetrics.widthPixels;

        int convertHighet = (int) hight, convertWidth = (int) width;

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bmp.getWidth(), bmp.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();


        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);


        Bitmap bitmap = Bitmap.createScaledBitmap(bmp, bmp.getWidth(), bmp.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);


        // write the document content
        String targetPdf = "/sdcard/" + orderid + ".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            //   btn_convert.setText("Check PDF");
            //   boolean_save=true;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);


        if (filePath.exists()) {
          //  pd.dismiss();
            intentShareFile.setType("application/pdf");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filePath));
            Toast.makeText(this, "Successfully Download at :" + filePath, Toast.LENGTH_LONG).show();

            //startActivity(Intent.createChooser(intentShareFile, "Share From"));
        }
    }


    //create a function to create the print job
    private void createWebPrintJob(WebView webView) {

        //create object of print manager in your device
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);

        //create object of print adapter
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();

        //provide name to your newly generated pdf file
        String jobName = getString(R.string.app_name) + " Print Test";

        //open print dialog
        printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
    }

    private Bitmap pictureDrawable2Bitmap(PictureDrawable pictureDrawable) throws FileNotFoundException {
        Bitmap bitmap = Bitmap.createBitmap(
                pictureDrawable.getIntrinsicWidth(),
                pictureDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawPicture(pictureDrawable.getPicture());
        return bitmap;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:

                    if ((keyCode == KeyEvent.KEYCODE_BACK) && Wv.canGoBack()) {
                        Wv.goBack();
                        return true;
                    }
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // pd.hide();
        if (printJob != null && printBtnPressed) {
            if (printJob.isCompleted()) {
                //Showing Toast Message
                Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();
            } else if (printJob.isStarted()) {
                //Showing Toast Message
                Toast.makeText(this, "isStarted", Toast.LENGTH_SHORT).show();

            } else if (printJob.isBlocked()) {
                //Showing Toast Message
                Toast.makeText(this, "isBlocked", Toast.LENGTH_SHORT).show();

            } else if (printJob.isCancelled()) {
                //Showing Toast Message
                Toast.makeText(this, "isCancelled", Toast.LENGTH_SHORT).show();

            } else if (printJob.isFailed()) {
                //Showing Toast Message
                Toast.makeText(this, "isFailed", Toast.LENGTH_SHORT).show();

            } else if (printJob.isQueued()) {
                //Showing Toast Message
                Toast.makeText(this, "isQueued", Toast.LENGTH_SHORT).show();

            }
            //set printBtnPressed false
            printBtnPressed = false;
        }
    }

    private class MainWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            view.setVisibility(View.GONE);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            Picture picture = view.capturePicture();
            bmp = Bitmap.createBitmap(picture.getWidth(),
                    picture.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            picture.draw(c);
            view.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
        }
    }
}