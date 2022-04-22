package com.speed.provider.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.speed.provider.R;
import com.speed.provider.helper.ConnectionHelper;
import com.speed.provider.helper.CustomDialog;
import com.speed.provider.helper.SharedHelper;
import com.speed.provider.models.User;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class ShowProfile extends AppCompatActivity {

    public Context context = ShowProfile.this;
    public Activity activity = ShowProfile.this;
    String TAG = "ShowActivity";
    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;
    ImageView backArrow;
    TextView email, first_name, last_name, mobile_no, services_provided;
    ImageView profile_Image;
    RatingBar ratingProvider;
    String strUserId = "", strServiceRequested = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
        setContentView(R.layout.activity_show_profile);
        findViewByIdandInitialization();

        backArrow.setOnClickListener(view -> finish());

    }

    public void findViewByIdandInitialization() {
        email = findViewById(R.id.email);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        mobile_no = findViewById(R.id.mobile_no);
        //services_provided = (TextView) findViewById(R.id.services_provided);
        backArrow = findViewById(R.id.backArrow);
        profile_Image = findViewById(R.id.img_profile);
        ratingProvider = findViewById(R.id.ratingProvider);
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();

        User user = getIntent().getParcelableExtra("user");
        if (user.getEmail() != null && !user.getEmail().equalsIgnoreCase("null") && user.getEmail().length() > 0)
            email.setText(user.getEmail());
        else
            email.setText("");
        if (user.getFirstName() != null && !user.getFirstName().equalsIgnoreCase("null") && user.getFirstName().length() > 0)
            first_name.setText(user.getFirstName());
        else
            first_name.setText("");
        if (user.getMobile() != null && !user.getMobile().equalsIgnoreCase("null") && user.getMobile().length() > 0)
            mobile_no.setText(user.getMobile());
        else
            mobile_no.setText(getString(R.string.user_no_mobile));
        if (user.getLastName() != null && !user.getLastName().equalsIgnoreCase("null") && user.getLastName().length() > 0)
            last_name.setText("");
        else
            last_name.setText("");
        if (user.getRating() != null && !user.getRating().equalsIgnoreCase("null") && user.getRating().length() > 0)
            ratingProvider.setRating(Float.parseFloat(user.getRating()));
        else
            ratingProvider.setRating(1);
        Picasso.get().load(user.getImg()).memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(profile_Image);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void displayMessage(String toastString) {
        Toast.makeText(context, toastString + "", Toast.LENGTH_SHORT).show();
    }


}
