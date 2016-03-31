package com.citation.emmanuel.citation365.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.citation.emmanuel.citation365.ListeAuteur;
import com.citation.emmanuel.citation365.ListeAuteurIndex;
import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.adapters.IndexAdapterListeAuteur;
import com.citation.emmanuel.citation365.fragments.ListeAuteurFragment;
import com.citation.emmanuel.citation365.models.ItemAuteur;
import com.citation.emmanuel.citation365.models.ItemListAuteur;
import com.citation.emmanuel.citation365.stockage.OpenHelper;
import com.citation.emmanuel.citation365.stockage.Provider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmanuel on 19/01/2016.
 */
public class RequeteListeAuteur {

    /***********************************************************************************************
     *                                                                                             *
     *                                         Requete pour avoir l'index des auteurs              *
     *                                                                                             *
     **********************************************************************************************/

    /** Fonction pour la récupération de la liste des auteurs
     *
     *
     * */
    public void getListeAuteur(Context context, FrameLayout loading_frame, FrameLayout connection_frame, GridView gridView, FragmentManager fm ){

        ListeAuteurURL liste_auteur = new ListeAuteurURL();
        liste_auteur.setUrl("http://evene.lefigaro.fr/citations/dictionnaire-citations-auteurs.php");

        liste_auteur.setLoading_layout(loading_frame);
        liste_auteur.setConnection_layout(connection_frame);
        liste_auteur.setGridView(gridView);
        liste_auteur.setContext(context);
        liste_auteur.setFm(fm);

        liste_auteur.execute();
    }

    /** AsyncTask de la récupération de données de type auteur */
    private class ListeAuteurURL extends AsyncTask<Void, Void, Void> {

        private String url = "";
        private Boolean success = false;

        private FrameLayout loading_layout = null;
        private FrameLayout connection_layout = null;
        private GridView gridView = null;
        private Context context = null;
        private FragmentManager fm = null;

        private ListeAuteur liste_auteur = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.success = false;

            if(this.loading_layout != null) {
                this.loading_layout.setVisibility(View.VISIBLE);
            }

            if(this.connection_layout != null) {
                this.connection_layout.setVisibility(View.GONE);
            }
            this.liste_auteur = new ListeAuteur();

            if(ListeAuteurFragment.liste_auteur_tableau != null){
                ListeAuteurFragment.liste_auteur_tableau = new ArrayList<ItemListAuteur>();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {


                Document document = Jsoup.connect(this.url).get();

                //Log.e(" LISTE AUTEUR INFO"," Initiale : "+ListeAuteurFragment.liste_auteur_tableau.size());
                for(Element div : document.select("article.figsco__box div.figsco__culture__article__body div.bloc_content")){

                    Elements lettre = div.select("h3");

                    ItemListAuteur itemListAuteur = new ItemListAuteur();
                    itemListAuteur.setIndex(lettre.get(0).html());
                    //Log.e(" LISTE AUTEUR INFO", "INDEX ------------------------------ : " + lettre.get(0).html());
                    for(Element a : div.select("p a[class]")) {

                        String nom_auteur = a.text();
                        String url_auteur = a.attr("href");

                        itemListAuteur.addItemListAuteur(nom_auteur, url_auteur, null, null);
                        //Log.e(" LISTE AUTEUR INFO", "AUTEUR ------------------------------ : " + nom_auteur);
                    }

                    if(lettre.size() > 0 && lettre.size() > 0) {
                        ListeAuteurFragment.liste_auteur_tableau.add(itemListAuteur);
                    }
                }

                //Log.e(" LISTE AUTEUR INFO"," Fin : "+ListeAuteurFragment.liste_auteur_tableau.size());
                this.success = true;
            } catch (Exception e) {
                this.success = false;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(!this.success){
                this.connection_layout.setVisibility(View.VISIBLE);
            }else {

                for(ItemListAuteur itemListAuteur : ListeAuteurFragment.liste_auteur_tableau) {
                    ajoutInformationAuteur(itemListAuteur.getListAuteur(), getContext());
                }

                ListeAuteurFragment.indexAdapterListeAuteur = new IndexAdapterListeAuteur(context, R.layout.item_gridview_index, ListeAuteurFragment.liste_auteur_tableau, getFm());
                this.gridView.setAdapter(ListeAuteurFragment.indexAdapterListeAuteur);
                this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Log.e("LISTE AUTEUR "," Click");


                        ItemListAuteur itemListAuteur = (ItemListAuteur) view.getTag();
                        Intent intent = new Intent(context, ListeAuteurIndex.class);
                        intent.putParcelableArrayListExtra("LISTE_AUTEUR",(ArrayList)itemListAuteur.getListAuteur());
                        intent.putExtra("LIST_AUTEUR_TITLE", "Auteur en  \"" + itemListAuteur.getIndex()+"\"");
                        context.startActivity(intent);
                        ((Activity)context).overridePendingTransition(R.anim.slide_enter_2, R.anim.slide_leave_2);

                    }
                });
            }

            this.loading_layout.setVisibility(View.GONE);

        }

        /** getter de l'url de localisation des données sur l'auteur */
        public String getUrl() {
            return this.url;
        }

        /** setter de l'url de localisation des données sur l'auteur */
        public void setUrl(String url) {
            this.url = url;
        }

        public FrameLayout getLoading_layout() {
            return loading_layout;
        }

        public void setLoading_layout(FrameLayout loading_layout) {
            this.loading_layout = loading_layout;
        }

        public FrameLayout getConnection_layout() {
            return connection_layout;
        }

        public void setConnection_layout(FrameLayout connection_layout) {
            this.connection_layout = connection_layout;
        }

        public GridView getGridView() {
            return gridView;
        }

        public void setGridView(GridView gridView) {
            this.gridView = gridView;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public FragmentManager getFm() {
            return fm;
        }

        public void setFm(FragmentManager fm) {
            this.fm = fm;
        }
    }


    public void ajoutInformationAuteur(List<ItemAuteur> arrayAuteur, Context context){

        for(int i = 0 ;i <arrayAuteur.size();i++){

            Cursor cursor = context.getApplicationContext().getContentResolver().query(Provider.CONTENT_URI_AUTEUR, null, OpenHelper.COLUMN_ID_AUTEUR_LINK+" = ?", new String[]{arrayAuteur.get(i).getUrl_auteur()}, null);
            if(cursor != null){
                try {
                    while (cursor.moveToNext()) {
                        arrayAuteur.get(i).setFonction_auteur(cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTEUR_FONCTION)));
                        arrayAuteur.get(i).setImage_url(cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_IMAGE_LINK)));
                    }
                } finally {
                    cursor.close();
                }
            }
        }
    }
}
