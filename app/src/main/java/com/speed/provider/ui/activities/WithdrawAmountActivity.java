package com.speed.provider.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.Login;
import com.speed.provider.R;
import com.speed.provider.ui.adapters.PaymentListAdapter;
import com.speed.provider.helper.CustomDialog;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.speed.provider.models.CardDetails;
import com.speed.provider.utills.Utilities;
import com.stripe.android.Stripe;
import com.ybs.countrypicker.CountryPicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class WithdrawAmountActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<JSONObject> listItems;
    Utilities utils = new Utilities();
    String type = "";
    private CustomDialog customDialog;
    private ArrayList<CardDetails> cardArrayList;
    private ListView payment_list_view;
    private PaymentListAdapter paymentAdapter;
    private Button addBankAccountBtn;
    private Button addAmountBtn;
    private Stripe stripe;
    private EditText addAccountName;
    private ImageView backArrow;
    private EditText amountEditText;
    private String providerID;
    private int accountId;
    private LinearLayout withdrawLayout;
    private EditText addBankName;
    private TextView selectAmountTxt;
    private LinearLayout noBankDetailsFoundLayout;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_withdraw_amount);
        stripe = new Stripe(this);
        stripe.setDefaultPublishableKey(URLHelper.STRIPE_TOKEN);

        providerID = SharedHelper.getKey(WithdrawAmountActivity.this, "id");

        initViews();
        getCardList();


        payment_list_view.setOnItemLongClickListener((parent, view, position, id) -> {

            String accountHolderName = cardArrayList.get(position).getAccountName();
            String bankName = cardArrayList.get(position).getBankName();
            int accountNumber = cardArrayList.get(position).getAccountNumber();
            int routingNumber = cardArrayList.get(position).getRoutingNumber();
            String countryName = cardArrayList.get(position).getCountryName();
            String currency = cardArrayList.get(position).getCurrency();


            bankDetailsPoupUp(accountHolderName, bankName, accountNumber, routingNumber, countryName);

            return true;
        });

    }

    private void bankDetailsPoupUp(String accountHName, String bankName, int accountNumber, int routingNumber, String countryName) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawAmountActivity.this);
        View customView = LayoutInflater.from(WithdrawAmountActivity.this).inflate(R.layout.bank_details_popup, null);
        // View customView = layoutInflater.inflate(R.layout.bank_details_popup,null);
        builder.setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.bank_details)
                .setView(customView)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();

        Button oKBtn = customView.findViewById(R.id.oKBtn);
        //instantiate popup window
        //popupWindow = new PopupWindow(customView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView textViewBankName = customView.findViewById(R.id.textViewBankName);
        TextView textViewAccountNumber = customView.findViewById(R.id.textViewAccountNumber);
        TextView textViewRoutingNumber = customView.findViewById(R.id.textViewRoutingNumber);
        TextView textViewAccountName = customView.findViewById(R.id.textViewAccountName);
        TextView textViewCountry = customView.findViewById(R.id.textViewCountry);

        textViewAccountName.setText(accountHName);
        textViewBankName.setText(bankName);
        textViewAccountNumber.setText(accountNumber + "");
        textViewRoutingNumber.setText(routingNumber + "");
        textViewCountry.setText(countryName);
        //close the popup window on button click
        oKBtn.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void initViews() {
        payment_list_view = findViewById(R.id.payment_list_view);
        addBankAccountBtn = findViewById(R.id.addBankAccountBtn);
        addAmountBtn = findViewById(R.id.addAmountBtn);
        backArrow = findViewById(R.id.backArrow);
        amountEditText = findViewById(R.id.amountEditText);
        withdrawLayout = findViewById(R.id.withdrawLayout);
        noBankDetailsFoundLayout = findViewById(R.id.noBankDetailsFoundLayout);
        selectAmountTxt = findViewById(R.id.selectAmountTxt);
        addBankAccountBtn.setOnClickListener(this);
        backArrow.setOnClickListener(this);
        addAmountBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addBankAccountBtn:
                addAccountDialog();
                break;
            case R.id.backArrow:
                onBackPressed();
                break;
            case R.id.addAmountBtn:
                getWithDrawAmount();
                break;

        }
    }

    private void addAccountDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(WithdrawAmountActivity.this);
        View view = LayoutInflater.from(WithdrawAmountActivity.this).inflate(R.layout.add_bank_account_item, null);
        Button continue_btn = view.findViewById(R.id.addAccountDetailsBtn);
        addAccountName = view.findViewById(R.id.addAccountName);
        addBankName = view.findViewById(R.id.addBankName);
        EditText addAccountNumber = view.findViewById(R.id.addAccountNumber);
        EditText addCountryName = view.findViewById(R.id.addCountryName);
        EditText paypalId = view.findViewById(R.id.paypalId);
        Button cancelBtn = view.findViewById(R.id.cancelBtn);
        RadioGroup rg = view.findViewById(R.id.rg);
        LinearLayout layoutbank = view.findViewById(R.id.layoutbank);
        LinearLayout layoutPaypal = view.findViewById(R.id.layoutPaypal);
        RadioButton radioBank = view.findViewById(R.id.radioBank);
        RadioButton radioPaypal = view.findViewById(R.id.radioPaypal);
        addCountryName.setOnClickListener(v -> {
            CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
            picker.setListener((name, code, dialCode, flagDrawableResID) -> {
                addCountryName.setText(name);
                picker.dismiss();
            });
            picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        });
        radioBank.setChecked(true);
        layoutbank.setVisibility(View.VISIBLE);
        layoutPaypal.setVisibility(View.GONE);
        type = "bank";
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioBank) {
                radioBank.setChecked(true);
                layoutbank.setVisibility(View.VISIBLE);
                layoutPaypal.setVisibility(View.GONE);
                type = "bank";
            } else if (checkedId == R.id.radioPaypal) {
                radioPaypal.setChecked(true);
                layoutbank.setVisibility(View.GONE);
                layoutPaypal.setVisibility(View.VISIBLE);
                type = "paypal";
            }
        });

        builder.setTitle(R.string.add_account_details)
                .setView(view)
                .setCancelable(true);
        final AlertDialog dialog = builder.create();
        continue_btn.setOnClickListener(v -> {
            dialog.dismiss();
            if (type.equals("bank")) {
                if (addBankName.getText().toString().matches("")) {
                    Toast.makeText(WithdrawAmountActivity.this,
                            getApplicationContext().getResources()
                                    .getString(R.string.bank_name), Toast.LENGTH_SHORT).show();
                } else if (addAccountName.getText().toString().matches("")) {
                    Toast.makeText(WithdrawAmountActivity.this,
                            getApplicationContext().getResources()
                                    .getString(R.string.account_holder_name), Toast.LENGTH_SHORT).show();
                } else if (addAccountNumber.getText().toString().matches("")) {
                    Toast.makeText(WithdrawAmountActivity.this,
                            getApplicationContext().getResources()
                                    .getString(R.string.account_number), Toast.LENGTH_SHORT).show();
                } else {

                    addAccountDetails(addAccountNumber.getText().toString(), addCountryName.getText().toString(), "", type);

                }
            } else {
                if (paypalId.getText().toString().isEmpty()) {
                    Toast.makeText(WithdrawAmountActivity.this,
                            "Enter Paypal id", Toast.LENGTH_SHORT).show();
                } else {
                    addAccountDetails("", "", paypalId.getText().toString(), type);
                }
            }
        });
        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void addAccountDetails(String accountNumber, String countryCode, String paypalId, String type) {

        getAddAccountDetails(addBankName.getText().toString(), addAccountName.getText().toString(), accountNumber, "110000000", paypalId, type);

    }


    // get added bank and card list api
    public void getCardList() {
        customDialog = new CustomDialog(WithdrawAmountActivity.this);
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET, URLHelper.GET_CARD_LIST_DETAILS,
                        object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (customDialog != null && customDialog.isShowing())
                                customDialog.dismiss();
                            Log.e(this.getClass().getName(), "RESPONSE_CARD" + response);
                            if (response != null) {
                                cardArrayList = new ArrayList<>();
                                if (response.optInt("status") == 1) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray != null) {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            CardDetails cardDetails = new CardDetails();
                                            cardDetails.setAccountName(jsonObject.optString("account_name"));
                                            cardDetails.setAccountNumber(jsonObject.optInt("account_number"));
                                            cardDetails.setBankName(jsonObject.optString("bank_name"));
                                            cardDetails.setRoutingNumber(jsonObject.optInt("routing_number"));
                                            cardDetails.setCountryName(jsonObject.optString("country"));
                                            cardDetails.setCurrency(jsonObject.optString("currency"));
                                            cardDetails.setType(jsonObject.optString("type"));
                                            cardDetails.setPaypal_id(jsonObject.optString("paypal_id"));
                                            SharedHelper.putKey(WithdrawAmountActivity.this, "AccountId_SP", jsonObject.getString("id"));
                                            cardArrayList.add(cardDetails);
                                        }
                                    }
                                    paymentAdapter = new PaymentListAdapter(getApplicationContext(), R.layout.payment_list_item, cardArrayList);
                                    payment_list_view.setAdapter(paymentAdapter);
                                } else {
                                    Log.e("TAG", "NO_BANK_DETAILS_FOUND");
                                    withdrawLayout.setVisibility(View.GONE);
                                    selectAmountTxt.setVisibility(View.GONE);
                                    noBankDetailsFoundLayout.setVisibility(View.VISIBLE);
                                }

                            } else {
                                Log.e("TAG", "NO_BANK_DETAILS_FOUND");
                                withdrawLayout.setVisibility(View.GONE);
                                selectAmountTxt.setVisibility(View.GONE);
                                noBankDetailsFoundLayout.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("TAG", "EXCEPTION==" + e.getMessage());
                        }

                    }
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


    // add bank account details
    public void getAddAccountDetails(String bankName, String accountName, String accountNumber, String routingNumber, String paypalId, String type) {

        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();
        String url = "";
        if (type.equalsIgnoreCase("bank")) {
            url = URLHelper.GET_ADD_BANK_DETAILS + accountName + "&account_number=" + Long.parseLong(accountNumber) + "&routing_number=" + routingNumber + "&country=" + "United States" + "&currency=USD" + "&bank_name=" + bankName +
                    "&type=" + type + "&paypal_id=" + paypalId;
        } else {
            url = URLHelper.GET_ADD_BANK_DETAILS + accountName + "&account_number=" + 0 + "&routing_number=" + routingNumber + "&country=" + "United States" + "&currency=USD" + "&bank_name=" + bankName +
                    "&type=" + type + "&paypal_id=" + paypalId;
        }

        Log.e("TAG", "URLS:" + url);
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, object, response -> {
            try {
                if (customDialog.isShowing())
                    customDialog.dismiss();
                if (response != null) {
                    if (response.optInt("status") == 1) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        SharedHelper.putKey(WithdrawAmountActivity.this, "AccountName_SP", jsonObject.getString("account_name"));
                        SharedHelper.putKey(WithdrawAmountActivity.this, "AccountId_SP", jsonObject.getString("id"));
                        startActivity(new Intent(WithdrawAmountActivity.this, WithdrawAmountActivity.class));
                    } else {
                        displayMessage(getApplicationContext().getResources()
                                .getString(R.string.you_not_have_an_account));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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

    // get amount withdraw api
    public void getWithDrawAmount() {

        customDialog = new CustomDialog(this);
        customDialog.setCancelable(false);
        customDialog.show();
        String accountIdSp = SharedHelper.getKey(WithdrawAmountActivity.this, "AccountId_SP");
        String urlWithDrawMoney = URLHelper.WITHDRAW_REQUEST + providerID + "&bank_account_id=" + accountIdSp + "&amount=" + amountEditText.getText().toString();

        Log.e("Money_TRANSFER_URL", "URL" + urlWithDrawMoney);
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlWithDrawMoney, object, response -> {
            try {
                if (response != null) {
                    Log.e("Money_TRANSFER_URL", "Response" + response + " ");
                    if (response.optInt("status") == 1) {
                        Toast.makeText(WithdrawAmountActivity.this, getApplicationContext().getResources()
                                .getString(R.string.fifteen_days), Toast.LENGTH_LONG).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
//                            displayMessage(getApplicationContext().getResources()
//                                    .getString(R.string.fifteen_days));
//
                    } else {
                        displayMessage(response.optString("msg"));
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
        WithdrawAmountActivity.this.finish();
    }

    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }


}
