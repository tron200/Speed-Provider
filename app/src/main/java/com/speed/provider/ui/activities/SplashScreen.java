package com.speed.provider.ui.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.Login;
import com.speed.provider.R;
import com.speed.provider.helper.ConnectionHelper;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.speed.provider.ui.activities.login.IntroActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = SplashScreen.class.getSimpleName();
    ConnectionHelper helper;
    Boolean isInternet;
    Handler handleCheckStatus;
    String device_token;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            String packageName = context.getApplicationContext().getPackageName();
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);
            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        handleCheckStatus = new Handler();
        showPermissionDialog();

        helper = new ConnectionHelper(this);
        isInternet = helper.isConnectingToInternet();
        new Thread(() -> {
            while (progressStatus < 101) {
                progressStatus += 25;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(() -> progressBar.setProgress(progressStatus));
            }
        }).start();

        Log.e("printKeyHash", printKeyHash(SplashScreen.this) + "");

        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<InstallationTokenResult> task) {
                String newToken = task.getResult().getToken();
                Log.e("newToken", newToken);
                SharedHelper.putKey(getApplicationContext(), "device_token", "" + newToken);
                device_token = newToken;
            }
        });

    }

    @SuppressLint("HardwareIds")
    public void getProfile() {
        String device_UDID = "";

        try {
            device_UDID = Settings.Secure.getString(getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Log.i(TAG, "Device UDID:" + device_UDID);
        } catch (Exception e) {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
        String url = URLHelper.USER_PROFILE_API + "?device_type=android&device_id="
                + device_UDID + "&device_token=" + device_token;
        Log.v("profileur", url);
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        url,
                        object,
                        response -> {
                            Log.e("responseBody", response.toString());
                            SharedHelper.putKey(this, "id", response.optString("id"));
                            SharedHelper.putKey(this, "first_name", response.optString("first_name"));
                            SharedHelper.putKey(this, "email", response.optString("email"));
                            SharedHelper.putKey(this, "sos", response.optString("sos"));
                            SharedHelper.putKey(this, "currency", response.optString("currency"));
                            SharedHelper.putKey(this, "rating", response.optString("rating"));
                            if (response.optString("avatar").startsWith("http"))
                                SharedHelper.putKey(this, "picture", response.optString("avatar"));
                            else
                                SharedHelper.putKey(this, "picture", URLHelper.BASE + "storage/app/public/" + response.optString("avatar"));
                            SharedHelper.putKey(this, "gender", response.optString("gender"));
                            SharedHelper.putKey(this, "mobile", response.optString("mobile"));
                            SharedHelper.putKey(this, "approval_status", response.optString("status"));
                            SharedHelper.putKey(this, "loggedIn", getString(R.string.True));
                            if (response.optJSONObject("service") != null) {
                                try {
                                    JSONObject service = response.optJSONObject("service");
                                    if (service.optJSONObject("service_type") != null) {
                                        JSONObject serviceType = service.optJSONObject("service_type");
                                        SharedHelper.putKey(this, "service",
                                                serviceType.optString("name"));
                                        SharedHelper.putKey(this, "service_image",
                                                serviceType.optString("image"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            if (response.optString("status").equalsIgnoreCase("new")) {
                                Intent intent = new Intent(this, WaitingForApproval.class);
                                startActivity(intent);
                                finish();
                            } else {
                                GoToMainActivity();
                            }

                        },
                        error -> {
                            SharedHelper.clearSharedPreferences(SplashScreen.this);
                            displayMessage(getString(R.string.something_went_wrong));
                            GoToBeginActivity();
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " +
                                SharedHelper.getKey(SplashScreen.this, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void GoToMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(this, "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(this, Login.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void showPermissionDialog() {
        Dexter.withActivity(SplashScreen.this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (SharedHelper.getKey(getApplicationContext(), "selectedlanguage") != null &&
                                    !SharedHelper.getKey(getApplicationContext(), "selectedlanguage").isEmpty()) {
                                setLocale(SharedHelper.getKey(getApplicationContext(), "selectedlanguage"));
                                handleCheckStatus.postDelayed(() -> {
                                    if (SharedHelper.getKey(SplashScreen.this, "loggedIn").equalsIgnoreCase(getString(R.string.True))) {
                                        getProfile();
                                    } else {
                                        GoToBeginActivity();
                                    }
                                }, 3000);
                            } else {
                                startActivity(new Intent(SplashScreen.this, IntroActivity.class));
                                finish();
                            }
                        }
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    boolean localeHasChanged = false;
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        localeHasChanged = true;
    }

}
