package com.speed.provider.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.Login;
import com.speed.provider.R;
import com.speed.provider.helper.CustomDialog;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.speed.provider.models.WithdrawAmount;
import com.speed.provider.utills.Utilities;
import com.stripe.android.Stripe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class WithdrawActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView backArrow;
    String locale;
    WithdrawAdapter withdrawAdapter;
    ArrayList<WithdrawAmount> withdrawAmountArrayList;
    private Button addAccountDetailsBtn;
    private Stripe stripe;
    private CustomDialog customDialog;
    private LinearLayout layoutMainId;
    private String bankAccount;
    private String totalAmountTransfer;
    private Button addAmountBtn;
    private EditText amountEditText;
    private TextView earnedMoneyTxtView;
    private String providerId;
    private RecyclerView recyclerWithdraw;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_withdraw);

        stripe = new Stripe(this);
        stripe.setDefaultPublishableKey(URLHelper.STRIPE_TOKEN);
        initViews();

        //SharedHelper.getKey(WithdrawActivity.this,"user_provider_id");
        providerId = SharedHelper.getKey(WithdrawActivity.this, "id");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = this.getResources().getConfiguration().getLocales().get(0).getCountry();
        } else {
            locale = this.getResources().getConfiguration().locale.getCountry();
        }


        Log.v("Country_Code", "Locale" + locale);
        getWithdrawList();

    }

    private void initViews() {
        addAccountDetailsBtn = findViewById(R.id.addAccountDetailsBtn);
        backArrow = findViewById(R.id.backArrow);
        recyclerWithdraw = findViewById(R.id.recyclerWithdraw);
        layoutMainId = findViewById(R.id.layoutMainId);
        earnedMoneyTxtView = findViewById(R.id.earnedMoneyTxtView);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerWithdraw.setLayoutManager(mLayoutManager);
        recyclerWithdraw.setItemAnimator(new DefaultItemAnimator());
        addAccountDetailsBtn.setOnClickListener(this);

        backArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backArrow:
                startActivity(new Intent(WithdrawActivity.this, MainActivity.class));

                break;
            case R.id.addAccountDetailsBtn:
                Intent intent = new Intent(this, WithdrawAmountActivity.class);
                startActivityForResult(intent, 1);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                getWithdrawList();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    public void getWithdrawList() {

        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();
        String urlAddMoney = URLHelper.GET_WITHDRAW_LIST;

        Log.v("Money_TRANSFER_URL", "URL" + urlAddMoney);
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlAddMoney, object, response -> {
            try {
                if (response != null) {
                    Log.v("TAG", "RESPONSE" + response);
                    if (response.optInt("status") == 1) {
                        int totalEarn = response.optInt("totalEarn");
                        earnedMoneyTxtView.setText(totalEarn + ""+SharedHelper.getKey(getApplicationContext(), "currency"));
                        withdrawAdapter = new WithdrawAdapter(response);
                        recyclerWithdraw.setAdapter(withdrawAdapter);


                    } else {
                        int totalEarn = response.optInt("totalEarn");
                        earnedMoneyTxtView.setText( totalEarn + " AED");
                    }

                } else {
                    displayMessage(response.optString("msg"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            customDialog.dismiss();
        }, error -> {
            displayMessage(getString(R.string.something_went_wrong));
            customDialog.dismiss();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(getApplicationContext(), "access_token"));
                return headers;
            }
        };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(getApplicationContext(), Login.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        WithdrawActivity.this.finish();
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    private class WithdrawAdapter extends RecyclerView.Adapter<WithdrawAdapter.MyViewHolder> {
        JSONObject jsonResponse;

        public WithdrawAdapter(JSONObject jsonResponse) {
            this.jsonResponse = jsonResponse;
        }

        @Override
        public WithdrawAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.withdraw_item_adapter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(WithdrawAdapter.MyViewHolder holder, final int position) {
            JSONArray jsonArray = jsonResponse.optJSONArray("data");
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(position);
                holder.lblWithdrawAmount.setText(SharedHelper.getKey(getApplicationContext(), "currency") + jsonObject.optString("amount"));
                String from = null;
                try {
                    from = Utilities.getDateFormate(jsonObject.optString("created_at"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.lblWithdrawDateTime.setText(from);
                holder.lblWithdrawStatus.setText(jsonObject.optString("status"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return jsonResponse.optJSONArray("data").length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView lblWithdrawAmount, lblWithdrawDateTime, lblWithdrawStatus;

            public MyViewHolder(View itemView) {
                super(itemView);
                lblWithdrawAmount = itemView.findViewById(R.id.lblWithdrawAmount);
                lblWithdrawDateTime = itemView.findViewById(R.id.lblWithdrawDateTime);
                lblWithdrawStatus = itemView.findViewById(R.id.lblWithdrawStatus);
            }
        }
    }


}
