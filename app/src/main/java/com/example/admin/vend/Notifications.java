package com.example.admin.vend;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.view.View;

public class Notifications {

    public static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_1_ID = "1";

    private Context context;
    public Notifications(Context context) {
        this.context = context;
    }

    public void sendNotification(){

        Intent goingIntent = new Intent(context, Recommendbackend.class);
        goingIntent.setAction("Going");
        goingIntent.putExtra("going", true);
        PendingIntent goingPendingIntent =
                PendingIntent.getBroadcast(context,0, goingIntent, 0);
        Intent notgoingIntent = new Intent(context, Recommendbackend.class);
        goingIntent.setAction("Not Going");
        goingIntent.putExtra("going", false);
        PendingIntent notgoingPendingIntent =
                PendingIntent.getBroadcast(context,0, notgoingIntent, 0);



        Intent intent = new Intent(context, Recommended.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_1_ID);
        builder.setSmallIcon(R.drawable.ic_t_pose);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_t_pose));
        builder.setContentTitle("Vendor Recommendation");
        builder.setContentText("Vendor y is x miles from you");
        builder.setSubText("Tap to view vendor details.");
        builder.addAction(R.drawable.ic_t_pose, "Going", goingPendingIntent);
        builder.addAction(R.drawable.ic_t_pose, "Not Going", notgoingPendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
