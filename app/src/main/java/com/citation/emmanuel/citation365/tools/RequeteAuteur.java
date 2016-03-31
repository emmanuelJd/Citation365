package com.citation.emmanuel.citation365.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.adapters.MyFragmentPagerAdapter;
import com.citation.emmanuel.citation365.fragments.AuteurAutreCitation;
import com.citation.emmanuel.citation365.fragments.ListeAuteurIndexFragment;
import com.citation.emmanuel.citation365.stockage.OpenHelper;
import com.citation.emmanuel.citation365.stockage.Provider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.InputStream;
import java.util.List;

/**
 * Created by emmanuel on 06/01/2016.
 */
public class RequeteAuteur {

    /***********************************************************************************************
     *                                                                                             *
     *                                         Requete pour l'auteur                               *
     *                                                                                             *
     **********************************************************************************************/

    /** Fonction pour la récupération des informations sur l'auteurs
     *
     * @param url_auteur String l'url de l'auteur de la page à récupérer
     * */
    public void getAuteurInformation(String url_auteur, View view, FrameLayout loading_frame, FrameLayout connection_frame, MyFragmentPagerAdapter myFragmentPagerAdapter, List<Fragment> listFragment, ViewPager viewPager, Context context){

        AuteurURL auteur_info = new AuteurURL();
        auteur_info.setUrl(url_auteur);
        auteur_info.setView(view);
        auteur_info.setLoading_layout(loading_frame);
        auteur_info.setConnection_layout(connection_frame);
        auteur_info.setMyFragmentPagerAdapter(myFragmentPagerAdapter);
        auteur_info.setListFragments(listFragment);
        auteur_info.setViewPager(viewPager);
        auteur_info.setContext(context);
        auteur_info.execute();
    }

    /** AsyncTask de la récupération de données de type auteur */
    private class AuteurURL extends AsyncTask<Void, Void, Void> {

        private String url = "";
        private Boolean success = false;
        private View view = null;
        private Context context = null;
        private FrameLayout loading_layout = null;
        private FrameLayout connection_layout = null;
        private MyFragmentPagerAdapter myFragmentPagerAdapter = null;
        private List<Fragment> listFragments = null;
        private ViewPager viewPager = null;

        private com.citation.emmanuel.citation365.models.Auteur auteur_info = null;

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
            this.auteur_info = new com.citation.emmanuel.citation365.models.Auteur();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {


                Document document = Jsoup.connect(this.url).get();

                if(document.select("div[class=figsco__row] h1[itemprop=name]").size()> 0) {
                    Element nom_auteur_container = document.select("div[class=figsco__row] h1[itemprop=name]").first();
                    this.auteur_info.setNom_auteur(nom_auteur_container.text());
                }

                if(document.select("div[class=figsco__row] div[itemprop=jobTitle]").size()> 0) {
                    Element fonction_auteur_container = document.select("div[class=figsco__row] div[itemprop=jobTitle]").first();
                    this.auteur_info.setFonction_auteur(fonction_auteur_container.text());
                }

                if(document.select("p[class=figsco__artist__quote]").size()> 0) {
                    Element citation_initiale_container = document.select("p[class=figsco__artist__quote]").first();
                    this.auteur_info.setCitation_initiale(citation_initiale_container.text());
                }

                if(document.select("div[itemprop=description]").size()> 0) {
                    Element biographie_auteur_container = document.select("div[itemprop=description]").first();
                    this.auteur_info.setBiographie_auteur(biographie_auteur_container.text());
                }

                if(document.select("img[itemprop=image]").size()> 0) {
                    Element image_auteur_url = document.select("img[itemprop=image]").first();
                    this.auteur_info.setImage_url(image_auteur_url.attr("src"));
                }

                if(document.select("div.figsco__culture__card > div.figsco__box > a.figsco__quote__link").size()> 0) {
                    Element autre_citation_auteur_url = document.select("div.figsco__culture__card > div.figsco__box > a.figsco__quote__link").first();
                    this.auteur_info.setAutre_citation(autre_citation_auteur_url.attr("href"));
                }

                this.success = true;
            } catch (Exception e) {
                this.success = false;
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            TextView textView_nom_auteur = (TextView) this.view.findViewById(R.id.nom_auteur);
            TextView textView_fonction_auteur = (TextView) this.view.findViewById(R.id.fonction_auteur);
            TextView textView_biographie_auteur = (TextView) this.view.findViewById(R.id.biographie_auteur);
            TextView textView_citation_auteur = (TextView) this.view.findViewById(R.id.citation_celebre);

            FloatingActionButton buttonAutreCitation = (FloatingActionButton) this.view.findViewById(R.id.btn_autre_citation);

            this.loading_layout.setVisibility(View.GONE);

            if( this.auteur_info.getNom_auteur() != null ) {
                if( this.auteur_info.getNom_auteur() instanceof String ) {
                    textView_nom_auteur.setText(this.auteur_info.getNom_auteur());
                }
            }

            if( this.auteur_info.getFonction_auteur() != null ) {
                if( this.auteur_info.getFonction_auteur() instanceof String ) {
                    textView_fonction_auteur.setText(this.auteur_info.getFonction_auteur());
                }
            }

            if( this.auteur_info.getBiographie_auteur() != null ) {
                if( this.auteur_info.getBiographie_auteur() instanceof String ) {
                    textView_biographie_auteur.setText(this.auteur_info.getBiographie_auteur());
                }
            }

            if( this.auteur_info.getCitation_initiale() != null ) {
                if( this.auteur_info.getCitation_initiale() instanceof String ) {
                    textView_citation_auteur.setText(this.auteur_info.getCitation_initiale());
                }
            }

            if( this.auteur_info.getImage_url() != null ) {
                if( this.auteur_info.getImage_url() instanceof String ) {
                    new DownloadImageTask((ImageView) this.view.findViewById(R.id.image_auteur))
                            .execute(this.auteur_info.getImage_url());
                }
            }

            if( this.auteur_info.getAutre_citation() != null ) {
                if( this.auteur_info.getAutre_citation() instanceof String ) {
                    buttonAutreCitation.setTag(this.auteur_info.getAutre_citation());
                }
            }

            if(!this.success){
                this.connection_layout.setVisibility(View.VISIBLE);
            }else{
                if( this.getMyFragmentPagerAdapter() != null && this.getListFragments() != null) {
                    if(this.auteur_info.getAutre_citation() != null){
                        if(this.auteur_info.getAutre_citation() instanceof String) {
                            AuteurAutreCitation auteurAutreCitation = new AuteurAutreCitation();
                            auteurAutreCitation.setViewpager(this.viewPager);
                            auteurAutreCitation.setUrl_auteur_autre_citation(this.auteur_info.getAutre_citation());
                            this.listFragments.add(auteurAutreCitation);
                            this.myFragmentPagerAdapter.notifyDataSetChanged();
                        }
                    }
                }
                insertAuteurImage(this.url, this.auteur_info.getImage_url(), this.auteur_info.getFonction_auteur(), getContext());
                if(ListeAuteurIndexFragment.listeAuteurAdapter != null) {
                    ListeAuteurIndexFragment.listeAuteurAdapter.notifyDataSetChanged();
                }
            }
        }

        /** getter de l'url de localisation des données sur l'auteur */
        public String getUrl() {
            return this.url;
        }

        /** setter de l'url de localisation des données sur l'auteur */
        public void setUrl(String url) {
            this.url = url;
        }

        /** getter du context */
        public Context getContext() {
            return this.context;
        }

        /** setter du context */
        public void setContext(Context context) {
            this.context = context;
        }

        /** getter de la vue ou les données de l'auteur seront affichées */
        public View getView() {
            return this.view;
        }

        /** setter de la vue ou les données de l'auteur seront affichées */
        public void setView(View view) {
            this.view = view;
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

        public MyFragmentPagerAdapter getMyFragmentPagerAdapter() {
            return myFragmentPagerAdapter;
        }

        public void setMyFragmentPagerAdapter(MyFragmentPagerAdapter myFragmentPagerAdapter) {
            this.myFragmentPagerAdapter = myFragmentPagerAdapter;
        }

        public List<Fragment> getListFragments() {
            return listFragments;
        }

        public void setListFragments(List<Fragment> listFragments) {
            this.listFragments = listFragments;
        }

        public ViewPager getViewPager() {
            return viewPager;
        }

        public void setViewPager(ViewPager viewPager) {
            this.viewPager = viewPager;
        }
    }


    /** Fonction pour le chargement d'une image */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }


    /**
     *
     *
     */
    public static void insertAuteurImage(String id_auteur_link, String image_link, String fonction_auteur, Context context){

        ContentValues values = new ContentValues();
        values.clear();

        values.put(OpenHelper.COLUMN_ID_AUTEUR_LINK, id_auteur_link);
        values.put(OpenHelper.COLUMN_IMAGE_LINK, image_link);
        values.put(OpenHelper.COLUMN_AUTEUR_FONCTION, fonction_auteur);


        boolean alreadyExist = false;
        Cursor cursor = context.getApplicationContext().getContentResolver().query(Provider.CONTENT_URI_AUTEUR, null, OpenHelper.COLUMN_ID_AUTEUR_LINK+" = ?", new String[]{id_auteur_link}, null);
        //Log.e("FAVORIS"," Nombre de citation en favoris database: "+cursor.getCount());
        int id = -1;

        /** vérification de l'existance d'une citation dans la base de données pour éviter la redondance */
        //for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
          //  if(cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_ID_AUTEUR_LINK)).equals(id_auteur_link)){
                //id = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_ID));
                // Log.e("FAVORIS"," Ok ");
            ///    alreadyExist = true;
           // }
      //  }

        /**
         * vérification de la redondance, si la citation est ciblée pour la mise en favoris et qu'elle y est déjà donc on le supprime des favoris sinon
         *  on l'ajoute dans la table des favoris.
         * */
        if(cursor != null){
            try {
                context.getApplicationContext().getContentResolver().insert(Provider.CONTENT_URI_AUTEUR, values);
            }finally {
                cursor.close();
            }
            Log.e("AUTEUR INSERTION", "Les informations sur l'auteur ont été ajouté");
        }


        //Log.e("FAVORIS"," Nombre de citation en favoris : "+Favoris.adapter.getCount());
    }
}
