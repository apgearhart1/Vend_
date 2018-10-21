package com.example.admin.vend;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Recommendbackend extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getBooleanExtra("going", false)) {
            Sekretz.train(Notifications.lat, Notifications.lng, Notifications.category, 1);
        } else {
            Sekretz.train(Notifications.lat, Notifications.lng, Notifications.category, 0);
        }
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(Notifications.NOTIFICATION_ID);
    }
}
