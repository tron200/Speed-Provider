package com.speed.provider.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.speed.provider.ClassLuxApp;
import com.speed.provider.R;
import com.speed.provider.ui.activities.EarningActivity;
import com.speed.provider.ui.activities.MainActivity;
import com.speed.provider.ui.activities.SplashScreen;
import com.speed.provider.helper.CustomDialog;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.helper.URLHelper;
import com.daasuu.cat.CountAnimationTextView;

import org.json.JSONObject;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryFragment extends Fragment implements View.OnClickListener {

    ImageView imgBack;
    LinearLayout cardLayout;

    CountAnimationTextView noOfRideTxt;
    CountAnimationTextView scheduleTxt;
    CountAnimationTextView revenueTxt;
    CountAnimationTextView cancelTxt;

    CardView ridesCard;
    CardView cancelCard;
    CardView scheduleCard;
    CardView revenueCard;

    TextView currencyTxt;

    int rides, revenue, schedule, cancel;
    Double doubleRevenue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        findViewsById(view);
        setClickListeners();

        getProviderSummary();
        return view;

    }

    private void setClickListeners() {
        imgBack.setOnClickListener(this);
        revenueCard.setOnClickListener(this);
        ridesCard.setOnClickListener(this);
        revenueCard.setOnClickListener(this);
        scheduleCard.setOnClickListener(this);
    }

    private void findViewsById(View view) {
        imgBack = view.findViewById(R.id.backArrow);
        cardLayout = view.findViewById(R.id.card_layout);
        noOfRideTxt = view.findViewById(R.id.no_of_rides_txt);
        scheduleTxt = view.findViewById(R.id.schedule_txt);
        cancelTxt = view.findViewById(R.id.cancel_txt);
        revenueTxt = view.findViewById(R.id.revenue_txt);
        currencyTxt = view.findViewById(R.id.currency_txt);
        revenueCard = view.findViewById(R.id.revenue_card);
        scheduleCard = view.findViewById(R.id.schedule_card);
        ridesCard = view.findViewById(R.id.rides_card);
        cancelCard = view.findViewById(R.id.cancel_card);
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("toolbar", "true");
        switch (v.getId()) {
            case R.id.backArrow:
                startActivity(new Intent(getActivity(), MainActivity.class));
                break;
            case R.id.rides_card:
                fragment = new PastTrips();
                fragment.setArguments(bundle);
                transaction.add(R.id.content, fragment);
                transaction.hide(this);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.schedule_card:
                fragment = new OnGoingTrips();
                transaction.add(R.id.content, fragment);
                fragment.setArguments(bundle);
                transaction.hide(this);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.revenue_card:
                startActivity(new Intent(getActivity(), EarningActivity.class));
//                fragment = new EarningsFragment();
//                transaction.add(R.id.content, fragment);
//                transaction.hide(this);
//                transaction.addToBackStack(null);
//                transaction.commit();
                break;
            case R.id.cancel_card:
                fragment = new PastTrips();
                transaction.add(R.id.content, fragment);
                fragment.setArguments(bundle);
                transaction.hide(this);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    private void setDetails() {
        Animation txtAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.txt_size);
        if (schedule > 0) {
            scheduleTxt.setAnimationDuration(500)
                    .countAnimation(0, schedule);
        } else {
            scheduleTxt.setText("0");
        }
        if (revenue > 0) {
            revenueTxt.setAnimationDuration(500)
                    .countAnimation(0, revenue);
        } else {
            revenueTxt.setText("0 AED");
        }
        if (rides > 0) {
            noOfRideTxt.setAnimationDuration(500)
                    .countAnimation(0, rides);

        } else {
            noOfRideTxt.setText("0");
        }
        if (cancel > 0) {
            cancelTxt.setAnimationDuration(500)
                    .countAnimation(0, cancel);
        } else {
            cancelTxt.setText("0");
        }
        scheduleTxt.startAnimation(txtAnim);
        revenueTxt.startAnimation(txtAnim);
        noOfRideTxt.startAnimation(txtAnim);
        cancelTxt.startAnimation(txtAnim);
        //  currencyTxt.setText(SharedHelper.getKey(getContext(), "currency"));
    }

    public void getProviderSummary() {
        {
            final CustomDialog customDialog = new CustomDialog(getActivity());
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.SUMMARY, object, response -> {
                customDialog.dismiss();
                cardLayout.setVisibility(View.VISIBLE);
                Animation slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
                cardLayout.startAnimation(slideUp);
                rides = Integer.parseInt(response.optString("rides"));
                schedule = Integer.parseInt(response.optString("scheduled_rides"));
                cancel = Integer.parseInt(response.optString("cancel_rides"));
                doubleRevenue = Double.parseDouble(response.optString("revenue"));
                revenue = doubleRevenue.intValue();
                //revenue = Integer.parseInt(response.optString("revenue"));

                slideUp.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        setDetails();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }, error -> {
                customDialog.dismiss();
                displayMessage(getString(R.string.something_went_wrong));
            }) {
                @Override
                public java.util.Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + SharedHelper.getKey(getContext(), "access_token"));
                    Log.e("", "Access_Token" + SharedHelper.getKey(getContext(), "access_token"));
                    return headers;
                }
            };

            ClassLuxApp.getInstance().addToRequestQueue(jsonObjectRequest);
        }
    }

    public void displayMessage(String toastString) {
        Toasty.info(getActivity(), toastString, Toast.LENGTH_SHORT, true).show();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(getContext(), "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(getContext(), SplashScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        getActivity().finish();
    }

}
