package com.citation.emmanuel.citation365.tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.citation.emmanuel.citation365.MainActivity;
import com.citation.emmanuel.citation365.ParametreApp;
import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.models.CitationItem;
import com.citation.emmanuel.citation365.services.CitationService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by emmanuel on 06/01/2016.
 */
public class RequeteCitationDuJourNotification {


    /***********************************************************************************************
     *                                                                                             *
     *                                         Requete pour la citation du jour                    *
     *                                                                                             *
     **********************************************************************************************/
    /**
     * Fonction pour la récupération des citations
     *
     */
    public void getCitationDuJour(Context context, int valueVibreur, int valueLED) {

        CitationDuJourNotification citationDuJourNotification = new CitationDuJourNotification();
        citationDuJourNotification.url = "http://evene.lefigaro.fr/citations/citation-jour.php";
        citationDuJourNotification.context = context;
        citationDuJourNotification.valueVibreur = valueVibreur;
        citationDuJourNotification.valueLED = valueLED;
        citationDuJourNotification.execute();
    }

    /**
     * AsyncTask de la récupération de données de type citation du jour
     */
    private class CitationDuJourNotification extends AsyncTask<Void, Void, Void> {

        /**
         * url de l'adresse web afin de récupérer les données
         */
        public String url = "http://evene.lefigaro.fr/citations/citation-jour.php";

        /**
         * réussi ou pas
         */
        public boolean success = false;

        public Context context = null;

        public int valueVibreur = 0;
        public int valueLED = 0;

        /**
         * liste à remplir après le parsage des données
         */
        public ArrayList<CitationItem> listCitationItem = null;

        /**
         * fonction avant la réalisation de la requête
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.listCitationItem = new ArrayList<CitationItem>();
            this.success = false;
        }


        /**
         * fonction qui récupère le flux de réponse à la requête et parse les données
         */
        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document document = Jsoup.connect(this.url).get();
                //Log.e("CITATION JOUR", this.url);
                for (Element li : document.select("ul[class=figsco__selection__list__evene__list] li[class=figsco__selection__list__evene__list__item]")) {

                    Elements citation = li.select("a[href]");

                    Elements auteur = li.select("div[class=figsco__quote__from figsco__row] div[class=figsco__fake__col-9] > a[href]");

                    if (citation.size() > 0 && auteur.size() > 0) {
                        this.listCitationItem.add(new CitationItem(citation.get(0).html(), auteur.get(0).html(), citation.get(0).attr("href"), auteur.get(0).attr("href"), 0));
                    }
                }
                this.success = true;
            } catch (IOException e) {
                this.success = false;
                e.printStackTrace();
            }
            return null;
        }

        /**
         * a la fin de la requête lance la fonction suivante
         */
        @Override
        protected void onPostExecute(Void result) {

            //Log.d("citation", " " + listCitationItem.size());

            String tampon_id_citation = getValueIdCitation(this.context);

            if (this.success && this.listCitationItem.size() > 0) {
                if(!tampon_id_citation.equals(this.listCitationItem.get(0).getId_citation_link())) {

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this.context)
                                    .setSmallIcon(R.mipmap.ic_launcher)
                                    .setContentTitle(this.listCitationItem.get(0).getAuteur())
                                    .setContentText(this.listCitationItem.get(0).getCitation());

                    if (valueVibreur == 1) {
                        mBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                    }

                    if (valueLED == 1) {
                        mBuilder.setLights(Color.BLUE, 3000, 3000);
                    }

                    // Creates an explicit intent for an Activity in your app
                    Intent resultIntent = new Intent(this.context, MainActivity.class);

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(this.context);

                    // Adds the Intent that starts the Activity to the top of the stack
                    stackBuilder.addNextIntent(resultIntent);

                    PendingIntent resultPendingIntent =
                            stackBuilder.getPendingIntent(
                                    0,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );

                    mBuilder.setContentIntent(resultPendingIntent);
                    NotificationManager mNotificationManager =
                            (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);

                    // mId allows you to update the notification later on.
                    mNotificationManager.notify(CitationService.ID_NOTIF_CITATION, mBuilder.build());

                    setValueIdCitation(this.context, this.listCitationItem.get(0).getId_citation_link());
                }
            }else{
                //Log.d("SERVICE", " " + listCitationItem.size());
            }
        }
    }

    /**  Getter et setter de la variable de l'id citation de l'option notification */
    public String getValueIdCitation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(ParametreApp.SHAREIDCITATION, "");
    }

    public void setValueIdCitation(Context context, String newValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(ParametreApp.SHAREIDCITATION, newValue);
        editor.commit();
    }
}