package com.speed.provider.ui.activities.login;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.R;
import com.speed.provider.ui.activities.MainActivity;
import com.speed.provider.ui.activities.OtpVerification;
import com.speed.provider.helper.CustomDialog;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.speed.provider.utills.Utilities;
import com.google.gson.JsonObject;
import com.hbb20.CountryCodePicker;
import com.koushikdutta.ion.Ion;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    private final int GOOGLE_LOGIN = 0001;
    private final int FACEBOOK_LOGIN = 0002;
    private final int OTP_LOGIN = 0003;
    JsonObject socialJson;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    private String deviceToken, deviceUDID;
    private String socialUrl, loginType;
    private String mobile = "";
    private CustomDialog customDialog;

    @OnClick(R.id.btnGoogle)
    void btnGoogleClick() {
        startActivityForResult(new Intent(this, GoogleLoginActivity.class), GOOGLE_LOGIN);
    }

    @OnClick(R.id.btnFb)
    void btnFbClick() {
        startActivityForResult(new Intent(this, FaceBookLoginActivity.class), FACEBOOK_LOGIN);
    }

    @OnClick(R.id.txtSignUp)
    void txtSignUpClick() {
        startActivity(new Intent(this, SignUp.class));
    }

    @OnClick(R.id.txtForget)
    void txtForgetClick() {
        startActivity(new Intent(this, ForgotPassword.class));
    }

    @OnClick(R.id.btnLogin)
    void btnLoginClick() {
        if (etEmail.getText().toString().equals("") ||
                etEmail.getText().toString().equalsIgnoreCase(getString(R.string.sample_mail_id))) {
            displayMessage(getString(R.string.email_validation));
        } else if (!Utilities.isValidEmail(etEmail.getText().toString())) {
            displayMessage(getString(R.string.not_valid_email));
        } else if (etPassword.getText().toString().equals("") ||
                etPassword.getText().toString()
                        .equalsIgnoreCase(getString(R.string.password_txt))) {
            displayMessage(getString(R.string.password_validation));
        } else if (etPassword.length() < 6) {
            displayMessage(getString(R.string.password_size));
        } else {
            signIn();
        }
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        customDialog = new CustomDialog(this);
        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<InstallationTokenResult> task) {
                String newToken = task.getResult().getToken();
                Log.e("newToken", newToken);
                SharedHelper.putKey(getApplicationContext(), "device_token", "" + newToken);
                deviceToken = newToken;
            }
        });
        try {
            deviceUDID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        finish();
    }

    public void GoToMainActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        finish();
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (requestCode == GOOGLE_LOGIN) {
                    data.getStringExtra("userName");
                    data.getStringExtra("userMail");
                    data.getStringExtra("userId");
                    data.getStringExtra("userToken");

                    final JsonObject json = new JsonObject();
                    json.addProperty("device_type", "android");
                    json.addProperty("device_token", deviceToken);
                    json.addProperty("accessToken", data.getStringExtra("userToken"));
                    json.addProperty("device_id", deviceUDID);
                    json.addProperty("login_by", "google");
                    json.addProperty("mobile", mobile);


                    socialJson = json;
                    socialUrl = URLHelper.GOOGLE_LOGIN;
                    loginType = "google";

                    phoneLogin();
                }
                if (requestCode == FACEBOOK_LOGIN) {
                    data.getStringExtra("userName");
                    data.getStringExtra("userMail");
                    data.getStringExtra("userId");
                    data.getStringExtra("userToken");

                    final JsonObject json = new JsonObject();
                    json.addProperty("device_type", "android");
                    json.addProperty("device_token", deviceToken);
                    json.addProperty("accessToken", data.getStringExtra("userToken"));
                    json.addProperty("device_id", deviceUDID);
                    json.addProperty("login_by", "facebook");
                    json.addProperty("first_name", data.getStringExtra("userName"));
                    json.addProperty("last_name", "");
                    json.addProperty("id", data.getStringExtra("userId"));
                    json.addProperty("email", data.getStringExtra("userMail"));
                    json.addProperty("avatar", "");
                    json.addProperty("mobile", mobile);

                    socialJson = json;
                    socialUrl = URLHelper.FACEBOOK_LOGIN;
                    loginType = "facebook";

                    phoneLogin();
                }
                if (requestCode == OTP_LOGIN) {
                    login(socialJson, socialUrl, loginType);
                }
            }
        }
    }

    private void phoneLogin() {
        Dialog dialog = new Dialog(this, R.style.AppTheme_NoActionBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.mobileverification);
        dialog.setCancelable(true);
        dialog.show();
        ImageView imgBack = dialog.findViewById(R.id.imgBack);
        CountryCodePicker ccp = dialog.findViewById(R.id.ccp);
        ImageButton nextIcon = dialog.findViewById(R.id.nextIcon);
        EditText mobile_no = dialog.findViewById(R.id.mobile_no);
        imgBack.setOnClickListener(v -> dialog.dismiss());
        nextIcon.setOnClickListener(v -> {
            dialog.dismiss();
            String phone = ccp.getSelectedCountryCodeWithPlus() + mobile_no.getText().toString();
            mobile = phone;
            socialJson.addProperty("mobile", mobile);
            Intent intent = new Intent(LoginActivity.this, OtpVerification.class);
            intent.putExtra("phonenumber", phone);
            startActivityForResult(intent, OTP_LOGIN);


        });

    }

    public void login(final JsonObject json, final String URL, final String Loginby) {
        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();
        Ion.with(this)
                .load(URL)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback((e, result) -> {
                    if ((customDialog != null) && customDialog.isShowing())
                        customDialog.dismiss();
                    if (e != null) {
                        if (e instanceof NetworkErrorException) {
                            displayMessage(getString(R.string.oops_connect_your_internet));
                        } else if (e instanceof TimeoutException) {
                            login(json, URL, Loginby);
                        }
                        return;
                    }
                    if (result != null) {
                        Log.e(Loginby + "_Response", result.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            SharedHelper.putKey(this, "token_type", jsonObject.optString("token_type"));
                            SharedHelper.putKey(this, "access_token", jsonObject.optString("access_token"));
                            if (Loginby.equalsIgnoreCase("facebook"))
                                SharedHelper.putKey(this, "login_by", "facebook");
                            if (Loginby.equalsIgnoreCase("google"))
                                SharedHelper.putKey(this, "login_by", "google");
                            getProfile();
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });
    }

    private void getProfile() {
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        URLHelper.USER_PROFILE_API + "?device_type=android&device_id="
                                + deviceUDID + "&device_token=" + deviceToken,
                        object,
                        response -> {
                            if ((customDialog != null) && customDialog.isShowing())
                                customDialog.dismiss();
                            SharedHelper.putKey(getApplicationContext(), "id",
                                    response.optString("id"));
                            SharedHelper.putKey(getApplicationContext(), "first_name",
                                    response.optString("first_name"));
                            SharedHelper.putKey(getApplicationContext(), "email",
                                    response.optString("email"));
                            SharedHelper.putKey(getApplicationContext(), "gender", "" +
                                    response.optString("gender"));
                            SharedHelper.putKey(getApplicationContext(), "mobile",
                                    response.optString("mobile"));
                            SharedHelper.putKey(getApplicationContext(), "approval_status",
                                    response.optString("status"));
                            SharedHelper.putKey(getApplicationContext(), "loggedIn",
                                    getString(R.string.True));
                            SharedHelper.putKey(getApplicationContext(), "rating", response.optString("rating"));
                            SharedHelper.putKey(getApplicationContext(), "currency", response.optString("currency"));
                            if (response.optString("avatar").startsWith("http"))
                                SharedHelper.putKey(getApplicationContext(), "picture",
                                        response.optString("avatar"));
                            else
                                SharedHelper.putKey(getApplicationContext(), "picture",
                                        URLHelper.BASE + "storage/app/public/" +
                                                response.optString("avatar"));

                            SharedHelper.getKey(getApplicationContext(), "picture");

                            if (response.optJSONObject("service") != null) {
                                try {
                                    JSONObject service = response.optJSONObject("service");
                                    if (service.optJSONObject("service_type") != null) {
                                        JSONObject serviceType = service.optJSONObject("service_type");
                                        SharedHelper.putKey(getApplicationContext(), "service",
                                                serviceType.optString("name"));
                                        SharedHelper.putKey(getApplicationContext(), "service_image",
                                                serviceType.optString("image"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            SharedHelper.putKey(getApplicationContext(), "sos",
                                    response.optString("sos"));
                            SharedHelper.putKey(getApplicationContext(), "sos", response.optString("sos"));
                            SharedHelper.putKey(getApplicationContext(), "loggedIn", getString(R.string.True));
                            GoToMainActivity();

                        },
                        error -> {
                            if ((customDialog != null) && customDialog.isShowing())
                                customDialog.dismiss();
                            displayMessage(getString(R.string.something_went_wrong));
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void signIn() {
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        try {

            object.put("grant_type", "password");
            object.put("client_id", URLHelper.client_id);
            object.put("client_secret", URLHelper.client_secret);
            object.put("email", etEmail.getText().toString().trim());
            object.put("password", etPassword.getText().toString().trim());
            object.put("scope", "");
            object.put("device_type", "android");
            object.put("device_id", deviceUDID);
            object.put("device_token", deviceToken);
            object.put("logged_in", "1");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST,
                        URLHelper.login,
                        object,
                        response -> {
                            if ((customDialog != null) && customDialog.isShowing())
                                customDialog.dismiss();
                            SharedHelper.putKey(getApplicationContext(),
                                    "access_token", response.optString("access_token"));
                            SharedHelper.putKey(getApplicationContext(),
                                    "refresh_token", response.optString("refresh_token"));
                            SharedHelper.putKey(getApplicationContext(),
                                    "token_type", response.optString("token_type"));
                            if (!response.optString("currency").equalsIgnoreCase("") && response.optString("currency") != null)
                                SharedHelper.putKey(getApplicationContext(), "currency", response.optString("currency"));
                            else
                                SharedHelper.putKey(getApplicationContext(), "currency", "$");
                            getProfile();
                        },
                        error -> {
                            if ((customDialog != null) && customDialog.isShowing())
                                customDialog.dismiss();
                            displayMessage(getString(R.string.something_went_wrong));
                        }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

    }


}
