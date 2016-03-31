package com.citation.emmanuel.citation365.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.models.ItemAuteur;
import com.citation.emmanuel.citation365.stockage.OpenHelper;
import com.citation.emmanuel.citation365.stockage.Provider;

import java.io.InputStream;
import java.util.List;

/**
 * Created by emmanuel on 05/10/2015.
 *
 * l'adapter pour la liste des citations
 */
public class ListeAuteurAdapter extends ArrayAdapter<ItemAuteur>{

    private Context context;
    private int resLayout;
    private List<ItemAuteur> listCitationItems;
    private ListView lvCitation;


    /** constructeur de l'adapter  */
    public ListeAuteurAdapter(Context context, int resLayout, List<ItemAuteur> listCitationItems) {
        super(context, resLayout, listCitationItems);

        this.context = context;
        this.resLayout = resLayout;
        this.listCitationItems = listCitationItems;
    }

    /** constructeur de l'adapter  */
    public ListeAuteurAdapter(Context context, int resLayout, List<ItemAuteur> listCitationItems, ListView lvCitation) {
        super(context, resLayout, listCitationItems);

        this.context = context;
        this.resLayout = resLayout;
        this.listCitationItems = listCitationItems;
        this.lvCitation = lvCitation;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = null;

        v = layoutInflater.inflate(R.layout.item_liste_auteur, null);

        TextView tvNomAuteur = (TextView) v.findViewById(R.id.nom_auteur_item);
        TextView tvFonctionAuteur = (TextView) v.findViewById(R.id.fonction_auteur_item);

        ItemAuteur itemAuteur = this.listCitationItems.get(position);

        Cursor cursor = context.getApplicationContext().getContentResolver().query(Provider.CONTENT_URI_AUTEUR, null, OpenHelper.COLUMN_ID_AUTEUR_LINK + " = ?", new String[]{itemAuteur.getUrl_auteur()}, null);
        if(cursor != null){
            try {
                while (cursor.moveToNext()) {
                    itemAuteur.setFonction_auteur(cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTEUR_FONCTION)));
                    itemAuteur.setImage_url(cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_IMAGE_LINK)));
                }
            } finally {
                cursor.close();
            }
        }


        tvNomAuteur.setText(itemAuteur.getNom_auteur());
        if(itemAuteur.getFonction_auteur() != null && !itemAuteur.getFonction_auteur().isEmpty()) {
            tvFonctionAuteur.setText(itemAuteur.getFonction_auteur());
        }

        if(itemAuteur.getImage_url() != null && !itemAuteur.getImage_url().isEmpty()) {
            new DownloadImageTask((ImageView) v.findViewById(R.id.image_auteur_item))
                    .execute(itemAuteur.getImage_url());
        }

        v.setTag(itemAuteur.getUrl_auteur());

       // Log.e("IMAGE AUTEUR", itemAuteur.getUrl_auteur()+ " && " + itemAuteur.getNom_auteur()+" && "+itemAuteur.getFonction_auteur()+" && "+itemAuteur.getImage_url());
        return v;
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
}


