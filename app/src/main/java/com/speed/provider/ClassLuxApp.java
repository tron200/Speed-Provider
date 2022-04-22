package com.speed.provider;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.speed.provider.helper.SharedHelper;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import java.util.Locale;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;



public class ClassLuxApp extends Application {

    public static final String TAG = ClassLuxApp.class
            .getSimpleName();
    private static ClassLuxApp mInstance;
    private static Context context;
    private RequestQueue mRequestQueue;

    public static Context getContext() {
        return context;
    }

    public static synchronized ClassLuxApp getInstance() {
        return mInstance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initCalligraphyConfig();
        EmojiManager.install(new IosEmojiProvider());

        context = getApplicationContext();


        setLocale(SharedHelper.getKey(ClassLuxApp.this, "selectedlanguage"));

    }

    private void initCalligraphyConfig() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getResources().getString(R.string.bariol))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        VolleyLog.DEBUG = true;
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        VolleyLog.DEBUG = true;
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

}
