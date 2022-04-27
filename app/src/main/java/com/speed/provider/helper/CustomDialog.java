package com.speed.provider.helper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.speed.provider.R;


public class CustomDialog extends ProgressDialog {

    public CustomDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setIndeterminate(true);

        setMessage(context.getString(R.string.loading));
    }
}
