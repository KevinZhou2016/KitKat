package com.kitkat.savingsmanagement.manager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.kitkat.savingsmanagement.R;

import com.kitkat.savingsmanagement.activities.DashboardActivity;
import com.kitkat.savingsmanagement.utils.Constants;

/**
 * Created by Administrator on 2017/5/30 0030.
 */

public class NotificationsManager {

    public static void sendNotification(Context context, String title, String message) {
        // Instantiate a Builder object.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_menu_send)
                .setDefaults(Notification.DEFAULT_ALL);

        // Creates an Intent for the Activity
        Intent notifyIntent =
                new Intent(context, DashboardActivity.class);
        // Sets the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Creates the PendingIntent
        PendingIntent notifyPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        notifyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        // Puts the PendingIntent into the notification builder
        builder.setContentIntent(notifyPendingIntent);
        // Notifications are issued by sending them to the
        // NotificationsManager system service.
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Builds an anonymous Notification object from the builder, and
        // passes it to the NotificationsManager
        notificationManager.notify(0, builder.build());

        Log.d(Constants.LOG_TAG, "Notification sent to status bar: \n" + title + "\n" + message);

    }
}
