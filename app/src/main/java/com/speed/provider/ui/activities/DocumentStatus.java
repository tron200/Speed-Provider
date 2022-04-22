package com.speed.provider.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
    RecyclerView recDocuments;
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
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_document_status);
        backArrow = findViewById(R.id.backArrow);
        recDocuments = findViewById(R.id.recDocuments);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DocumentStatus.this, MainActivity.class));
            }
        });
        getDocList();
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
        Dialog dialog = new Dialog(DocumentStatus.this);
        dialog.setContentView(R.layout.image_show_layout);
        ImageView ivShow = dialog.findViewById(R.id.ivShow);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        Button btOk = dialog.findViewById(R.id.btOk);
        dialog.show();
        btCancel.setOnClickListener(v -> {
            dialog.dismiss();
            browseanother();
        });

        new BitmapWorkerTask(DocumentStatus.this, ivShow,
                "add_revenue").execute(uri);
        btOk.setOnClickListener(v -> {

            saveProfileAccount(documnetName,
                    AppHelper.getFileDataFromDrawable(ivShow.getDrawable()),
                    documentId);
            dialog.dismiss();

        });

    }

    void showImage1(Bitmap bitmap) {
        Log.e("dialogcallk", "dialogcall");
        final Dialog dialog = new Dialog(DocumentStatus.this);
        dialog.setContentView(R.layout.image_show_layout);
        ImageView ivShow = dialog.findViewById(R.id.ivShow);
        Button btCancel = dialog.findViewById(R.id.btCancel);
        Button btOk = dialog.findViewById(R.id.btOk);
        ivShow.setImageBitmap(bitmap);

        dialog.show();


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
            dialog.dismiss();
            saveProfileAccount(documnetName,
                    AppHelper.getFileDataFromDrawable(ivShow.getDrawable()),
                    documentId);

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
                            if (ContextCompat.checkSelfPermission(DocumentStatus.this,
                                    Manifest.permission.CAMERA) ==
                                    PackageManager.PERMISSION_GRANTED) {
                                takePhoto();
                            } else {
                                Toast.makeText(DocumentStatus.this, "You have denied camera access permission.",
                                        Toast.LENGTH_LONG).show();
                            }
                            break;
                        default:
                            break;
                    }
                });


        return builder.create();
    }

    public void saveProfileAccount(String filename, byte[] bytes, String docid) {
        if (Utils.isConnectingToInternet(DocumentStatus.this)) {
            ProgressDialog pDialog = new ProgressDialog(DocumentStatus.this);
            // pDialog.setTitle("Loading...");
            pDialog.setCancelable(false);
            pDialog.setMessage("Loading...");
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
                                Toast.makeText(DocumentStatus.this, "File is Uploaded Successfully",
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
            Toast.makeText(DocumentStatus.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
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
