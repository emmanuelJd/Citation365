package com.citation.emmanuel.citation365.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.adapters.CitationListAdapter;
import com.citation.emmanuel.citation365.adapters.ListeAuteurAdapter;
import com.citation.emmanuel.citation365.models.ItemAuteur;
import com.citation.emmanuel.citation365.tools.Connect;
import com.citation.emmanuel.citation365.tools.RequeteListeAuteur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmanuel on 02/10/2015.
 */
public class ListeAuteurIndexFragment extends Fragment implements View.OnClickListener {

    /** contener de la liste, page de chargement et du text error no connection */

    private FrameLayout flLoading = null;
    private FrameLayout flConnection = null;
    private ListView lvListeAuteurIndex = null;

    private RequeteListeAuteur requete = null;

    private FloatingActionButton btn_refresh = null;


    private Bundle savedInstanceStateTampon = null;

    public static List<ItemAuteur> liste_auteur = null;
    public static ListeAuteurAdapter listeAuteurAdapter = null;

    public ListeAuteurIndexFragment(){
        super();
        if(this.liste_auteur == null) {
            this.liste_auteur = new ArrayList<ItemAuteur>();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** on sauvegarde le bundle pour pouvoir lancer un oncreate au besoin avec ce parametre */
        this.savedInstanceStateTampon = savedInstanceState;

        /** initialisation de la classe permettant la récupération des données */
        this.requete = new RequeteListeAuteur();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** on récupère le fragment accueil */
        View v = inflater.inflate(R.layout.fragment_liste_auteur_par_index, container, false);

        /** récupération de la vue de chargement  */
        if(this.flLoading == null) {
            this.flLoading = (FrameLayout) v.findViewById(R.id.loading_data_liste_auteur_index);
        }

        /** récupération de la vue de manque de connection  */
        if(this.flConnection == null) {
            this.flConnection = (FrameLayout) v.findViewById(R.id.connection_invalide_liste_auteur_index);
        }

        /** récupération de la vue de la fiche auteur  */
        if(this.lvListeAuteurIndex == null) {
            this.lvListeAuteurIndex = (ListView) v.findViewById(R.id.liste_auteur_index);

        }

        /**  */
        if(getActivity().getIntent().getParcelableArrayListExtra("LISTE_AUTEUR") != null) {
            this.liste_auteur = getActivity().getIntent().getParcelableArrayListExtra("LISTE_AUTEUR");
        }

        /** on initialise l'adapter de la liste */
        if(this.listeAuteurAdapter == null) {
            this.listeAuteurAdapter = new ListeAuteurAdapter(getActivity(), R.layout.item_liste_auteur, this.liste_auteur);
        }


        /** on initialise le button des autres citations de l'auteur */
        if(this.btn_refresh == null) {
            this.btn_refresh = (FloatingActionButton) v.findViewById(R.id.btn_refresh_fragment_liste_auteur_index);
            this.btn_refresh.setOnClickListener(this);
        }

        return v;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        /** Si une connexion est disponible on effectue la requete */
        if(Connect.isNetworkAvailable(getActivity())){

            this.flLoading.setVisibility(View.GONE);
            this.flConnection.setVisibility(View.GONE);
            this.lvListeAuteurIndex.setVisibility(View.VISIBLE);

            if(this.liste_auteur != null && this.liste_auteur.size() > 0) {
                ListeAuteurIndexFragment.listeAuteurAdapter = new ListeAuteurAdapter(getContext(), R.layout.item_citation_list, ListeAuteurIndexFragment.liste_auteur);
                this.lvListeAuteurIndex.setAdapter(ListeAuteurIndexFragment.listeAuteurAdapter);
                this.lvListeAuteurIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        String host = "http://evene.lefigaro.fr";
                        String url_auteur = (String) view.getTag();
                        url_auteur = url_auteur.substring(host.length(), url_auteur.length() );
                        CitationListAdapter.information_auteur(getContext(), url_auteur);

                    }
                });
            }

            /** sinon on prévient l'utilisateur qu'il faut une connexion */
        }else{
            this.flLoading.setVisibility(View.GONE);
            this.flConnection.setVisibility(View.VISIBLE);
            this.lvListeAuteurIndex.setVisibility(View.GONE);
        }

    }



    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_refresh_fragment_liste_auteur : onViewStateRestored(this.savedInstanceStateTampon);
                break;

            default :    break;
        }
    }


}
