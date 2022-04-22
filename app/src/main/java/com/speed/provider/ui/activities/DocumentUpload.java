package com.speed.provider.ui.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.speed.provider.BuildConfig;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.R;
import com.speed.provider.ui.fragments.DriverMapFragment;
import com.speed.provider.helper.BitmapCompletion;
import com.speed.provider.helper.BitmapWorkerTask;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.speed.provider.helper.VolleyMultipartRequest;
import com.speed.provider.utills.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;


public class DocumentUpload extends AppCompatActivity implements View.OnClickListener, BitmapCompletion {
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int TAKE_PICTURE = 0;
    private static final int GET_PICTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static String dateFormat = "yyyyMMdd_HHmmss";
    private static String[] PERMISSIONS_STORAGE = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA

    };
    ImageView backArrow;
    Button uploadLicense, uploadsever;
    ProgressDialog pDialog;
    String fileDriving, fileRegistration;
    ImageView img;
    String tag = "";
    ListView lv;
    String docId;
    TextView txtUploadTitle;
    ArrayList<String> document_id, document_name, documnet_type;
    ArrayAdapter arrayAdapter;
    private Bitmap bmp = null;
    private Uri cameraImageUri = null;
    private String first = "", boring_depth = "";
    private String fileExt = "";
    private byte[] b, b1, b2;
    private File file;
    private Uri galleryImageUri = null;

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_document_upload);

        verifyStoragePermissions(DocumentUpload.this);
        backArrow = findViewById(R.id.backArrow);
        uploadLicense = findViewById(R.id.uploadLicense);
        uploadsever = findViewById(R.id.uploadsever);
        img = findViewById(R.id.imgLicense);
        lv = findViewById(R.id.lv);
        uploadLicense.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        uploadsever.setOnClickListener(this);
        document_id = new ArrayList<>();
        document_name = new ArrayList<>();
        documnet_type = new ArrayList<>();
        txtUploadTitle = findViewById(R.id.txtUploadTitle);
        if (getIntent().getStringExtra("account_status") != null) {
            if (getIntent().getStringExtra("account_status").contains("account_status_new")) {
                LinearLayout label = findViewById(R.id.label);
                label.setVisibility(View.GONE);
                TextView txtWaiting = findViewById(R.id.txtWaiting);
                txtWaiting.setVisibility(View.VISIBLE);
                txtWaiting.setOnClickListener(view -> startActivity(new Intent(DocumentUpload.this, DriverMapFragment.class)));
            }
        }
        getDocumnetstype();
        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lv.setTextFilterEnabled(true);
        lv.setOnItemClickListener((adapterView, view, i, l) -> {
            CheckedTextView item = (CheckedTextView) view;

            hideDetails();
            docId = document_id.get(i);
            txtUploadTitle.setText(document_name.get(i) + "");

        });
    }

    private void hideDetails() {
        lv.setVisibility(View.GONE);

        LinearLayout bottomLayout = findViewById(R.id.bottomLayout);
        bottomLayout.setVisibility(View.VISIBLE);

        TextView topTitle = findViewById(R.id.topTitle);
        topTitle.setVisibility(View.GONE);
    }

    private void showDetails() {
        lv.setVisibility(View.VISIBLE);

        LinearLayout bottomLayout = findViewById(R.id.bottomLayout);
        bottomLayout.setVisibility(View.GONE);

        TextView topTitle = findViewById(R.id.topTitle);
        topTitle.setVisibility(View.GONE);
    }

    private void getDocumnetstype() {
        final ProgressDialog progressDialog = new ProgressDialog(DocumentUpload.this);
        progressDialog.setTitle("Getting Documents type....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        URLHelper.BASE + "api/provider/document/types",
                        null,
                        response -> {
                            Log.e("response", response + "document");
                            progressDialog.dismiss();

                            try {
                                JSONArray jsonArray = response.getJSONArray("document");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String name = jsonArray.getJSONObject(i).getString("name");
                                    document_name.add(name);
                                    document_id.add(jsonArray.getJSONObject(i).getString("id"));
                                    documnet_type.add(jsonArray.getJSONObject(i).getString("type"));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            String[] arr = document_name.toArray(new String[document_name.size()]);

                            arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_checked, arr);
                            lv.setAdapter(arrayAdapter);

                        }, error -> {
                    progressDialog.dismiss();
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                        Log.e("", "Access_Token" + SharedHelper.getKey(getApplicationContext(), "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backArrow:
                startActivity(new Intent(DocumentUpload.this, DriverMapFragment.class));
                finish();
                break;
            case R.id.uploadsever:
                saveProfileAccount();
                uploadsever.setVisibility(View.GONE);
                uploadLicense.setVisibility(View.VISIBLE);
                break;

            case R.id.uploadLicense:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                5);
                    }
                }
                tag = "uploadLicense";
                Dialog d = ImageChoose();
                d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                d.show();
                break;

        }
    }

    private Dialog ImageChoose() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(DocumentUpload.this);

        if (bmp == null) {


            CharSequence[] ch = {};
            ch = new CharSequence[]{"Gallery", "Camera"};


            builder.setTitle("Choose Image :").setItems(
                    ch,
                    (dialog, which) -> {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                getPhoto();
                                break;

                            case 1:

                                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) ==
                                        PackageManager.PERMISSION_GRANTED) {

                                    takePhoto();

                                } else {
                                    Toast.makeText(getApplicationContext(), "You have denied camera access permission.", Toast.LENGTH_LONG).show();
                                }


                                break;

                            default:
                                break;
                        }
                    });
        } else {
            builder.setTitle("Choose Image :").setItems(
                    new CharSequence[]{"Gallery", "Camera"},
                    (dialog, which) -> {
                        // TODO Auto-generated method stub
                        switch (which) {
                            case 0:
                                getPhoto();
                                break;

                            case 1:
                                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) ==
                                        PackageManager.PERMISSION_GRANTED) {

                                    takePhoto();

                                } else {
                                    Toast.makeText(getApplicationContext(), "You have denied camera access permission.", Toast.LENGTH_LONG).show();
                                }
                                break;


                            default:
                                break;
                        }
                    });
        }


        return builder.create();
    }

    private void getPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GET_PICTURE);
    }

    private void takePhoto() {


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraImageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    private Uri getOutputMediaFileUri(int type) {
        Uri uri = FileProvider.getUriForFile(DocumentUpload.this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type));
        return uri;
    }

    @SuppressLint("SimpleDateFormat")
    private File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "APP");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {

                return null;
            }
        }
        String timeStamp = new SimpleDateFormat(dateFormat)
                .format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }

        return mediaFile;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case TAKE_PICTURE:


                if (resultCode == Activity.RESULT_OK) {
                    //  cameraImageUri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
                    if (cameraImageUri != null) {
                        getContentResolver().notifyChange(cameraImageUri, null);

                        fileExt = ".png";
                        file = new File(cameraImageUri.getPath());
                        first = file.getName() + fileExt;
                        galleryImageUri = null;
                        try {
                            if (tag.equalsIgnoreCase("uploadLicense")) {
                                fileDriving = first;
                                new BitmapWorkerTask(DocumentUpload.this, img,
                                        "add_revenue").execute(cameraImageUri);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case GET_PICTURE:

                if (resultCode == Activity.RESULT_OK) {
                    {
                        cameraImageUri = null;
                        galleryImageUri = data.getData();
                        getContentResolver().notifyChange(galleryImageUri, null);
                        fileExt = Utils.getRealPathFromURI(DocumentUpload.this, galleryImageUri);
                        try {


                            if (fileExt != null) {
                                file = new File(fileExt);
                                first = file.getName();
                                fileExt = fileExt.substring(fileExt.lastIndexOf('.') + 1
                                );
                            } else {

                                first = System.currentTimeMillis() + ".png";
                                fileExt = ".png";
                            }


//                            bmp = MediaStore.Images.Media.getBitmap(
//                                    getContentResolver(), galleryImageUri);
                            if (tag.equalsIgnoreCase("uploadLicense")) {
                                fileDriving = first;
                                new BitmapWorkerTask(DocumentUpload.this, img,
                                        "add_revenue").execute(galleryImageUri);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                break;

        }

    }

    @Override
    public void onBitmapScaleComplete(Bitmap bmp) {
        int dim = Utils.getSquareCropDimensionForBitmap(bmp);
        this.bmp = ThumbnailUtils.extractThumbnail(bmp, dim, dim);
        if (bmp != null) {
            b = Utils.convertBitmapToByte(bmp);
            try {
                if (tag.equalsIgnoreCase("uploadLicense")) {
                    b1 = b;
                    img.setImageBitmap(bmp);
                    uploadLicense.setVisibility(View.GONE);
                    uploadsever.setVisibility(View.VISIBLE);
                }

//                img.setImageBitmap(bmp);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    public void saveProfileAccount() {


        if (Utils.isConnectingToInternet(DocumentUpload.this)) {
//            final ProgressDialog pd_loading = new ProgressDialog(DocumentUpload.this);
//            pd_loading.setCancelable(false);

            pDialog = new ProgressDialog(DocumentUpload.this);
            // pDialog.setTitle("Loading...");
            pDialog.setCancelable(false);


            pDialog.setMessage("Loading...");

            pDialog.show();

            final ProgressDialog finalPDialog = pDialog;
            VolleyMultipartRequest multipartRequest = new
                    VolleyMultipartRequest(Request.Method.POST,
                            URLHelper.BASE + "api/provider/document/upload",
                            response -> {
                                pDialog.dismiss();
                                String resultResponse = new String(response.data);
                                Log.e("uploadtest", resultResponse + "");
                                Toast.makeText(getApplicationContext(), "File is Uploaded Successfully", Toast.LENGTH_LONG).show();
                                SharedHelper.putKey(DocumentUpload.this, "Document Uploaded", "true");
                                try {
                                    JSONObject jsonObject = new JSONObject(resultResponse);
                                    showDetails();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    // pd_loading.dismiss();
                                    pDialog.dismiss();
                                }
                            }, error -> {
                        displayMessage(getString(R.string.something_went_wrong));
                        pDialog.dismiss();

                        error.printStackTrace();
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> param = new HashMap<>();
                            param.put("document_id", docId);
                            param.put("provider_id", SharedHelper.getKey(getApplicationContext(), "id"));
                            return param;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("X-Requested-With", "XMLHttpRequest");
                            headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                            return headers;
                        }


                        @Override
                        protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            // file name could found file BASE or direct access from real path
                            // for now just get bitmap data from ImageView
                            params.put("document", new DataPart(first, b, "image/jpeg"));
                            return params;
                        }
                    };

            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue = Volley.newRequestQueue(DocumentUpload.this);
            queue.add(multipartRequest);
        } else {

            Toast.makeText(DocumentUpload.this, "No Internet Connection", Toast.LENGTH_SHORT).show();


        }

    }

}
