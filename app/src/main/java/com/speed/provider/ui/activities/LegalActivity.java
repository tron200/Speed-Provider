package com.speed.provider.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.speed.provider.R;
import com.speed.provider.helper.SharedHelper;

public class LegalActivity extends AppCompatActivity {

    private ImageView backArrow;
    private TextView
            termsConditionTextView,
            privacyPolicyTextView,
            copyrightTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        setContentView(R.layout.activity_legal);
        backArrow = findViewById(R.id.backArrow);
        termsConditionTextView = findViewById(R.id.termsConditionTextView);
        privacyPolicyTextView = findViewById(R.id.privacyPolicyTextView);
        copyrightTextView = findViewById(R.id.copyrightTextView);

        backArrow.setOnClickListener(view -> onBackPressed());

        privacyPolicyTextView.setOnClickListener(v -> startActivity(new
                Intent(LegalActivity.this, PrivacyPolicyActivity.class)));

        termsConditionTextView.setOnClickListener(v -> startActivity(new
                Intent(LegalActivity.this, TermsOfUseActivity.class)));

        copyrightTextView.setOnClickListener(v -> Toast.makeText(
                LegalActivity.this, "Coming Soon...", Toast.LENGTH_SHORT).show());


    }
}
