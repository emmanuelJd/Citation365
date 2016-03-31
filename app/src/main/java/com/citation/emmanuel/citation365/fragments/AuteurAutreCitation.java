package com.citation.emmanuel.citation365.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.citation.emmanuel.citation365.adapters.CitationListAdapterAuteurAutreCitation;
import com.citation.emmanuel.citation365.models.CitationItem;
import com.citation.emmanuel.citation365.tools.Connect;
import com.citation.emmanuel.citation365.tools.RequeteCitationAuteur;
import com.citation.emmanuel.citation365.tools.RequeteImageDeFondNasa;

import java.util.ArrayList;

/**
 * Created by emmanuel on 02/10/2015.
 */
public class AuteurAutreCitation extends Fragment implements AbsListView.OnScrollListener, View.OnClickListener {

    /** contener de la liste, page de chargement et du text error no connection */
    private FrameLayout llConnection = null;
    private FrameLayout flLoading = null;
    private ListView lvCitation = null;

    private ImageView ivFondEcran = null;

    public static ArrayList<CitationItem> listCitationItemAutreCitation = null;
    public static CitationListAdapterAuteurAutreCitation citationListAdapterAutreCitation = null;

    private RequeteCitationAuteur requeteAuteurAutreCitation = null;
    private RequeteImageDeFondNasa requeteImageNasa = null;

    private static int numero_page = -1;
    private static int preLast = 0;

    private FloatingActionButton btn_refresh = null;
    private FloatingActionButton btn_back_fiche_auteur = null;

    private String url_auteur_autre_citation = null;

    private Bundle savedInstanceStateTampon = null;

    private ViewPager viewpager = null;


    public AuteurAutreCitation(){
        super();

        if(AuteurAutreCitation.listCitationItemAutreCitation != null && AuteurAutreCitation.listCitationItemAutreCitation.size() > 0){
            AuteurAutreCitation.listCitationItemAutreCitation.clear();
            if(AuteurAutreCitation.citationListAdapterAutreCitation != null){
                AuteurAutreCitation.citationListAdapterAutreCitation.notifyDataSetChanged();
            }
        }
    }

    /**
     *
     * @return
     */
    public ViewPager getViewpager() {
        return viewpager;
    }

    /**
     *
     * @param viewpager
     */
    public void setViewpager(ViewPager viewpager) {
        this.viewpager = viewpager;
    }

    /**
     *
     * @return
     */
    public String getUrl_auteur_autre_citation() {
        return url_auteur_autre_citation;
    }

    /**
     *
     * @param url_auteur_autre_citation
     */
    public void setUrl_auteur_autre_citation(String url_auteur_autre_citation) {
        this.url_auteur_autre_citation = url_auteur_autre_citation;
    }

    /** enregistrement de l'état de l'activité lors de sa fin */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /** Enregistrement des citations */
        outState.putParcelableArrayList("lvCitation", this.listCitationItemAutreCitation);

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
        this.requeteAuteurAutreCitation = new RequeteCitationAuteur();
        this.requeteImageNasa = new RequeteImageDeFondNasa();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** on récupère le fragment accueil */
        View v = inflater.inflate(R.layout.fragment_fiche_auteur_autre_citation, container, false);

        /** récupération de la liste présente dans le fragment */
        if(this.lvCitation == null) {
            this.lvCitation = (ListView) v.findViewById(R.id.nav_list_citation_auteur_autre_citation);
        }

        /** récupération du container dans le fragment */
        if(this.ivFondEcran == null) {
            this.ivFondEcran = (ImageView) v.findViewById(R.id.image_de_fond_autre_citation);
        }

        /** récupération du texte d'erreur de connection présente dans le fragment */
        if(this.llConnection == null) {
            this.llConnection = (FrameLayout) v.findViewById(R.id.connection_invalide_auteur_autre_citation);
        }

        /** récupération de la page de chargement présente dans le fragment */
        if(this.flLoading == null) {
            this.flLoading = (FrameLayout) v.findViewById(R.id.loading_data_auteur_autre_citation);
        }

        /** on initialise la liste des citations */
        if(this.listCitationItemAutreCitation == null) {
            this.listCitationItemAutreCitation = new ArrayList<CitationItem>();
        }

        /** on initialise l'adapter de la liste */
        if(this.citationListAdapterAutreCitation == null) {
            this.citationListAdapterAutreCitation = new CitationListAdapterAuteurAutreCitation(getActivity(), R.layout.item_citation_list, this.listCitationItemAutreCitation);
        }

        /** on initialise le button de rafraichissement */
        if(this.btn_refresh == null) {
            this.btn_refresh = (FloatingActionButton) v.findViewById(R.id.btn_refresh_auteur_autre_citation);
            this.btn_refresh.setOnClickListener(this);
        }

        /** on initialise le button de rafraichissement */
        if(this.btn_back_fiche_auteur == null) {
            this.btn_back_fiche_auteur = (FloatingActionButton) v.findViewById(R.id.btn_back_auteur_autre_citation);
            this.btn_back_fiche_auteur.setOnClickListener(this);
        }

        /** ajout de l'adapter à la liste view */
        this.lvCitation.setAdapter(this.citationListAdapterAutreCitation);
        this.lvCitation.setOnScrollListener(this);

        return v;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        /** on vérifie qu'une connection internet est active */
        if(Connect.isNetworkAvailable(getActivity())){

            this.lvCitation.setVisibility(View.VISIBLE);
            this.llConnection.setVisibility(View.GONE);

            this.numero_page = 1;

            this.requeteAuteurAutreCitation.getCitationAuteurAutre(this.getUrl_auteur_autre_citation(), 1, this.flLoading, this.llConnection, this.lvCitation);
/*
            if(ParametreApp.getValueImage(getContext()) == 1){
                this.requeteImageNasa.getImageNasaFondView(this.lvCitation,getContext());
            }*/
            /** sinon on prévient l'utilisateur qu'il faut une connexion */
        }else{
            this.lvCitation.setVisibility(View.GONE);
            this.llConnection.setVisibility(View.VISIBLE);
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
            case R.id.nav_list_citation_auteur_autre_citation:

                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount) {
                    if(AuteurAutreCitation.preLast!=lastItem){
                        if(Connect.isNetworkAvailable(getActivity())) {
                            this.requeteAuteurAutreCitation.getCitationAuteurAutre(this.getUrl_auteur_autre_citation(), this.getPage(), this.flLoading, this.llConnection, this.lvCitation);
                            AuteurAutreCitation.preLast = lastItem;
                        }else{
                            Toast.makeText(getContext(), "Pas de connexion disponible !", Toast.LENGTH_LONG).show();
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

        AuteurAutreCitation.numero_page++;
        return AuteurAutreCitation.numero_page;
    }

    /**
     * fonctionnalité pour remettre le cursor tampon de la derniere citation visité à 0 afin de pouvoir relancer la récupération des données en cas d'erreur de chargement
     */
    public static void resetPrelast(){
        AuteurAutreCitation.preLast = 0;
    }


    /**
     * fonctionnalité pour mettre la page courante à jour en cas d'échec de chargement
     */
    public static void backPage(){
        AuteurAutreCitation.numero_page--;
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_refresh_auteur_autre_citation : onViewStateRestored(this.savedInstanceStateTampon);
                break;

            case R.id.btn_back_auteur_autre_citation : this.viewpager.setCurrentItem(0);
                break;

            default :    break;
        }
    }

}

