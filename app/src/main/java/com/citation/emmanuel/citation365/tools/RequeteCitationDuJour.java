package com.citation.emmanuel.citation365.tools;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.citation.emmanuel.citation365.fragments.Accueil;
import com.citation.emmanuel.citation365.fragments.Favoris;
import com.citation.emmanuel.citation365.models.CitationItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by emmanuel on 06/01/2016.
 */
public class RequeteCitationDuJour {


    /***********************************************************************************************
     *                                                                                             *
     *                                         Requete pour la citation du jour                    *
     *                                                                                             *
     **********************************************************************************************/
    /**
     * Fonction pour la récupération des citations
     *
     * @param numero_page int le numéro de la page à récupérer
     */
    public void getCitationDuJour(int numero_page, View loading_view, View refresh_view, View citation_view) {

        CitationDuJour citationDuJour = new CitationDuJour();
        if (numero_page <= 1) {
            citationDuJour.url = "http://evene.lefigaro.fr/citations/citation-jour.php";
        } else {
            citationDuJour.url = "http://evene.lefigaro.fr/citations/citation-jour.php?page=" + numero_page;
        }

        citationDuJour.loading_view = loading_view;
        citationDuJour.refresh_view = refresh_view;
        citationDuJour.citation_view = citation_view;

        citationDuJour.execute();
    }

    /**
     * AsyncTask de la récupération de données de type citation du jour
     */
    private class CitationDuJour extends AsyncTask<Void, Void, Void> {

        /**
         * url de l'adresse web afin de récupérer les données
         */
        public String url = "http://evene.lefigaro.fr/citations/citation-jour.php";

        /**
         * réussi ou pas
         */
        public boolean success = false;

        /**
         * la vue qui sert à prévenir d'un chargement de données
         */
        public View loading_view = null;

        /**
         * la vue qui sert à refresh la page
         */
        public View refresh_view = null;

        /**
         * la vue qui sert à prévenir d'un chargement de données
         */
        public View citation_view = null;

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
            this.success = false;
            this.listCitationItem = new ArrayList<CitationItem>();
            if (this.loading_view != null) {
                this.loading_view.setVisibility(View.VISIBLE);
            }
            if (this.refresh_view != null) {
                this.refresh_view.setVisibility(View.GONE);
            }
        }


        /**
         * fonction qui récupère le flux de réponse à la requête et parse les données
         */
        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document document = Jsoup.connect(this.url).get();
                Log.e("CITATION JOUR", this.url);
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
            this.listCitationItem = SynchronisationFavoris.synchronisationElement(this.listCitationItem, Favoris.adapter);
            Accueil.citationListAdapter.addAll(this.listCitationItem);
            Accueil.citationListAdapter.notifyDataSetChanged();
            if (this.loading_view != null) {
                this.loading_view.setVisibility(View.GONE);
            }
            if (this.refresh_view != null) {
                this.refresh_view.setVisibility(View.GONE);
            }
            if (this.citation_view != null) {
                this.citation_view.setVisibility(View.VISIBLE);
            }
            if (!this.success) {
                Accueil.backPage();
                Accueil.resetPrelast();

                if (this.loading_view != null) {
                    this.loading_view.setVisibility(View.GONE);
                }
                if (this.refresh_view != null) {
                    this.refresh_view.setVisibility(View.VISIBLE);
                }
                if (this.citation_view != null) {
                    this.citation_view.setVisibility(View.GONE);
                }
            }
        }
    }
}