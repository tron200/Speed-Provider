package com.speed.provider.fcm;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.speed.provider.ui.activities.MainActivity;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent launch_intent = new Intent(context, MainActivity.class);
        launch_intent.setComponent(new ComponentName("com.classluxdrive.providers",
                "com.classluxdrive.providers.activities.MainActivity"));
        launch_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        launch_intent.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(launch_intent);
    }


}
