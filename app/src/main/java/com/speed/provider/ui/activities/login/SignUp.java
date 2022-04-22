package com.speed.provider.ui.activities.login;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.Login;
import com.speed.provider.R;
import com.speed.provider.ui.activities.MainActivity;
import com.speed.provider.ui.activities.OtpVerification;
import com.speed.provider.ui.activities.SplashScreen;
import com.speed.provider.helper.ConnectionHelper;
import com.speed.provider.helper.CustomDialog;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.speed.provider.utills.Utilities;
import com.hbb20.CountryCodePicker;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    public static int APP_REQUEST_CODE = 99;
    String TAG = "SignUp";
    Button txtSignIn;
    EditText etName, etEmail, etPassword;
    Button btnSignUp;
    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;
    Utilities utils = new Utilities();
    String device_token, device_UDID;
    //    Button btnFb,btnGoogle;
    Dialog dialog;
    private MaterialSpinner spRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<InstallationTokenResult> task) {
                String newToken = task.getResult().getToken();
                Log.e("newToken", newToken);
                SharedHelper.putKey(getApplicationContext(), "device_token", "" + newToken);
                device_token = newToken;
            }
        });

        device_UDID = android.provider.Settings.Secure.getString(getApplicationContext().getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        setContentView(R.layout.activity_sign_up);
        helper = new ConnectionHelper(getApplicationContext());
        isInternet = helper.isConnectingToInternet();
        txtSignIn = findViewById(R.id.txtSignIn);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtSignIn) {
            startActivity(new Intent(SignUp.this, Login.class));
            finish();
        }
        if (v.getId() == R.id.btnSignUp) {

            Pattern ps = Pattern.compile(".*[0-9].*");
            Matcher firstName = ps.matcher(etName.getText().toString());


            if (etName.getText().toString().equals("") ||
                    etName.getText().toString().equalsIgnoreCase(getString(R.string.first_name))) {
                displayMessage(getString(R.string.first_name_empty));
            } else if (firstName.matches()) {
                displayMessage(getString(R.string.first_name_no_number));
            } else if (etEmail.getText().toString().equals("") ||
                    etEmail.getText().toString().equalsIgnoreCase(getString(R.string.sample_mail_id))) {
                displayMessage(getString(R.string.email_validation));
            } else if (!Utilities.isValidEmail(etEmail.getText().toString())) {
                displayMessage(getString(R.string.not_valid_email));
            } else if (etPassword.getText().toString().equals("") ||
                    etPassword.getText().toString().equalsIgnoreCase(getString(R.string.password_txt))) {
                displayMessage(getString(R.string.password_validation));
            } else if (etPassword.length() < 6) {
                displayMessage(getString(R.string.password_size));
            } else {
                if (isInternet) {


                    checkEmail();

                } else {
                    displayMessage(getString(R.string.something_went_wrong_net));
                }
            }
        }
    }

    private void checkEmail() {
        customDialog = new CustomDialog(SignUp.this);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();
        JSONObject object = new JSONObject();
        try {

            object.put("email", etEmail.getText().toString());
            utils.print("InputToEmailCheck", "" + object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST,
                        URLHelper.email_check,
                        object,
                        response -> {
                            customDialog.dismiss();
                            if (response.optString("status").equalsIgnoreCase("0")) {
                                displayMessage(response.optString("msg"));
                            } else {
                                openphonelogin();
                            }
                        },
                        error -> {
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

    private void openphonelogin() {

        dialog = new Dialog(SignUp.this, R.style.AppTheme_NoActionBar);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.mobileverification);
        dialog.setCancelable(true);
        dialog.show();
        CountryCodePicker ccp = dialog.findViewById(R.id.ccp);
        ImageButton nextIcon = dialog.findViewById(R.id.nextIcon);
        ImageView imgBack = dialog.findViewById(R.id.imgBack);

        EditText mobile_no = dialog.findViewById(R.id.mobile_no);

        imgBack.setOnClickListener(v -> dialog.dismiss());
        nextIcon.setOnClickListener(v -> {
            String phone = ccp.getSelectedCountryCodeWithPlus() + mobile_no.getText().toString();
            SharedHelper.putKey(getApplicationContext(), "mobile", phone);
            Log.v("Phonecode", phone + " ");
            Intent intent = new Intent(SignUp.this, OtpVerification.class);
            intent.putExtra("phonenumber", phone);
            startActivityForResult(intent, APP_REQUEST_CODE);
            dialog.dismiss();
        });
    }


    @Override
    public void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult");
        if (data != null) {
            if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
                dialog.dismiss();
                registerAPI();
            }
        }
    }

    private void registerAPI() {
        customDialog = new CustomDialog(SignUp.this);
        customDialog.setCancelable(false);
        if (customDialog != null)
            customDialog.show();
        JSONObject object = new JSONObject();
        try {

            object.put("device_type", "android");
            object.put("device_id", device_UDID);
            object.put("device_token", "" + device_token);
            object.put("login_by", "manual");
            object.put("first_name", etName.getText().toString());
            object.put("last_name", "");
            object.put("email", etEmail.getText().toString());
            object.put("password", etPassword.getText().toString());
            object.put("password_confirmation", etPassword.getText().toString());
            object.put("mobile", SharedHelper.getKey(getApplicationContext(), "mobile"));

            utils.print("InputToRegisterAPI", "" + object);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST,
                        URLHelper.register,
                        object,
                        response -> {
                            Log.e("registerresponse", response + "");

                            utils.print("SignInResponse", response.toString());
                            if (response.optString("msg").equalsIgnoreCase("The mobile has already been taken.")) {
                                if ((customDialog != null) && (customDialog.isShowing()))
                                    customDialog.dismiss();
                            } else {
                                SharedHelper.putKey(getApplicationContext(), "email", etEmail.getText().toString());
                                SharedHelper.putKey(getApplicationContext(), "password", etPassword.getText().toString());
                                signIn();
                            }
                        },
                        error -> {
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


    private void signIn() {
        if (isInternet) {
            customDialog = new CustomDialog(SignUp.this);
            customDialog.setCancelable(false);
            if (customDialog != null)
                customDialog.show();
            JSONObject object = new JSONObject();
            try {

                object.put("grant_type", "password");
                object.put("client_id", URLHelper.client_id);
                object.put("client_secret", URLHelper.client_secret);
                object.put("email", SharedHelper.getKey(getApplicationContext(), "email"));
                object.put("password", SharedHelper.getKey(getApplicationContext(), "password"));
                object.put("scope", "");
                object.put("device_type", "android");
                object.put("device_id", device_UDID);
                object.put("device_token", device_token);
                object.put("logged_in", "1");
                utils.print("InputToLoginAPI", "" + object);

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
                                utils.print("SignUpResponse", response.toString());
                                SharedHelper.putKey(getApplicationContext(),
                                        "access_token", response.optString("access_token"));
                                SharedHelper.putKey(getApplicationContext(),
                                        "refresh_token", response.optString("refresh_token"));
                                SharedHelper.putKey(getApplicationContext(),
                                        "token_type", response.optString("token_type"));

                                if (!response.optString("currency").equalsIgnoreCase("") &&
                                        response.optString("currency") != null)
                                    SharedHelper.putKey(getApplicationContext(), "currency",
                                            response.optString("currency"));
//                                    SharedHelper.putKey(getApplicationContext(), "currency",
//                                            response.optString("currency"));
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

        } else {
            displayMessage(getString(R.string.something_went_wrong_net));
        }

    }


    public void getProfile() {
        if (isInternet) {
            customDialog = new CustomDialog(SignUp.this);
            customDialog.setCancelable(false);
            if (customDialog != null)
                customDialog.show();
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new
                    JsonObjectRequest(Request.Method.GET,
                            URLHelper.USER_PROFILE_API + "?device_type=android&device_id="
                                    + device_UDID + "&device_token=" + device_token,
                            object,
                            response -> {
                                if ((customDialog != null) && (customDialog.isShowing()))
                                    //customDialog.dismiss();
                                    utils.print("GetProfile", response.toString());
                                SharedHelper.putKey(getApplicationContext(), "id",
                                        response.optString("id"));
                                SharedHelper.putKey(getApplicationContext(), "first_name",
                                        response.optString("first_name"));
//                                SharedHelper.putKey(getApplicationContext(), "last_name",
//                                        response.optString("last_name"));
                                SharedHelper.putKey(getApplicationContext(), "email",
                                        response.optString("email"));
                                SharedHelper.putKey(getApplicationContext(), "picture",
                                        URLHelper.BASE + "storage/app/public/" +
                                                response.optString("picture"));
                                SharedHelper.putKey(getApplicationContext(), "gender",
                                        response.optString("gender"));
                                SharedHelper.putKey(getApplicationContext(), "mobile",
                                        response.optString("mobile"));
                                SharedHelper.putKey(getApplicationContext(), "wallet_balance",
                                        response.optString("wallet_balance"));
                                SharedHelper.putKey(getApplicationContext(), "payment_mode",
                                        response.optString("payment_mode"));
                                SharedHelper.putKey(getApplicationContext(), "rating", response.optString("rating"));
                                SharedHelper.putKey(getApplicationContext(), "currency", response.optString("currency"));
                                if (!response.optString("currency")
                                        .equalsIgnoreCase("") &&
                                        response.optString("currency") != null)
                                    SharedHelper.putKey(getApplicationContext(), "currency",
                                            response.optString("currency"));
//                                    SharedHelper.putKey(getApplicationContext(), "currency",
//                                            response.optString("currency"));
                                else
                                    SharedHelper.putKey(getApplicationContext(), "currency", "$");
                                SharedHelper.putKey(getApplicationContext(), "sos",
                                        response.optString("sos"));
                                SharedHelper.putKey(getApplicationContext(), "loggedIn",
                                        getString(R.string.True));
                                GoToMainActivity();
                            },
                            error -> {
                                customDialog.dismiss();
                                displayMessage(getString(R.string.something_went_wrong));
                            }) {
                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("X-Requested-With", "XMLHttpRequest");
                            headers.put("Authorization", "" + "Bearer"
                                    + " " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                            Log.e("headers", headers + "");
                            return headers;
                        }
                    };

            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            displayMessage(getString(R.string.something_went_wrong_net));
        }
    }

    private void GoToBeginActivity() {
        if (customDialog != null && customDialog.isShowing())
            customDialog.dismiss();
        Intent mainIntent = new Intent(getApplicationContext(), SplashScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }

    public void GoToMainActivity() {
        if (customDialog != null && customDialog.isShowing())
            customDialog.dismiss();
        Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }


}
