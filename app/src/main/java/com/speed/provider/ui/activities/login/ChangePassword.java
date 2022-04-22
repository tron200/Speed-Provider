package com.speed.provider.ui.activities.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.Login;
import com.speed.provider.R;
import com.speed.provider.helper.ConnectionHelper;
import com.speed.provider.helper.CustomDialog;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.speed.provider.utills.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ChangePassword extends AppCompatActivity {
    public Context context = ChangePassword.this;
    public Activity activity = ChangePassword.this;
    String TAG = "ChangePasswordActivity";
    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;
    Button changePasswordBtn;
    ImageView backArrow;
    EditText current_password, new_password, confirm_new_password;
    Utilities utils = new Utilities();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this,"selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_change_password);
        findViewByIdandInitialization();

        backArrow.setOnClickListener(view -> onBackPressed());

        changePasswordBtn.setOnClickListener(view -> {
            String current_password_value = current_password.getText().toString();
            String new_password_value = new_password.getText().toString();
            String confirm_password_value = confirm_new_password.getText().toString();
            if (current_password_value == null || current_password_value.equalsIgnoreCase("")) {
                displayMessage(getString(R.string.please_enter_current_pass));
            } else if (new_password_value == null || new_password_value.equalsIgnoreCase("")) {
                displayMessage(getString(R.string.please_enter_new_pass));
            } else if (confirm_password_value == null || confirm_password_value.equalsIgnoreCase("")) {
                displayMessage(getString(R.string.please_enter_confirm_pass));
            } else if (!new_password_value.equals(confirm_password_value)) {
                displayMessage(getString(R.string.different_passwords));
            } else {
                changePassword();
            }
        });

    }

    public void findViewByIdandInitialization() {
        current_password = findViewById(R.id.current_password);
        new_password = findViewById(R.id.new_password);
        confirm_new_password = findViewById(R.id.confirm_password);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);
        backArrow = findViewById(R.id.backArrow);
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
    }


    private void changePassword() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("password", new_password.getText().toString());
            object.put("password_confirmation", confirm_new_password.getText().toString());
            object.put("password_old", current_password.getText().toString());
            utils.print("ChangePasswordAPI", "" + object);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                URLHelper.CHANGE_PASSWORD_API,
                object,
                response -> {
                    customDialog.dismiss();
                    utils.print("SignInResponse", response.toString());
                    displayMessage(response.optString("message"));
                }, error -> {
            customDialog.dismiss();
            displayMessage(getString(R.string.something_went_wrong));
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(activity, "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(activity, Login.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }
}
