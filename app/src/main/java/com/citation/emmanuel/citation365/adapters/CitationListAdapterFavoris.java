package com.citation.emmanuel.citation365.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.fragments.Accueil;
import com.citation.emmanuel.citation365.fragments.Favoris;
import com.citation.emmanuel.citation365.fragments.Recherche;
import com.citation.emmanuel.citation365.models.CitationItem;
import com.citation.emmanuel.citation365.models.CitationViewObject;
import com.citation.emmanuel.citation365.stockage.OpenHelper;
import com.citation.emmanuel.citation365.stockage.Provider;
import com.citation.emmanuel.citation365.tools.SynchronisationFavoris;

import java.util.List;

/**
 * Created by emmanuel on 05/10/2015.
 */
public class CitationListAdapterFavoris extends ArrayAdapter<CitationItem> implements View.OnClickListener{

    private Context context;
    private List<CitationItem> listCitationItems;

    public CitationListAdapterFavoris(Context context, int resLayout, List<CitationItem> listCitationItems) {
        super(context, resLayout, listCitationItems);

        this.context = context;
        this.listCitationItems = listCitationItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = null;

        if (convertView == null) {
            v = layoutInflater.inflate(R.layout.item_citation_list, null);
        } else {
            v = convertView;
        }

        /** on récupère les éléments qui contiendront la citation et l'auteur*/
        TextView tvCitation = (TextView) v.findViewById(R.id.citation_text);
        TextView tvAuteur = (TextView) v.findViewById(R.id.citation_auteur);

        /** on récupère la variable contenant les informations */
        CitationItem citationItem = this.listCitationItems.get(position);

        /** on insert les informations dans les éléments*/
        tvCitation.setText(citationItem.getCitation());
        tvAuteur.setText(citationItem.getAuteur());


        /** on récupère les boutons pour appliquer l'image associé et ajouter les listeners */
        ImageButton imBfavoris = (ImageButton) v.findViewById(R.id.favoris_bouton);
        ImageButton imVAuteur = (ImageButton) v.findViewById(R.id.voir_auteur);
        ImageButton imCCitation = (ImageButton) v.findViewById(R.id.copy_citation);
        ImageButton imShare = (ImageButton) v.findViewById(R.id.share_bouton);

        imVAuteur.setBackgroundResource(R.drawable.icon_citation);
        imCCitation.setBackgroundResource(R.drawable.icon_citation);
        imShare.setBackgroundResource(R.drawable.icon_citation);
        imBfavoris.setBackgroundResource(R.drawable.icon_citation);

        if(citationItem.getFavoris() == 1) {
            imBfavoris.setImageResource(R.drawable.star_fill);
        }else{
            imBfavoris.setImageResource(R.drawable.star_empty);
        }
        imBfavoris.setTag(new CitationViewObject(citationItem, null));

        imVAuteur.setTag(citationItem.getId_auteur_link());

        imCCitation.setTag(citationItem.getCitation());

        imShare.setTag(citationItem.getCitation() + " - " + citationItem.getAuteur());

        imVAuteur.setOnClickListener(this);
        imBfavoris.setOnClickListener(this);
        imCCitation.setOnClickListener(this);
        imShare.setOnClickListener(this);

        v.setTag(new CitationViewObject(citationItem, null));

        return v;
    }

    /** listener d'un clique sur la liste des citations*/
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.favoris_bouton :  /** récupération de la citation enregistrer dans l'item  et enregistrement en favoris dans la base de données */
                    CitationItem citationItemTag = (CitationItem) ((CitationViewObject) v.getTag()).getCitation();
                    CitationListAdapterFavoris.insertData(citationItemTag, v, this, getContext(), "favoris");
                    break;

            case R.id.voir_auteur :     String url_auteur = (String) v.getTag();
                    CitationListAdapter.information_auteur(getContext(), url_auteur);
                    break;

            case R.id.copy_citation :   String citation_copy = (String) v.getTag();
                    ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("citation365", citation_copy);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getContext(), "Copie effectuée !", Toast.LENGTH_SHORT).show();
                    break;

            case R.id.share_bouton :    String citation_share = (String) v.getTag();
                                        Intent share = new Intent(Intent.ACTION_SEND);
                                        share.setType("text/plain");
                                        share.putExtra(Intent.EXTRA_TEXT, citation_share);

                                        getContext().startActivity(Intent.createChooser(share, "Partager cette citation !"));
                                        break;
            default :   break;
        }
    }

    /**
     *
     *
     * @param citationData information sur les citations
     * @param view
     *
     * fonction pour ajouter dans la base de données une citation dans la table favoris
     */
    public static void insertData(CitationItem citationData, View view, ArrayAdapter arrayAdapter, Context context, String origine){

        ContentValues values = new ContentValues();
        values.clear();

        values.put(OpenHelper.COLUMN_ID_CITATION_LINK, citationData.getId_citation_link());
        values.put(OpenHelper.COLUMN_ID_AUTEUR_LINK, citationData.getId_auteur_link());
        values.put(OpenHelper.COLUMN_CITATION, citationData.getCitation());
        values.put(OpenHelper.COLUMN_AUTEUR, citationData.getAuteur());

        boolean alreadyExist = false;
        Cursor cursor = context.getApplicationContext().getContentResolver().query(Provider.CONTENT_URI,null,null,null,null);
        //Log.e("FAVORIS"," Nombre de citation en favoris database: "+cursor.getCount());
        int id = -1;

        /** vérification de l'existance d'une citation dans la base de données pour éviter la redondance */
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if(cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_ID_CITATION_LINK)).equals(citationData.getId_citation_link())){
                id = cursor.getInt(cursor.getColumnIndex(OpenHelper.COLUMN_ID));
               // Log.e("FAVORIS"," Ok ");
                alreadyExist = true;
            }
        }

        /**
         * vérification de la redondance, si la citation est ciblée pour la mise en favoris et qu'elle y est déjà donc on le supprime des favoris sinon
         *  on l'ajoute dans la table des favoris.
         * */
        if(!alreadyExist){
            context.getApplicationContext().getContentResolver().insert(Provider.CONTENT_URI, values);
            if(Favoris.adapter != null) {
                citationData.setFavoris(1);
                Favoris.adapter.add(citationData);
                ((ImageButton)view).setImageResource(R.drawable.star_fill);
                //Log.e("FAVORIS", "ADD");
            }
        }else{

            context.getApplicationContext().getContentResolver().delete(Uri.parse(Provider.CONTENT_URI.toString() + "/" + id), null, null);
            if(Favoris.adapter != null) {

                /** si l'appel vient du fragment de la page de favoris */
                if(origine.equals("favoris")) {

                    final View animView = (View) view.getParent().getParent();
                    final Animation animation_out = AnimationUtils.loadAnimation(context,
                            android.R.anim.fade_out);
                    final CitationItem citationDataFinal = citationData;

                    animation_out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                        }
                        @Override
                        public void onAnimationEnd(Animation animation) {

                            Favoris.adapter.remove(citationDataFinal);
                            SynchronisationFavoris.synchronisationElement(Accueil.listCitationItem, Favoris.adapter);
                            SynchronisationFavoris.synchronisationElement(Recherche.listCitationItemRecherche, Favoris.adapter);

                            Favoris.adapter.notifyDataSetChanged();
                            Accueil.citationListAdapter.notifyDataSetChanged();
                            Recherche.citationListAdapterRecherche.notifyDataSetChanged();
                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    animView.startAnimation(animation_out);
                }

                /** si l'appel vient du fragment de la page d'accueil */
                if(origine.equals("accueil")){

                    Favoris.adapter.remove(citationData);
                    citationData.setFavoris(0);
                    ((ImageButton)view).setImageResource(R.drawable.star_empty);

                    Favoris.adapter.notifyDataSetChanged();
                    Accueil.citationListAdapter.notifyDataSetChanged();
                    Recherche.citationListAdapterRecherche.notifyDataSetChanged();

                }

            }
        }

    }

}
