package com.citation.emmanuel.citation365.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.citation.emmanuel.citation365.ParametreApp;
import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.adapters.CitationListAdapterRecherche;
import com.citation.emmanuel.citation365.models.CitationItem;
import com.citation.emmanuel.citation365.tools.Connect;
import com.citation.emmanuel.citation365.tools.RequeteImageDeFondNasa;
import com.citation.emmanuel.citation365.tools.RequeteRechercheCitation;

import java.util.ArrayList;

/**
 * Created by emmanuel on 02/10/2015.
 */
public class Recherche extends Fragment implements AbsListView.OnScrollListener, View.OnClickListener {

    public static ArrayList<CitationItem> listCitationItemRecherche;
    public static CitationListAdapterRecherche citationListAdapterRecherche;
    private static int numero_page = 1;
    private static int preLast = 0;

    private RequeteRechercheCitation requeteResearch = null;
    private RequeteImageDeFondNasa requeteImageNasa = null;


    /** le bouton et le champ texte de la recherche  */
    private FloatingActionButton button_search = null;
    private FloatingActionButton btn_refresh = null;
    private EditText search_edit_text = null;

    private String mot_recherche = null;

    /**  liste des citations résultante et page de chargement et d'erreur connection */
    private ListView lvCitation = null;
    private FrameLayout llConnection = null;
    private FrameLayout flLoading = null;

    private ImageView ivFondEcran = null;

    /** */
    private Bundle savedInstanceStateTampon = null;

    /** enregistrement de l'état de l'activité lors de sa fin */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        /** Enregistrement des citations */
        outState.putParcelableArrayList("lvCitation", this.listCitationItemRecherche);

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
        this.requeteResearch = new RequeteRechercheCitation();
        this.requeteImageNasa = new RequeteImageDeFondNasa();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** on récupère le fragment accueil */
        View v = inflater.inflate(R.layout.fragment_recherche, container, false);

        /**  on récupère le bouton de recherche */
        if(this.button_search == null){
            this.button_search = (FloatingActionButton) v.findViewById(R.id.search_button);
            this.button_search.setOnClickListener(this);
        }

        /** on initialise le button de rafraichissement */
        if(this.btn_refresh == null) {
            this.btn_refresh = (FloatingActionButton) v.findViewById(R.id.btn_refresh_search);
            this.btn_refresh.setOnClickListener(this);
        }

        /** on récupère le filtre de la recherche */
        if(this.search_edit_text == null) {
            this.search_edit_text = (EditText) v.findViewById(R.id.search_edit_text);
        }

        /** on récupère la liste de résultat de la recherche */
        if(this.ivFondEcran == null) {
            this.ivFondEcran = (ImageView) v.findViewById(R.id.image_de_fond_recherche);
        }

        /** on récupère la liste de résultat de la recherche */
        if(this.lvCitation == null) {
            this.lvCitation = (ListView) v.findViewById(R.id.nav_list_search_citation);
        }

        /** on récupère la page de chargement  */
        if(this.flLoading == null) {
            this.flLoading = (FrameLayout) v.findViewById(R.id.loading_data_search);
        }

        /** on récupère la page de reconnection */
        if(this.llConnection == null) {
            this.llConnection = (FrameLayout) v.findViewById(R.id.connection_invalide_search);
        }

        /** on initialise la liste des citations */
        if(this.listCitationItemRecherche == null){
            this.listCitationItemRecherche = new ArrayList<CitationItem>();
        }


        /** on initialise l'adapter de la liste de recherche */
        if(this.citationListAdapterRecherche == null) {
            this.citationListAdapterRecherche = new CitationListAdapterRecherche(getActivity(), R.layout.item_citation_list, this.listCitationItemRecherche);
        }
        /** ajout de l'adapter à la liste view */
        lvCitation.setAdapter(this.citationListAdapterRecherche);

        /** ajout du listener du scroll */
        lvCitation.setOnScrollListener(this);

        return v;
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        /** on vérifie que l'instance existe afin de récupérer les données sauvegarder */
        if(savedInstanceState != null && !savedInstanceState.isEmpty() && savedInstanceState.containsKey("lvCitation")) {
            this.listCitationItemRecherche = savedInstanceState.getParcelableArrayList("lvCitation");
            this.lvCitation.setVisibility(View.VISIBLE);
            this.llConnection.setVisibility(View.GONE);

            /** sinon on initialise le tableau de citation et l'adapter et on récupère les données en ligne */
        }else{
            /** on vérifie qu'une connection internet est active */
            if(Connect.isNetworkAvailable(getActivity())){

                this.lvCitation.setVisibility(View.VISIBLE);
                this.llConnection.setVisibility(View.GONE);

                this.numero_page = 1;

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
                this.requeteImageNasa.getImageNasaFondView(this.ivFondEcran, getContext());
            }
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        /** si on scroll sur la liste des citations on vérifie si on a atteint le dernier élément */
        switch(view.getId()) {
            case R.id.nav_list_search_citation:

                final int lastItem = firstVisibleItem + visibleItemCount;
                if(lastItem == totalItemCount) {
                    if(Recherche.preLast!=lastItem){
                        if(Connect.isNetworkAvailable(getActivity())) {
                            this.requeteResearch.getCitationParRecherche(this.getPage(), this.mot_recherche, this.flLoading, this.llConnection, this.lvCitation, getContext());
                            Recherche.preLast = lastItem;
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

        Recherche.numero_page++;
        return Recherche.numero_page;
    }

    /**
     * fonctionnalité pour remettre le cursor tampon de la derniere citation visité à 0 afin de pouvoir relancer la récupération des données en cas d'erreur de chargement
     */
    public static void resetPrelast(){
        Recherche.preLast = 0;
    }


    /**
     * fonctionnalité pour mettre la page courante à jour en cas d'échec de chargement
     */
    public static void backPage(){
        Recherche.numero_page--;
    }


    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_refresh_search : onViewStateRestored(this.savedInstanceStateTampon);
                break;

            case R.id.search_button : String editValue = this.search_edit_text.getText().toString();
                                      //Log.e("TEXT EDIT", editValue);
                                      this.mot_recherche = editValue;
                                      resetPrelast();
                                      Recherche.numero_page = 1;
                                      Recherche.listCitationItemRecherche.clear();
                                      this.requeteResearch.getCitationParRecherche( Recherche.numero_page, editValue, this.flLoading, this.llConnection, this.lvCitation, getContext() );
                                      break;

            default :    break;
        }
    }

}
