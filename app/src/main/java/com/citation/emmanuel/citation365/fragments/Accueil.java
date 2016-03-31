package com.citation.emmanuel.citation365.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.citation.emmanuel.citation365.ParametreApp;
import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.adapters.CitationListAdapter;
import com.citation.emmanuel.citation365.models.CitationItem;
import com.citation.emmanuel.citation365.tools.Connect;
import com.citation.emmanuel.citation365.tools.RequeteCitationDuJour;
import com.citation.emmanuel.citation365.tools.RequeteImageDeFondNasa;

import java.util.ArrayList;

/**
 * Created by emmanuel on 02/10/2015.
 */
public class Accueil extends Fragment implements AbsListView.OnScrollListener, View.OnClickListener {

    /** contener de la liste, page de chargement et du text error no connection */
    private FrameLayout llConnection = null;
    private FrameLayout flLoading = null;
    private ListView lvCitation = null;

    private ImageView ivFondEcran = null;

    public static ArrayList<CitationItem> listCitationItem = null;
    public static CitationListAdapter citationListAdapter = null;

    private RequeteCitationDuJour requete = null;
    private RequeteImageDeFondNasa requeteImageNasa = null;

    private static int numero_page = -1;
    private static int preLast = 0;

    private FloatingActionButton btn_refresh = null;

    private Bundle savedInstanceStateTampon = null;

    /** enregistrement de l'état de l'activité lors de sa fin */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /** Enregistrement des citations */
        outState.putParcelableArrayList("lvCitation", this.listCitationItem);

        /** Enregistrement de la position visible lors du changement d'activité */
        View v = this.lvCitation.getChildAt(0);

        outState.putInt("index", this.lvCitation.getFirstVisiblePosition());
        outState.putInt("top", (v == null) ? 0 : (v.getTop() - this.lvCitation.getPaddingTop()));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** on sauvegarde le bundle pour pouvoir lancer un oncreate au besoin avec ce parametre */
        this.savedInstanceStateTampon = savedInstanceState;

        /** initialisation de la classe permettant la récupération des données */
        this.requete = new RequeteCitationDuJour();
        this.requeteImageNasa = new RequeteImageDeFondNasa();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            /** on récupère le fragment accueil */
            View v = inflater.inflate(R.layout.fragment_accueil, container, false);

            /** récupération de la liste présente dans le fragment */
            if(this.ivFondEcran == null) {
                this.ivFondEcran = (ImageView) v.findViewById(R.id.image_de_fond);
            }

            /** récupération de la liste présente dans le fragment */
            if(this.lvCitation == null) {
                this.lvCitation = (ListView) v.findViewById(R.id.nav_list_citation);
            }

            /** récupération du texte d'erreur de connection présente dans le fragment */
            if(this.llConnection == null) {
                this.llConnection = (FrameLayout) v.findViewById(R.id.connection_invalide);
            }

            /** récupération de la page de chargement présente dans le fragment */
            if(this.flLoading == null) {
                this.flLoading = (FrameLayout) v.findViewById(R.id.loading_data);
            }

            /** on initialise la liste des citations */
            if(this.listCitationItem == null) {
                this.listCitationItem = new ArrayList<CitationItem>();
            }

            /** on initialise l'adapter de la liste */
            if(this.citationListAdapter == null) {
                this.citationListAdapter = new CitationListAdapter(getActivity(), R.layout.item_citation_list, this.listCitationItem);
            }

            /** on initialise le button de rafraichissement */
            if(this.btn_refresh == null) {
                this.btn_refresh = (FloatingActionButton) v.findViewById(R.id.btn_refresh);
                this.btn_refresh.setOnClickListener(this);
            }

            /** ajout de l'adapter à la liste view */
            this.lvCitation.setAdapter(this.citationListAdapter);
            this.lvCitation.setOnScrollListener(this);

            return v;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        /** on vérifie que l'instance existe afin de récupérer les données sauvegarder */
        if(savedInstanceState != null && !savedInstanceState.isEmpty() && savedInstanceState.containsKey("lvCitation")) {
            this.listCitationItem = savedInstanceState.getParcelableArrayList("lvCitation");
            this.lvCitation.setVisibility(View.VISIBLE);
            this.llConnection.setVisibility(View.GONE);

        }else{
            /** on vérifie qu'une connection internet est active */
            if(Connect.isNetworkAvailable(getActivity()) && this.listCitationItem.size() == 0){

                this.lvCitation.setVisibility(View.VISIBLE);
                this.llConnection.setVisibility(View.GONE);

                this.numero_page = 1;

                this.requete.getCitationDuJour(this.numero_page, this.flLoading, this.llConnection, this.lvCitation);


            }else if(Connect.isNetworkAvailable(getActivity()) && this.listCitationItem.size() > 0) {

                this.lvCitation.setVisibility(View.VISIBLE);
                this.llConnection.setVisibility(View.GONE);

                /** sinon on prévient l'utilisateur qu'il faut une connexion */
            }else{
                this.lvCitation.setVisibility(View.GONE);
                this.llConnection.setVisibility(View.VISIBLE);
            }

        }

        /** si on récupère les données sauvegarder on remet le scroll au niveau de la dernier citation visualisé */
        if(savedInstanceState != null && !savedInstanceState.isEmpty() && savedInstanceState.containsKey("lvCitation")) {
            if(savedInstanceState.containsKey("index") && savedInstanceState.containsKey("top")) {
                this.lvCitation.setSelectionFromTop(savedInstanceState.getInt("index"), savedInstanceState.getInt("top"));
            }
        }


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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    /** listener du scroll */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        /** vérification du scroll sur le dernier élément de la liste afin d'en charger des nouveaux */
        switch(view.getId()) {
            case R.id.nav_list_citation:

                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount) {
                    if(Accueil.preLast!=lastItem){
                        if(Connect.isNetworkAvailable(getActivity())) {
                            this.requete.getCitationDuJour(this.getPage(), this.flLoading, this.llConnection, this.lvCitation);
                            Accueil.preLast = lastItem;
                        }else{
                            Toast.makeText(getContext(),"Pas de connexion disponible !",Toast.LENGTH_LONG).show();
                        }
                    }
                }
        }
    }

    /**
     * fonction pour récupérer le numéro de la  page contenant les données requises
     * 
     * @return int numéro de page
     *
     * */
    public int getPage(){

        Accueil.numero_page++;
        return Accueil.numero_page;
    }

    /**
     * fonctionnalité pour remettre le cursor tampon de la derniere citation visité à 0 afin de pouvoir relancer la récupération des données en cas d'erreur de chargement
     */
    public static void resetPrelast(){
        Accueil.preLast = 0;
    }


    /**
     * fonctionnalité pour mettre la page courante à jour en cas d'échec de chargement
     */
    public static void backPage(){
        Accueil.numero_page--;
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_refresh : onViewStateRestored(this.savedInstanceStateTampon);
                                    break;

            default :    break;
        }
    }

}
