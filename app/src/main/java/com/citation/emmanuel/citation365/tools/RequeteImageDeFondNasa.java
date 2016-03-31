package com.citation.emmanuel.citation365.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.InputStream;

/**
 * Created by emmanuel on 06/01/2016.
 */
public class RequeteImageDeFondNasa {

    /***********************************************************************************************
     *                                                                                             *
     *                                         Requete pour l'image de fond                        *
     *                                                                                             *
     **********************************************************************************************/

    /** Fonction pour la récupération de l'image de fond
     *
     *²
     * */
    public void getImageNasaFondView( View viewImageFond, Context context){

        ImageNasa imageNasa = new ImageNasa();

        imageNasa.setView(viewImageFond);
        imageNasa.setContext(context);

        imageNasa.execute();
    }

    /** AsyncTask de la récupération de données de type auteur */
    private class ImageNasa extends AsyncTask<Void, Void, Void> {

        private final String url = "http://apod.nasa.gov/apod/astropix.html";
        private Boolean success = false;
        private View view = null;
        private Context context = null;
        private String url_image_de_fond = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.url_image_de_fond = "";
            this.success = false;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Document document = Jsoup.connect(this.url).get();

                if(document.select("body > center > p > a > img").size()> 0) {
                    Element image_de_fond_url = document.select("body > center > p > a > img").first();
                    this.url_image_de_fond = "http://apod.nasa.gov/apod/" + image_de_fond_url.attr("src");
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

            if(this.success && this.url_image_de_fond != ""){

                new DownloadImageTask(this.view, this.context)
                        .execute(this.url_image_de_fond);
            }
        }




        /** getter de la vue ou les données de l'auteur seront affichées */
        public View getView() {
            return this.view;
        }

        /** setter de la vue ou les données de l'auteur seront affichées */
        public void setView(View view) {
            this.view = view;
        }


        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }
    }


    /** Fonction pour le chargement d'une image */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        View bmImage;
        Context context;

        public DownloadImageTask(View bmImage,Context context) {
            this.bmImage = bmImage;
            this.context = context;
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
            /*bmImage.setImageBitmap(result);*/
            BitmapDrawable ob = new BitmapDrawable(this.context.getResources(), result);
            //bmImage.setBackground(ob);
            ((ImageView)bmImage).setImageDrawable(ob);

        }
    }
}
