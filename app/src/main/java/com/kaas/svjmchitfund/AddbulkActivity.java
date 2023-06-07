package com.kaas.svjmchitfund;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.kaas.svjmchitfund.Constant.commonDocumentDirPath;
import static com.kaas.svjmchitfund.Constant.getFilePathFromURI;
import static com.kaas.svjmchitfund.Constant.resumeformats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaas.svjmchitfund.Api.RetrofitClient;
import com.kaas.svjmchitfund.Module.BannerModel;
import com.kaas.svjmchitfund.Module.MSGStatus;
import com.kaas.svjmchitfund.Module.SessionModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddbulkActivity extends AppCompatActivity {
    private final String TAG = AddbulkActivity.class.getSimpleName();
    CardView cardview;
    Button Continue;
    TextView filetext;
    File newone;
    private static final int READ_REQUEST_CODE = 42;
    // One Preview Image

    public static final int SELECT_IMAGE = 22;
    private static final int PERMISSION_REQUEST_CODE = 200;
    // constant to compare
    // the activity result code
    int SELECT_PICTURE = 200;
    String list;
    private File newPath;
    String filePath = "";
    Uri fileURI = null;
    ArrayList<Uri> fileURIMultiple = new ArrayList<>();
    boolean gallery = false;
    SessionManager sessionManager;
    SessionModel sessionModel;
    Context context;
    ProgressDialog mProgressDialog;
    LinearLayout ll_1;
    boolean docment = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbulk);
        cardview =findViewById(R.id.cardview);
        filetext =findViewById(R.id.filetext);
        ll_1 =findViewById(R.id.ll_1);
        Continue =findViewById(R.id.Continue);

        sessionManager = new SessionManager(AddbulkActivity.this);
        sessionModel = new Gson().fromJson(sessionManager.getLoginSession(), SessionModel.class);
        // the image chooser function
        cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDocument();
            }
        });
        ll_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    void imageChooser() {

        if (checkPermission()) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            //List<Intent> targets = getTargets(intent);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, resumeformats);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
            //intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[targets.size()]));
            startActivityForResult(intent, READ_REQUEST_CODE);

      /*      ll_attachment.setVisibility(View.GONE);
            btn_submit.setVisibility(View.VISIBLE);*/
        } else {
            docment = true;
            requestpermission();
        }


    }
    /****************** Runtime Permission Functionality  *********************/
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE) + ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestpermission() {

        requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storagePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;


                    if (storagePermission) {

                        if (gallery) {
                            gallery = false;
                            Intent intent = new Intent();
                            //List<Intent> targets = getTargets(intent);
                            intent.setType("image/*");
                            //intent.setType("image/* video/*");
                            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            //intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targets.toArray(new Parcelable[targets.size()]));
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                         /*   ll_attachment.setVisibility(View.GONE);
                            btn_submit.setVisibility(View.VISIBLE);*/
                        }

                        if (docment) {
                            docment = false;

                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                            // Filter to only show results that can be "opened", such as a
                            // file (as opposed to a list of contacts or timezones)
                            intent.addCategory(Intent.CATEGORY_OPENABLE);

                            // Filter to show only images, using the image MIME data type.
                            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                            // To search for all documents available via installed storage providers,
                            // it would be "*/*".

                            intent.setType("*/*");
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, resumeformats);
                            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            startActivityForResult(intent, READ_REQUEST_CODE);

                        }
                    } else {

                        Toast.makeText(this, "Permission Denied, You cannot access App Properly.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(RECORD_AUDIO)) {
                                showMessageOKCancel("You need to allow access the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }




    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @SuppressLint("Range")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

 if (requestCode == READ_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {

                        fileURIMultiple = new ArrayList<>();

                        try {

                            Uri mImageUri = data.getData();
                            String uriString = mImageUri.toString();
                            Log.e("path", uriString);
                            File myFile = new File(uriString);
                            // String path = myFile.getAbsolutePath();
                            String path = getFilePathFromURI(this, mImageUri);
                            File file = new File(path);
                            int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                            long length = file.length();
                            Log.e("file_size", length + "");

                            if (file_size > 2048) {
                                Toast.makeText(this, "File Should be below 2MB",Toast.LENGTH_SHORT).show();
                            } else {
                                Cursor cursor = null;
                                try {
                                    String displayName;
                                    cursor = getContentResolver().query(mImageUri, null, null, null, null);
                                    if (cursor != null && cursor.moveToFirst()) {
                                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                        // doc_name.setText(displayName);
                                        addDocument(mImageUri, displayName);
                                    }
                                } finally {
                                    cursor.close();
                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Constant.log("NANANA",e.toString());
                        }



                        //  }
                    } catch (Exception e) {
                        e.printStackTrace();
                        filePath = "";
                        fileURI = null;
                        // rl_attach_pdf.setVisibility(View.GONE);
                        fileURIMultiple.clear();
                        Log.e("catch_err", e.toString());
                        Constant.log("ERROR", e.getMessage());
                        //Constant.showAlert("Unable to attach File.", this);

                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void addDocument(Uri uri, String ext) {
        try {



            InputStream imageStream = getContentResolver().openInputStream(uri);
            byte[] buffer = new byte[imageStream.available()];
            imageStream.read(buffer);

            File file = new File(String.valueOf(commonDocumentDirPath("Svjm/temp")));
            File newPath = new File(file.getAbsolutePath());
            if (!newPath.exists()) {
                newPath.mkdirs();
            }
             newone = new File(newPath, ext);
            OutputStream outStream = new FileOutputStream(newone);
            outStream.write(buffer);
            filetext.setVisibility(View.VISIBLE);
            filetext.setText(newone.getName());
 /*           rvListUpload.setVisibility(View.VISIBLE);
            uploadAdapter.addItem(new Image("pdfurl", newone.getPath(), false, ext));
            rvListUpload.scrollToPosition(0);
            imagename = "";

*/



        } catch (Exception e) {
            Constant.log("data", e.toString());
        }
    }
    private void uploadDocument() {

        try {

            mProgressDialog = Constant.showProgressBar("Please Wait..", this, mProgressDialog);

            ArrayList<MultipartBody.Part> files = new ArrayList<>();
            InputStream in = null;



            File file = new File(newone.getPath());
            Constant.log("top_path:::::",newone.getPath());
            RequestBody videoBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part vFile = MultipartBody.Part.createFormData("customer_file", file.getName(), videoBody);
            files.add(vFile);
            Call<MSGStatus> call = RetrofitClient.getInstance().getApi().uploadDocuments("Bearer " + sessionModel.token,files);



            call.enqueue(new Callback<MSGStatus>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<MSGStatus> call, final Response<MSGStatus> response) {
                    int statusCode = response.code();


                    Constant.hideProgressBar(mProgressDialog);
                    if (statusCode == 200) {

                    try {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {

                            Toast.makeText(AddbulkActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();                            finish();



                        } else {
                            Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    }

                }

                @Override
                public void onFailure(Call<MSGStatus> call, Throwable t) {
                    Log.e("API Error", t.toString());
                    // Log error here since request failed
                    // Toast.makeText(getActivity(), "Failure " + t.toString(), Toast.LENGTH_LONG).show();
                    Constant.hideProgressBar(mProgressDialog);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Constant.hideProgressBar(mProgressDialog);
        }

    }


}