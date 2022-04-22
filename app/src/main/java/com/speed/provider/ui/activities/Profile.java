package com.speed.provider.ui.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.R;
import com.speed.provider.ui.activities.login.ChangePassword;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;


public class Profile extends AppCompatActivity implements View.OnClickListener {
    ImageView backArrow;
    TextView txtuserName, txtEdituser, txtVehiclename, txtvehicleEdit, txtPassword, txtDocuments, txtEarning;
    CircleImageView img_profile;
    ImageView img_car;
    Button btnLogout;
    GoogleApiClient mGoogleApiClient;
    SwitchCompat language;
    Locale myLocale;
    TextView txtReview;
    String currentLanguage = "en", currentLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_profile);
        currentLanguage = getIntent().getStringExtra(currentLang);

        findview();

        backArrow.setOnClickListener(view -> onBackPressed());
    }

    private void findview() {
        img_profile = findViewById(R.id.img_profile);
        img_car = findViewById(R.id.img_car);
        backArrow = findViewById(R.id.backArrow);
        txtuserName = findViewById(R.id.txtuserName);
        txtEdituser = findViewById(R.id.txtEdituser);
        txtVehiclename = findViewById(R.id.txtVehiclename);
        txtvehicleEdit = findViewById(R.id.txtvehicleEdit);
        txtPassword = findViewById(R.id.txtPassword);
        txtDocuments = findViewById(R.id.txtDocuments);
        txtEarning = findViewById(R.id.txtEarning);
        btnLogout = findViewById(R.id.btnLogout);
        language = findViewById(R.id.changeLanguage);
        txtReview = findViewById(R.id.txtReview);


        language.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });

        txtuserName.setText(SharedHelper.getKey(Profile.this, "first_name"));
        txtVehiclename.setText(SharedHelper.getKey(Profile.this, "service"));
        Picasso.get().load(SharedHelper.getKey(Profile.this, "picture"))
                .placeholder(R.drawable.ic_dummy_user)
                .error(R.drawable.ic_dummy_user)
                .into(img_profile);
        if (!SharedHelper.getKey(Profile.this, "service_image").isEmpty()) {
            Picasso.get().load(SharedHelper.getKey(Profile.this, "service_image"))
                    .placeholder(R.drawable.car_select)
                    .error(R.drawable.car_select)
                    .into(img_car);
        }
        txtEdituser.setOnClickListener(this);
        txtPassword.setOnClickListener(this);
        txtDocuments.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        txtEarning.setOnClickListener(this);
        txtReview.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (!SharedHelper.getKey(Profile.this, "picture").equalsIgnoreCase("")
                    && SharedHelper.getKey(Profile.this, "picture") != null
                    && !SharedHelper.getKey(Profile.this, "picture").equalsIgnoreCase("null")) {
                Picasso.get()
                        .load(SharedHelper.getKey(Profile.this, "picture"))
                        .placeholder(R.drawable.ic_dummy_user)
                        .error(R.drawable.ic_dummy_user)
                        .into(img_profile);
            } else {
                Picasso.get()
                        .load(R.drawable.ic_dummy_user)
                        .placeholder(R.drawable.ic_dummy_user)
                        .error(R.drawable.ic_dummy_user)
                        .into(img_profile);
            }


            if (SharedHelper.getKey(Profile.this, "first_name") != null) {
                txtuserName.setText(SharedHelper.getKey(Profile.this, "first_name"));
            }
        }
    }//onActivityResult

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txtEdituser) {
            Intent i = new Intent(this, EditProfile.class);
            startActivityForResult(i, 1);
        }
        if (v.getId() == R.id.txtPassword) {
            startActivity(new Intent(Profile.this, ChangePassword.class));
        }
        if (v.getId() == R.id.txtDocuments) {
            new Handler().postDelayed(() -> startActivity(new Intent(Profile.this, DocUploadActivity.class)), 250);
        }
        if (v.getId() == R.id.btnLogout) {
            showLogoutDialog();
        }
        if (v.getId() == R.id.txtEarning) {
            startActivity(new Intent(Profile.this, EarningActivity.class));
        }
        if (v.getId() == R.id.txtReview) {
            startActivity(new Intent(Profile.this, UserReview.class));
        }
    }

    private void showLogoutDialog() {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.logout));
            builder.setMessage(getString(R.string.exit_confirm));
            builder.setPositiveButton(R.string.yes,
                    (dialog, which) -> logout());
            builder.setNegativeButton(R.string.no, (dialog, which) -> {
                //Reset to previous seletion menu in navigation
                dialog.dismiss();
            });
            builder.setCancelable(false);
            final AlertDialog dialog = builder.create();
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            dialog.setOnShowListener(arg -> {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
            });
            dialog.show();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void logout() {
        JSONObject object = new JSONObject();
        try {
            object.put("id", SharedHelper.getKey(getApplicationContext(), "id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.POST,
                        URLHelper.LOGOUT,
                        object,
                        response -> {
                            if (SharedHelper.getKey(getApplicationContext(), "login_by").equals("facebook"))
                                LoginManager.getInstance().logOut();
                            if (SharedHelper.getKey(getApplicationContext(), "login_by").equals("google"))
                                signOut();
                            if (!SharedHelper.getKey(Profile.this, "account_kit_token").equalsIgnoreCase("")) {
                                Log.e("MainActivity", "Account kit logout: " + SharedHelper.getKey(Profile.this, "account_kit_token"));
                                SharedHelper.putKey(Profile.this, "account_kit_token", "");
                            }


                            //SharedHelper.putKey(context, "email", "");
                            SharedHelper.clearSharedPreferences(Profile.this);
                            Intent goToLogin = new Intent(Profile.this, SplashScreen.class);
                            goToLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(goToLogin);
                            finishAffinity();
                        }, error -> {
                    displayMessage(getString(R.string.something_went_wrong));
                }) {
                    @Override
                    public java.util.Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        Log.e("getHeaders: Token", SharedHelper.getKey(getApplicationContext(), "access_token") + SharedHelper.getKey(getApplicationContext(), "token_type"));
                        headers.put("Authorization", "" + "Bearer" + " " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    private void signOut() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //taken from google api console (Web api client id)
//                .requestIdToken("795253286119-p5b084skjnl7sll3s24ha310iotin5k4.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {

                FirebaseAuth.getInstance().signOut();
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {
                        if (status.isSuccess()) {
                            Log.d("MainAct", "Google User Logged out");
                           /* Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();*/
                        }
                    });
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.d("MAin", "Google API Client Connection Suspended");
            }
        });
    }

}
