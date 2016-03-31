package com.citation.emmanuel.citation365.tools;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.citation.emmanuel.citation365.fragments.Favoris;
import com.citation.emmanuel.citation365.fragments.Recherche;
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
public class RequeteRechercheCitation {


    /***********************************************************************************************
     *                                                                                             *
     *                                         Requete pour la recherche d'un citation             *
     *                                                                                             *
     **********************************************************************************************/
    /** Fonction pour la récupération des citations
     *
     * @param numero_page int le numéro de la page à récupérer
     * */
    public void getCitationParRecherche(int numero_page, String mot, View loading_view, View refresh_view, View citation_view, Context context){

        if(mot != null) {
            CitationParRecherche citationParRecherche = new CitationParRecherche();
            if (numero_page <= 1) {
                citationParRecherche.url = "http://evene.lefigaro.fr/citations/mot.php?mot=" + mot;
            } else {
                citationParRecherche.url = "http://evene.lefigaro.fr/citations/mot.php?mot=" + mot + "&p=" + numero_page;
            }

            citationParRecherche.loading_view = loading_view;
            citationParRecherche.refresh_view = refresh_view;
            citationParRecherche.citation_view = citation_view;

            citationParRecherche.execute();
        }else{
            Toast.makeText(context, "Vous devez rechercher à l'aide d'un mot-clef !", Toast.LENGTH_LONG).show();
        }
    }

    /** AsyncTask de la récupération de données de type citation du jour */
    private class CitationParRecherche extends AsyncTask<Void, Void, Void> {

        /** url de l'adresse web afin de récupérer les données */
        public String url = "http://evene.lefigaro.fr/citations/mot.php?mot=";

        /** réussi ou pas*/
        public boolean success = false;

        /** la vue qui sert à prévenir d'un chargement de données */
        public View loading_view = null;

        /** la vue qui sert à refresh la page */
        public View refresh_view = null;

        /** la vue qui sert à prévenir d'un chargement de données */
        public View citation_view = null;

        /** liste à remplir après le parsage des données */
        public ArrayList<CitationItem> listCitationItem  = null;

        /** fonction avant la réalisation de la requête */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.success = false;
            this.listCitationItem =  new  ArrayList<CitationItem>();
            if(this.loading_view != null){
                this.loading_view.setVisibility(View.VISIBLE);
            }
            if(this.refresh_view != null){
                this.refresh_view.setVisibility(View.GONE);
            }
        }


        /** fonction qui récupère le flux de réponse à la requête et parse les données */
        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document document = Jsoup.connect(this.url).get();
                //Log.e("CITATION RECHERCHE",this.url);

                for(Element li : document.select("li[class=figsco__selection__list__evene__list__item] article")){

                    Elements citation = li.select("div[class=figsco__quote__text] a[href]");

                    Elements auteur = li.select("div[class=figsco__quote__from figsco__row] div[class=figsco__fake__col-9] > a[href]");
                    if(citation.size() > 0 && auteur.size() > 0) {
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

        /** a la fin de la requête lance la fonction suivante */
        @Override
        protected void onPostExecute(Void result) {
            //Log.d("citation", " " + listCitationItem.size());
            this.listCitationItem = SynchronisationFavoris.synchronisationElement(this.listCitationItem, Favoris.adapter);
            Recherche.citationListAdapterRecherche.addAll(this.listCitationItem);
            Recherche.citationListAdapterRecherche.notifyDataSetChanged();
            if(this.loading_view != null){
                this.loading_view.setVisibility(View.GONE);
            }
            if(this.refresh_view != null){
                this.refresh_view.setVisibility(View.GONE);
            }
            if(this.citation_view != null){
                this.citation_view.setVisibility(View.VISIBLE);
            }
            if(!this.success){
                Recherche.backPage();
                Recherche.resetPrelast();

                if(this.loading_view != null){
                    this.loading_view.setVisibility(View.GONE);
                }
                if(this.refresh_view != null){
                    this.refresh_view.setVisibility(View.VISIBLE);
                }
                if(this.citation_view != null){
                    this.citation_view.setVisibility(View.GONE);
                }
            }
        }
    }
}
