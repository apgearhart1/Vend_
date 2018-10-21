package com.example.admin.vend;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Recommendbackend extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        System.out.println("HERERERE " + intent.getBooleanExtra("going", false));
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        Log.d(TAG, log);
        Toast.makeText(context, log, Toast.LENGTH_LONG).show();

        if(intent.getBooleanExtra("going", false)) {
            Sekretz.train(Notifications.lat, Notifications.lng, Notifications.category, 1);
        } else {
            Sekretz.train(Notifications.lat, Notifications.lng, Notifications.category, 0);
        }

    }
}
