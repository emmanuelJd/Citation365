package com.citation.emmanuel.citation365.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.citation.emmanuel.citation365.ParametreApp;
import com.citation.emmanuel.citation365.tools.RequeteCitationDuJourNotification;


/**
 * Created by emmanuel on 26/01/2016.
 */
public class CitationService extends Service {

        /** ID de la notification de la citationDuJour */
        public static int ID_NOTIF_CITATION = 1;

        /** l'objet pour effectuer la requete de citation */
        public static RequeteCitationDuJourNotification requete = null;

        /** indicates whether onRebind should be used */
        boolean mAllowRebind;

        /** Called when the service is being created. */
        @Override
        public void onCreate() {
            CitationService.requete = new RequeteCitationDuJourNotification();
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {

            //Log.e("SERVICE"," Service est en lancement !");
            CitationService.requete.getCitationDuJour(this, getValueVibreur(), getValueLED());
            return START_STICKY;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            //Toast.makeText(this, "Notification désactivée !", Toast.LENGTH_SHORT).show();
        }



        /** Called when all clients have unbound with unbindService() */
        @Override
        public boolean onUnbind(Intent intent) {
            return mAllowRebind;
        }

        /** Called when a client is binding to the service with bindService()*/
        @Override
        public void onRebind(Intent intent) {

        }

        /**  Getter de la variable de l'état de l'option vibreur sur la notification */
        public int getValueVibreur() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            return prefs.getInt(ParametreApp.SHAREVIBREURNAME, -1);
        }

        /**  Getter de la variable de l'état de l'option lumière sur la notification */
        public int getValueLED() {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            return prefs.getInt(ParametreApp.SHARELEDNAME, -1);
        }

}
