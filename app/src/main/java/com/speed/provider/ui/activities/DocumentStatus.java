package com.speed.provider.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.speed.provider.BuildConfig;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.R;
import com.speed.provider.helper.AppHelper;
import com.speed.provider.helper.BitmapWorkerTask;
import com.speed.provider.helper.CustomDialog;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.speed.provider.helper.VolleyMultipartRequest;
import com.speed.provider.utills.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class DocumentStatus extends AppCompatActivity {
    private static final int GET_PICTURE = 1;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int TAKE_PICTURE = 0;
    public static String TAG = "DocumentStatus";
    public static int deviceHeight;
    public static int deviceWidth;
    private ImageView backArrow;
    ArrayList<String> documnet_type;
    RecyclerView recDocuments;
    JSONArray array;
    int UploadPosition = 0;
    String documnetName = "", documentId = "";
    Uri documentUri;
    private Uri cameraImageUri = null;
    private String fileExt = "";
    private File file;
    private String first = "";

    private static Bitmap getBitmapFromUri(@NonNull Context context, @NonNull Uri uri) throws IOException {

        Log.e(TAG, "getBitmapFromUri: Resize uri" + uri);
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        assert parcelFileDescriptor != null;
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        Log.e(TAG, "getBitmapFromUri: Height" + deviceHeight);
        Log.e(TAG, "getBitmapFromUri: width" + deviceWidth);
        int maxSize = Math.min(deviceHeight, deviceWidth);
        if (image != null) {
            Log.e(TAG, "getBitmapFromUri: Width" + image.getWidth());
            Log.e(TAG, "getBitmapFromUri: Height" + image.getHeight());
            int inWidth = image.getWidth();
            int inHeight = image.getHeight();
            int outWidth;
            int outHeight;
            if (inWidth > inHeight) {
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }
            return Bitmap.createScaledBitmap(image, outWidth, outHeight, false);
        } else {
            Toast.makeText(context, context.getString(R.string.valid_image), Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_status);
        backArrow = findViewById(R.id.backArrow);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            backArrow.setImageDrawable(getDrawable(R.drawable.ic_forward));
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        documnet_type = new ArrayList<>();

        recDocuments = findViewById(R.id.recDocuments);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DocumentStatus.this, MainActivity.class));
            }
        });
        getDocList();
        getDocumnetstype();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;
        deviceWidth = displayMetrics.widthPixels;

    }

    private void getDocList() {

        CustomDialog customDialog = new CustomDialog(DocumentStatus.this);
        customDialog.setCancelable(false);
        customDialog.show();

        JsonArrayRequest jsonArrayRequest = new
                JsonArrayRequest(URLHelper.BASE + "api/provider/document/status",
                        response -> {

                            if (response != null) {
                                Log.v("response", response + "doc");
                                array = response;
                                PostAdapter postAdapter = new PostAdapter(response);
                                recDocuments.setHasFixedSize(true);
                                recDocuments.setLayoutManager(new LinearLayoutManager(DocumentStatus.this) {
                                    @Override
                                    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                                        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT);
                                    }
                                });
                                if (postAdapter != null && postAdapter.getItemCount() > 0) {

                                    recDocuments.setAdapter(postAdapter);
                                } else {

                                }

                            } else {

                            }

                            customDialog.dismiss();

                        }, error -> {
                    Log.v("DocumentsStatus Error", error.getMessage() + "");
                    customDialog.dismiss();
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(DocumentStatus.this, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonArrayRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getDocumnetstype() {
        final ProgressDialog progressDialog = new ProgressDialog(DocumentStatus.this);
        progressDialog.setTitle(getString(R.string.getting_document));
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

                                    documnet_type.add(jsonArray.getJSONObject(i).getString("type"));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


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
        Uri uri = FileProvider.getUriForFile(DocumentStatus.this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile(type));
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
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
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
                        DocumentStatus.this.getContentResolver()
                                .notifyChange(cameraImageUri, null);

                        fileExt = ".png";
                        file = new File(cameraImageUri.getPath());
                        first = file.getName() + fileExt;
//                        galleryImageUri = null;

                        try {

                            showImage(cameraImageUri);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case GET_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    {
                        Uri uri = data.getData();
                        File f1 = new File(uri.getPath());

                        try {
                            Bitmap resizeImg = getBitmapFromUri(DocumentStatus.this, uri);

                            if (resizeImg != null) {
                                documentUri = uri;

                                Bitmap reRotateImg = AppHelper.modifyOrientation(resizeImg,
                                        AppHelper.getPath(DocumentStatus.this, uri));
                                showImage1(reRotateImg);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                break;

        }
    }

    private void showImage(Uri uri) {
        final Calendar myCalendar = Calendar.getInstance();

        Dialog dialog = new Dialog(DocumentStatus.this);
        dialog.setContentView(R.layout.image_show_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageView ivShow = dialog.findViewById(R.id.ivShow);
        TextView etDate = dialog.findViewById(R.id.etDate);
        TextView txtDocumentName = dialog.findViewById(R.id.txtDocumentName);
        TextView txtDocumentType = dialog.findViewById(R.id.txtDocumentType);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        Button btOk = dialog.findViewById(R.id.btOk);


            txtDocumentName.setText(documnetName);
            txtDocumentType.setText(documnet_type.get(UploadPosition));


        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            etDate.setText(sdf.format(myCalendar.getTime()));

       };

        dialog.show();
        etDate.setOnClickListener(v -> new DatePickerDialog(DocumentStatus.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        btCancel.setOnClickListener(v -> {
            dialog.dismiss();
            browseanother();
        });


        new BitmapWorkerTask(DocumentStatus.this, ivShow,
                "add_revenue").execute(uri);
        btOk.setOnClickListener(v -> {

            saveProfileAccount(documnetName,
                    AppHelper.getFileDataFromDrawable(ivShow.getDrawable()),
                    documentId,etDate.getText().toString());
            dialog.dismiss();

        });

    }

    void showImage1(Bitmap bitmap) {
        Log.e("dialogcallk", "dialogcall");
        final Calendar myCalendar = Calendar.getInstance();

        final Dialog dialog = new Dialog(DocumentStatus.this);
        dialog.setContentView(R.layout.image_show_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView ivShow = dialog.findViewById(R.id.ivShow);
        TextView etDate = dialog.findViewById(R.id.etDate);
        LinearLayout linearLayout = dialog.findViewById(R.id.linear);
        TextView txtDocumentName = dialog.findViewById(R.id.txtDocumentName);
        TextView txtDocumentType = dialog.findViewById(R.id.txtDocumentType);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        Button btOk = dialog.findViewById(R.id.btOk);
        ivShow.setImageBitmap(bitmap);

        if (SharedHelper.getKey(DocumentStatus.this, "selectedlanguage").contains("ar")) {
            linearLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            linearLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
            txtDocumentName.setText(documnetName);
//            txtDocumentType.setText(array.getJSONObject(UploadPosition).getString("type"));
            txtDocumentType.setText(documnet_type.get(UploadPosition));


        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            etDate.setText(sdf.format(myCalendar.getTime()));

        };
        dialog.show();

        etDate.setOnClickListener(v -> new DatePickerDialog(DocumentStatus.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        btOk.setOnClickListener(v -> {
            /*if (uploadTag.equalsIgnoreCase("LogBook")) {
                imgPersonal.setImageBitmap(bitmap);
                imgPersonal.setVisibility(View.VISIBLE);
                btPersonalPic.setVisibility(View.GONE);
                dialog.dismiss();
                                saveProfileAccount("LogBook",
                                        AppHelper.getFileDataFromDrawable(imgPersonal.getDrawable()),
                                        SharedHelper.getKey(DocUploadActivity.this, uploadTag));


            }*/
            if (etDate.getText().toString().isEmpty()) {
                etDate.setError(getString(R.string.select_expiry));
            }else {
                dialog.dismiss();
                saveProfileAccount(documnetName,
                        AppHelper.getFileDataFromDrawable(ivShow.getDrawable()),
                        documentId,etDate.getText().toString());
            }

        });
        btCancel.setOnClickListener(v -> {
            dialog.dismiss();
            browseanother();
        });
//        ivShow.setImageBitmap(uri);

    }

    private void browseanother() {
        if (ContextCompat.checkSelfPermission(DocumentStatus.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        5);
            }
        }
//                imgPersonal.setVisibility(View.VISIBLE);
        Dialog d = ImageChoose();
        d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        d.show();
    }

    private Dialog ImageChoose() {
        androidx.appcompat.app.AlertDialog.Builder builder = new
                androidx.appcompat.app.AlertDialog.Builder(DocumentStatus.this);


        CharSequence[] ch = {};
        ch = new CharSequence[]{getString(R.string.gallery), getString(R.string.camera)};
        builder.setTitle(getString(R.string.choose_image)).setItems(
                ch,
                (dialog, which) -> {
                    // TODO Auto-generated method stub
                    switch (which) {
                        case 0:
                            getPhoto();
                            break;
                        case 1:
                            if (ContextCompat.checkSelfPermission(DocumentStatus.this,
                                    Manifest.permission.CAMERA) ==
                                    PackageManager.PERMISSION_GRANTED) {
                                takePhoto();
                            } else {
                                Toast.makeText(DocumentStatus.this, getString(R.string.denied_camera_permission),
                                        Toast.LENGTH_LONG).show();
                            }
                            break;
                        default:
                            break;
                    }
                });


        return builder.create();
    }

    public void saveProfileAccount(String filename, byte[] bytes, String docid, String expdate) {
        if (Utils.isConnectingToInternet(DocumentStatus.this)) {
            ProgressDialog pDialog = new ProgressDialog(DocumentStatus.this);
            // pDialog.setTitle("Loading...");
            pDialog.setCancelable(false);
            pDialog.setMessage(getString(R.string.loading));
            pDialog.show();

            VolleyMultipartRequest multipartRequest = new
                    VolleyMultipartRequest(Request.Method.POST,
                            URLHelper.BASE + "api/provider/document/upload",
                            response -> {
                                pDialog.dismiss();
                                getDocList();

                               /* if (uploadTag == "PSV Insurance Certificate") {
                                    SharedHelperImage.putKey(DocUploadActivity.this,
                                            "PSVInsuranceCertificate", String.valueOf(documentUri));
                                }*/
                                String resultResponse = new String(response.data);
                                Log.e("uploadtest", resultResponse + "");
                                Toast.makeText(DocumentStatus.this, getString(R.string.uploaded_successfully),
                                        Toast.LENGTH_LONG).show();
                                pDialog.dismiss();

                            }, error -> {
                        displayMessage(getString(R.string.something_went_wrong));
                        pDialog.dismiss();
                        error.printStackTrace();
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> param = new HashMap<>();
                            param.put("document_id", docid);
                            param.put("expires_at", expdate);

                            param.put("provider_id", SharedHelper.getKey(DocumentStatus.this, "id"));


                            return param;
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("X-Requested-With", "XMLHttpRequest");
                            headers.put("Authorization", "Bearer " + SharedHelper.getKey(DocumentStatus.this, "access_token"));

                            return headers;
                        }


                        @Override
                        protected Map<String, DataPart> getByteData() {
                            Map<String, DataPart> params = new HashMap<>();
                            // file name could found file BASE or direct access from real path
                            // for now just get bitmap data from ImageView

                            params.put("document", new DataPart(filename, bytes, "image/jpeg"));

                            return params;
                        }
                    };


            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            RequestQueue queue = Volley.newRequestQueue(DocumentStatus.this);
            queue.add(multipartRequest);
        } else {
            Toast.makeText(DocumentStatus.this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

    }

    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {
        JSONArray jsonArray;

        public PostAdapter(JSONArray array) {
            this.jsonArray = array;
        }

        public void append(JSONArray array) {
            try {
                for (int i = 0; i < array.length(); i++) {
                    this.jsonArray.put(array.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public PostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.document_update_item, parent, false);
            return new PostAdapter.MyViewHolder(itemView);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onBindViewHolder(PostAdapter.MyViewHolder holder, int position) {
            try {

                holder.txtDocumentsName.setText(jsonArray.optJSONObject(position).optString("document_name"));
                holder.txtSatus.setText(jsonArray.optJSONObject(position).optString("status"));
                if (jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("REJECTED")) {
                    holder.txtSatus.setTextColor(getColor(R.color.red));
                } else if (jsonArray.optJSONObject(position).optString("status").equalsIgnoreCase("ACTIVE")) {
                    holder.txtSatus.setTextColor(getColor(R.color.green));
                } else {
                    holder.txtSatus.setTextColor(getColor(R.color.colorAccent));
                }
                Picasso.get().load(URLHelper.BASE + "storage/app/public/" + jsonArray.optJSONObject(position).optString("url"))
                        .placeholder(R.drawable.loading_gif)
                        .into(holder.imgDoc);

                holder.imgDoc.setOnClickListener(v -> {
                    Intent intent = new Intent(DocumentStatus.this, FullImage.class);
                    intent.putExtra("title", jsonArray.optJSONObject(position).optString("document_name"));
                    intent.putExtra("url", URLHelper.BASE + "storage/app/public/" + jsonArray.optJSONObject(position).optString("url"));
                    startActivity(intent);
                });

                holder.txtUpdate.setOnClickListener(v -> {
                    UploadPosition = position;
                    documnetName = jsonArray.optJSONObject(position).optString("document_name");
                    documentId = jsonArray.optJSONObject(position).optString("document_id");
                    if (ContextCompat.checkSelfPermission(DocumentStatus.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    5);
                        }
                    }

                    Dialog d = ImageChoose();
                    d.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                    d.show();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        @Override
        public int getItemCount() {
            return jsonArray.length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView txtUpdate, txtSatus, txtDocumentsName;
            CircleImageView imgDoc;

            public MyViewHolder(View itemView) {
                super(itemView);

                imgDoc = itemView.findViewById(R.id.imgDoc);
                txtUpdate = itemView.findViewById(R.id.txtUpdate);
                txtSatus = itemView.findViewById(R.id.txtSatus);
                txtDocumentsName = itemView.findViewById(R.id.txtDocumentsName);

                itemView.setOnClickListener(view -> {

                });

            }
        }
    }
}
