package com.citation.emmanuel.citation365.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.citation.emmanuel.citation365.ParametreApp;

/**
 * Created by emmanuel on 26/01/2016.
 */
public class VerificationCitation extends BroadcastReceiver {


    public void onReceive(Context arg0, Intent arg1)
    {
        Intent intent = new Intent(arg0,CitationService.class);

        int value_notif = getValue(arg0);

        if(value_notif == 1) {
            //Log.d("SERVICE", "RUN BEFORE");
            arg0.startService(intent);
        }
    }

    public int getValue(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(ParametreApp.SHARENAME, -1);
    }

}
