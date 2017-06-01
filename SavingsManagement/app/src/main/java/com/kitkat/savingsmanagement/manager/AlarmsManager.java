package com.kitkat.savingsmanagement.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kitkat.savingsmanagement.receivers.SavingsManagerReceivers;
import com.kitkat.savingsmanagement.utils.Constants;
import com.kitkat.savingsmanagement.utils.Utils;

import java.util.Date;

/**
 * Created by Administrator on 2017/5/30 0030.
 */

public class AlarmsManager {
    public static void scheduleAlarm(Context context, Date date) {

        if (date == null) {
            return;
        }

        // Get the alarm manager from system service
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // pending intent to start when alarm reaches
        Intent intent = new Intent(context, SavingsManagerReceivers.class);
        intent.setAction(Constants.ACTION_DUE_SAVINGS_ITEM_ALARM);
        intent.putExtra(Constants.INTENT_EXTRA_ALARM_TIME, date.getTime());
        PendingIntent alarm =
                PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // schedule the alarm
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), alarm);

        Log.d(Constants.LOG_TAG, "Scheduled an alarm at " +
                Utils.formatDate(date, Constants.FORMAT_DATE_YEAR_MONTH_DAY));

    }
}
