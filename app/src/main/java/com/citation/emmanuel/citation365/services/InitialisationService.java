package com.citation.emmanuel.citation365.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.citation.emmanuel.citation365.ParametreApp;

import java.util.Calendar;

/**
 * Created by emmanuel on 26/01/2016.
 */
public class InitialisationService extends BroadcastReceiver {

    public void onReceive(Context arg0, Intent arg1)
    {
        int value_notif = getValue(arg0);

        if(value_notif == 1) {

            if(ParametreApp.alarmManager_notification == null){
                ParametreApp.alarmManager_notification = (AlarmManager) arg0.getSystemService(Context.ALARM_SERVICE);
            }

            if(ParametreApp.intent_notification == null){
                ParametreApp.intent_notification = new Intent(arg0, VerificationCitation.class);
            }

            if(ParametreApp.pendingIntent_notification == null && ParametreApp.intent_notification != null){
                ParametreApp.pendingIntent_notification = PendingIntent.getBroadcast(arg0, 0, ParametreApp.intent_notification, PendingIntent.FLAG_CANCEL_CURRENT);
            }

            Calendar cal = Calendar.getInstance();
            // start 30 seconds after boot completed
            cal.add(Calendar.SECOND, 30);
            // fetch every 30 seconds
            // InexactRepeating allows Android to optimize the energy consumption
            ParametreApp.alarmManager_notification.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                    cal.getTimeInMillis(), ParametreApp.REPEAT_TIME, ParametreApp.pendingIntent_notification);
        }
    }

    public int getValue(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(ParametreApp.SHARENAME, -1);
    }

}
