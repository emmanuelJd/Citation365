package com.citation.emmanuel.citation365.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.models.CitationItem;
import com.citation.emmanuel.citation365.models.CitationViewObject;

import java.util.List;

/**
 * Created by emmanuel on 05/10/2015.
 *
 * l'adapter pour la liste des citations
 */
public class CitationListAdapterAuteurAutreCitation extends ArrayAdapter<CitationItem> implements View.OnClickListener/*, View.OnTouchListener*/{

    private Context context;
    private int resLayout;
    private List<CitationItem> listCitationItems;
    private ListView lvCitation;


    /** constructeur de l'adapter  */
    public CitationListAdapterAuteurAutreCitation(Context context, int resLayout, List<CitationItem> listCitationItems) {
        super(context, resLayout, listCitationItems);

        this.context = context;
        this.resLayout = resLayout;
        this.listCitationItems = listCitationItems;
    }

    /** constructeur de l'adapter  */
    public CitationListAdapterAuteurAutreCitation(Context context, int resLayout, List<CitationItem> listCitationItems, ListView lvCitation) {
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

        if (convertView == null) {
            v = layoutInflater.inflate(R.layout.item_citation_list, null);
        } else {
            v = convertView;
        }

        /** on récupère les éléments qui contiendront la citation et l'auteur*/
        TextView tvCitation = (TextView) v.findViewById(R.id.citation_text);
        TextView tvAuteur = (TextView) v.findViewById(R.id.citation_auteur);

        /** on récupère la variable contenant les informations */
        CitationItem citationItem = listCitationItems.get(position);

        /** on insert les informations dans les éléments*/
        tvCitation.setText(citationItem.getCitation());
        tvAuteur.setText(citationItem.getAuteur());


        /** on récupère les boutons pour appliquer l'image associé et ajouter les listeners */
        ImageButton imBfavoris = (ImageButton) v.findViewById(R.id.favoris_bouton);
        ImageButton imVAuteur = (ImageButton) v.findViewById(R.id.voir_auteur);
        ImageButton imCCitation = (ImageButton) v.findViewById(R.id.copy_citation);
        ImageButton imShare = (ImageButton) v.findViewById(R.id.share_bouton);

        imVAuteur.setVisibility(View.GONE);

        imCCitation.setBackgroundResource(R.drawable.icon_citation);
        imShare.setBackgroundResource(R.drawable.icon_citation);
        imBfavoris.setBackgroundResource(R.drawable.icon_citation);

        if(citationItem.getFavoris() == 1) {
            imBfavoris.setImageResource(R.drawable.star_fill);
        }else{
            imBfavoris.setImageResource(R.drawable.star_empty);
        }
        imBfavoris.setTag(new CitationViewObject(citationItem, null));

        imCCitation.setTag(citationItem.getCitation());

        imShare.setTag(citationItem.getCitation() + " - " +citationItem.getAuteur());

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
                                        CitationListAdapterFavoris.insertData(citationItemTag, v, this, getContext(), "accueil");
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

}
