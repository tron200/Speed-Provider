package com.speed.provider.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.R;
import com.speed.provider.helper.ConnectionHelper;
import com.speed.provider.helper.CustomDialog;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class EarningActivity extends AppCompatActivity {
    Boolean isInternet;
    Activity activity;

    EarningsAdapter earningsAdapter;
    RecyclerView rcvRides;
    RelativeLayout errorLayout;
    ConnectionHelper helper;
    CustomDialog customDialog;
    TextView lblTarget, lblEarnings, lblCommision;
    ImageView imgBack;
    //    CircularProgressBar custom_progressBar,custom_progressBar1;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.fragment_earnings);

        findViewByIdAndInitialize();
        activity = EarningActivity.this;

        if (isInternet) {
            getEarningsList();
        } else {
            showDialog();
        }
        BarChart chart = findViewById(R.id.chart);

        BarData barData = new BarData(getXAxisValues(), getDataSet());
        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.setDescription("");
        chart.animateXY(1000, 1000);
        chart.invalidate();
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("M");
        xAxis.add("Tu");
        xAxis.add("W");
        xAxis.add("Th");
        xAxis.add("F");
        xAxis.add("Sa");
        xAxis.add("Su");
        return xAxis;
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        ArrayList<BarEntry> valueSet2 = new ArrayList<>();
        BarEntry v2e1 = new BarEntry(140.000f, 0); // Jan
        valueSet2.add(v2e1);
        BarEntry v2e2 = new BarEntry(90.000f, 1); // Feb
        valueSet2.add(v2e2);
        BarEntry v2e3 = new BarEntry(120.000f, 2); // Mar
        valueSet2.add(v2e3);
        BarEntry v2e4 = new BarEntry(60.000f, 3); // Apr
        valueSet2.add(v2e4);
        BarEntry v2e5 = new BarEntry(20.000f, 4); // May
        valueSet2.add(v2e5);
        BarEntry v2e6 = new BarEntry(80.000f, 5); // Jun
        valueSet2.add(v2e6);
        BarEntry v2e8 = new BarEntry(70.000f, 6); // Sunday
        valueSet2.add(v2e8);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Total Trips");
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Total Earnings");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        dataSets = new ArrayList<>();
        //dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        return dataSets;
    }

    public void findViewByIdAndInitialize() {
        rcvRides = findViewById(R.id.rcvRides);
        errorLayout = findViewById(R.id.errorLayout);
        lblTarget = findViewById(R.id.lblTarget);
        lblEarnings = findViewById(R.id.lblEarnings);
        lblCommision = findViewById(R.id.lblCommision);
        imgBack = findViewById(R.id.backArrow);
        errorLayout.setVisibility(View.GONE);
        helper = new ConnectionHelper(EarningActivity.this);
        isInternet = helper.isConnectingToInternet();

        imgBack.setOnClickListener(v -> {
            startActivity(new Intent(activity, MainActivity.class));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void getEarningsList() {

        customDialog = new CustomDialog(activity);
        customDialog.setCancelable(false);
        customDialog.show();

        JSONObject object = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET, URLHelper.TARGET_API,
                        object,
                        response -> {
                            try {
                                if (response != null) {
                                    Log.e("response", response + "re");
                                    float total_price = 0;
                                    float total_commsion = 0;

                                    for (int i = 0; i < response.optJSONArray("rides").length(); i++) {
                                        try {
                                            JSONObject jsonObject = response.optJSONArray("rides").getJSONObject(i);
                                            JSONObject jsonObject2 = jsonObject.getJSONObject("payment");
                                            total_price += Float.parseFloat(jsonObject2.optString("total"));
                                            total_commsion += Float.parseFloat(jsonObject2.optString("commision"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    lblTarget.setText(response.optJSONArray("rides").length() + " ");


                                    String strTotalPrice = Math.round(total_price) + "";
                                    String currency = SharedHelper.getKey(activity, "currency");
                                    SharedHelper.putKey(activity, "totalearning", currency + strTotalPrice);
                                    lblEarnings.setText(response.optJSONObject("rides_count").getString("overall")+" "+currency);
                                    lblCommision.setText(response.optJSONObject("rides_count").getString("commission")+" "+currency);

                                    earningsAdapter = new EarningsAdapter(response);
                                    //  recyclerView.setHasFixedSize(true);
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                    rcvRides.setLayoutManager(mLayoutManager);
                                    rcvRides.setItemAnimator(new DefaultItemAnimator());
                                    if (earningsAdapter != null && earningsAdapter.getItemCount() > 0) {
                                        rcvRides.setVisibility(View.VISIBLE);
                                        errorLayout.setVisibility(View.GONE);
                                        rcvRides.setAdapter(earningsAdapter);
                                    } else {
                                        lblEarnings.setText("0");
                                        errorLayout.setVisibility(View.VISIBLE);
                                        rcvRides.setVisibility(View.GONE);
                                    }

                                } else {
                                    errorLayout.setVisibility(View.VISIBLE);
                                    rcvRides.setVisibility(View.GONE);
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
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + SharedHelper.getKey(activity, "access_token"));
                        return headers;
                    }
                };

        ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void processRides(String strRides) {

        try {
            JSONArray jsonArray = new JSONArray(strRides);
            if (jsonArray != null && jsonArray.length() > 0) {

            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void GoToBeginActivity() {
        Intent mainIntent = new Intent(activity, SplashScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }


    public void displayMessage(String toastString) {
        Toasty.info(this, toastString, Toast.LENGTH_SHORT, true).show();
    }

    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        return monthName;
    }

    private String getDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dateName = new SimpleDateFormat("dd").format(cal.getTime());
        return dateName;
    }

    private String getYear(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String yearName = new SimpleDateFormat("yyyy").format(cal.getTime());
        return yearName;
    }

    private String getTime(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String timeName = new SimpleDateFormat("hh:mm a").format(cal.getTime());
        return timeName;
    }

    public void animateTextView(int initialValue, int finalValue, final int target, final TextView textview) {
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(0.8f);
        int start = Math.min(initialValue, finalValue);
        int end = Math.max(initialValue, finalValue);
        int difference = Math.abs(finalValue - initialValue);
        Handler handler = new Handler();
        for (int count = start; count <= end; count++) {
            int time = Math.round(decelerateInterpolator.getInterpolation((((float) count) / difference)) * 100) * count;
            final int finalCount = ((initialValue > finalValue) ? initialValue - count : count);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textview.setText(finalCount + "");
//                    textview.setText(finalCount + "/"+target);
                }
            }, time);
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EarningActivity.this);
        builder.setMessage(getString(R.string.connect_to_network))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.connect_to_wifi), (dialog, id) -> {
                    alert.dismiss();
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                })
                .setNegativeButton(getString(R.string.quit), (dialog, id) -> {
                    finish();
                    alert.dismiss();
                });
        if (alert == null) {
            alert = builder.create();
            alert.show();
        }
    }

    private class EarningsAdapter extends RecyclerView.Adapter<EarningsAdapter.MyViewHolder> {
        JSONObject jsonResponse;

        public EarningsAdapter(JSONObject jsonResponse) {
            this.jsonResponse = jsonResponse;
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.earnings_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            JSONArray jsonArray = jsonResponse.optJSONArray("rides");
            try {

                JSONObject jsonObject = jsonArray.getJSONObject(position);
                try {
                    holder.lblTime.setText(getTime(jsonObject.optString("assigned_at")));
                    holder.lblDate.setText(getDate(jsonObject.optString("assigned_at")) + " " + getMonth(jsonObject.optString("assigned_at")) +
                            " " + getYear(jsonObject.optString("assigned_at")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Float price = Float.parseFloat(jsonArray.getJSONObject(position).getJSONObject("payment").optString("total"));


                holder.lblAmount.setText(price +" "+ SharedHelper.getKey(activity, "currency"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return jsonResponse.optJSONArray("rides").length();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView lblTime, lblDate, lblAmount;

            public MyViewHolder(View itemView) {
                super(itemView);
                lblTime = itemView.findViewById(R.id.lblTime);
                lblDate = itemView.findViewById(R.id.lblDate);
                lblAmount = itemView.findViewById(R.id.lblAmount);
            }
        }
    }
}
