package com.citation.emmanuel.citation365.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.models.ItemListAuteur;

import java.util.List;

/**
 * Created by emmanuel on 05/10/2015.
 *
 * l'adapter pour la liste des citations
 */
public class IndexAdapterListeAuteur extends ArrayAdapter<ItemListAuteur>{

    private Context context;
    private int resLayout;
    private List<ItemListAuteur> itemListAuteur;
    private FragmentManager fm;

    /** constructeur de l'adapter  */
    public IndexAdapterListeAuteur(Context context, int resLayout, List<ItemListAuteur> itemListAuteur, FragmentManager fm) {
        super(context, resLayout, itemListAuteur);

        this.context = context;
        this.resLayout = resLayout;
        this.itemListAuteur = itemListAuteur;
        this.fm = fm;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = null;

        if (convertView == null) {
            v = layoutInflater.inflate(R.layout.item_gridview_index, null);
        } else {
            v = convertView;
        }

        /** on récupère les éléments qui contiendront la citation et l'auteur*/
        TextView index_auteur = (TextView) v.findViewById(R.id.index_auteur);

        /** on récupère la variable contenant les informations */
        ItemListAuteur item_list_auteur = itemListAuteur.get(position);

        /** on insert les informations dans les éléments*/
        index_auteur.setText(item_list_auteur.getIndex());

        v.setTag(item_list_auteur);

        return v;
    }

}
