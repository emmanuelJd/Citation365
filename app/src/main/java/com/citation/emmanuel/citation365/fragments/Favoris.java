package com.citation.emmanuel.citation365.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.citation.emmanuel.citation365.ParametreApp;
import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.adapters.CitationListAdapterFavoris;
import com.citation.emmanuel.citation365.models.CitationItem;
import com.citation.emmanuel.citation365.stockage.OpenHelper;
import com.citation.emmanuel.citation365.stockage.Provider;
import com.citation.emmanuel.citation365.tools.Connect;
import com.citation.emmanuel.citation365.tools.RequeteImageDeFondNasa;

import java.util.ArrayList;

/**
 * Created by emmanuel on 02/10/2015.
 */
public class Favoris extends Fragment {

    public static CitationListAdapterFavoris adapter = null;

    private ArrayList<CitationItem> citationList;
    private ListView lvcitation_favoris = null;

    private ImageView ivFondEcran = null;

    private RequeteImageDeFondNasa requeteImageNasa = null;


    /** Create simple cursor adapter to connect the cursor dataset we load with a ListView */
    /*private void setupCursorAdapter(Activity activity) {

        this.citationList = new ArrayList<CitationItem>();
        Favoris.adapter = new CitationListAdapterFavoris(getActivity(), R.layout.item_citation_list, this.citationList);

        Cursor cursor = activity.getApplicationContext().getContentResolver().query(Provider.CONTENT_URI,null,null,null,null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String id_citation_link = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_ID_CITATION_LINK));
            String id_auteur_link = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_ID_AUTEUR_LINK));
            String citation = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_CITATION));
            String auteur = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTEUR));

            Favoris.adapter.add(new CitationItem(citation, auteur, id_citation_link, id_auteur_link, 1));
        }

    }
*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** initialisation de la liste de citation mise en favoris */
        this.citationList = new ArrayList<CitationItem>();

        Favoris.adapter = new CitationListAdapterFavoris(getActivity(), R.layout.item_citation_list, this.citationList);

        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(Provider.CONTENT_URI,null,null,null,null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String id_citation_link = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_ID_CITATION_LINK));
            String id_auteur_link = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_ID_AUTEUR_LINK));
            String citation = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_CITATION));
            String auteur = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTEUR));

            Favoris.adapter.add(new CitationItem(citation, auteur, id_citation_link, id_auteur_link, 1));
        }

        this.requeteImageNasa = new RequeteImageDeFondNasa();
    }

    /** fonction pour synchroniser les favoris présents dans la base de données et la variable de la classe Favoris */
    /*public static void getFavorisDataBase(Activity activity){


        setupCursorAdapter(activity);

        Cursor cursor = activity.getApplicationContext().getContentResolver().query(Provider.CONTENT_URI,null,null,null,null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            String id_citation_link = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_ID_CITATION_LINK));
            String id_auteur_link = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_ID_AUTEUR_LINK));
            String citation = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_CITATION));
            String auteur = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTEUR));

            Favoris.adapter.add(new CitationItem(citation, auteur, id_citation_link, id_auteur_link, 1));
        }
    }
*/



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favoris, container, false);


        /** récupération de la liste présente dans le fragment */
        if(this.lvcitation_favoris == null) {
            this.lvcitation_favoris = (ListView) v.findViewById(R.id.nav_list_citation_favoris);
        }

        /** récupération de la liste présente dans le fragment */
        if(this.ivFondEcran == null) {
            this.ivFondEcran = (ImageView) v.findViewById(R.id.image_de_fond_favoris);
        }
/*
        ListView lvContacts = (ListView) v.findViewById(R.id.nav_list_citation_favoris);
        lvContacts.setAdapter(Favoris.adapter);

        Cursor cursor = getActivity().getApplicationContext().getContentResolver().query(Provider.CONTENT_URI,null,null,null,null);
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            //Log.d("DATA PROVIDER",cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_CITATION)));
            String id_citation_link = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_ID_CITATION_LINK));
            String id_auteur_link = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_ID_AUTEUR_LINK));
            String citation = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_CITATION));
            String auteur = cursor.getString(cursor.getColumnIndex(OpenHelper.COLUMN_AUTEUR));

            Favoris.adapter.add(new CitationItem(citation, auteur, id_citation_link, id_auteur_link, 1));
        }
*/

        this.lvcitation_favoris.setAdapter(Favoris.adapter);

        return v;
    }



    @Override
    public void onResume() {
        super.onResume();
        /** on vérifie qu'une connection internet est active */
        if(Connect.isNetworkAvailable(getActivity())){

            if(ParametreApp.getValueImage(getContext()) == 1){
                this.requeteImageNasa.getImageNasaFondView(this.ivFondEcran,getContext());
            }
        }
    }

}
