package com.speed.provider.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.speed.provider.R;
import com.speed.provider.helper.SharedHelper;
import com.squareup.picasso.Picasso;

public class FullImage extends AppCompatActivity {
    ImageView imgFull;
    ImageView imgback;
    TextView txtTitle;
    String title;
    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        imgback = findViewById(R.id.imgback);

        if (SharedHelper.getKey(this, "selectedlanguage").contains("ar")) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            imgback.setImageDrawable(getDrawable(R.drawable.ic_forward));

        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

        title = getIntent().getStringExtra("title");
        imgUrl = getIntent().getStringExtra("url");
        imgFull = findViewById(R.id.imgFull);
        txtTitle = findViewById(R.id.txtTitle);
        Picasso.get().load(imgUrl).into(imgFull);
        txtTitle.setText(title);

        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
