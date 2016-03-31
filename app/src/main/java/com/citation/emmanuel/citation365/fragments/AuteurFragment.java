package com.citation.emmanuel.citation365.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.adapters.MyFragmentPagerAdapter;
import com.citation.emmanuel.citation365.tools.Connect;
import com.citation.emmanuel.citation365.tools.RequeteAuteur;

import java.util.List;

/**
 * Created by emmanuel on 02/10/2015.
 */
public class AuteurFragment extends Fragment implements View.OnClickListener {

    /** contener de la liste, page de chargement et du text error no connection */

    private FrameLayout flLoading = null;
    private FrameLayout flConnection = null;
    private LinearLayout llFiche_auteur = null;

    private com.citation.emmanuel.citation365.models.Auteur auteur = null;

    private RequeteAuteur requete = null;

    private ImageButton btn_back = null;
    private FloatingActionButton btn_autre_citation = null;
    private FloatingActionButton btn_refresh = null;


    private Bundle savedInstanceStateTampon = null;

    private String url_auteur = null;

    private List<Fragment> listFragments = null;
    private MyFragmentPagerAdapter myFragmentPagerAdapter = null;
    private ViewPager viewpager = null;

    /**
     *
     * @return
     */
    public ViewPager getViewpager() {
        return viewpager;
    }

    public void setViewpager(ViewPager viewpager) {
        this.viewpager = viewpager;
    }

    /**
     *
     * @return
     */
    public List<Fragment> getListFragments() {
        return listFragments;
    }

    public void setListFragments(List<Fragment> listFragments) {
        this.listFragments = listFragments;
    }

    /**
     *
     * @return
     */
    public MyFragmentPagerAdapter getMyFragmentPagerAdapter() {
        return myFragmentPagerAdapter;
    }

    /**
     *
     * @param myFragmentPagerAdapter
     */
    public void setMyFragmentPagerAdapter(MyFragmentPagerAdapter myFragmentPagerAdapter) {
        this.myFragmentPagerAdapter = myFragmentPagerAdapter;
    }

    /**
     *
     * @param url_auteur
     */
    public void setUrl_auteur(String url_auteur){

        this.url_auteur = url_auteur;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** on sauvegarde le bundle pour pouvoir lancer un oncreate au besoin avec ce parametre */
        this.savedInstanceStateTampon = savedInstanceState;

        /** initialisation de la classe permettant la récupération des données */
        this.requete = new RequeteAuteur();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /** on récupère le fragment accueil */
        View v = inflater.inflate(R.layout.fragment_fiche_auteur, container, false);

        /** récupération de la vue de chargement  */
        if(this.flLoading == null) {
            this.flLoading = (FrameLayout) v.findViewById(R.id.loading_data_fragment_fiche_auteur);
        }

        /** récupération de la vue de manque de connection  */
        if(this.flConnection == null) {
            this.flConnection = (FrameLayout) v.findViewById(R.id.connection_invalide_fragment_fiche_auteur);
        }

        /** récupération de la vue de la fiche auteur  */
        if(this.llFiche_auteur == null) {
            this.llFiche_auteur = (LinearLayout) v.findViewById(R.id.fragment_fiche_auteur);
        }

        /** on initialise le button de retour */
        if(this.btn_back == null) {
            //this.btn_back = (ImageButton) v.findViewById(R.id.btn_back);
            //this.btn_back.setOnClickListener(this);
        }

        /** on initialise le button des autres citations de l'auteur */
        if(this.btn_refresh == null) {
            this.btn_refresh = (FloatingActionButton) v.findViewById(R.id.btn_refresh_fragment_fiche_auteur);
            this.btn_refresh.setOnClickListener(this);
        }

        /** on initialise le button de rafraichissement */
        if(this.btn_autre_citation == null) {
            this.btn_autre_citation = (FloatingActionButton) v.findViewById(R.id.btn_autre_citation);
            this.btn_autre_citation.setOnClickListener(this);
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
                this.llFiche_auteur.setVisibility(View.VISIBLE);

                this.requete.getAuteurInformation(this.url_auteur, this.llFiche_auteur, this.flLoading, this.flConnection, this.myFragmentPagerAdapter, this.listFragments, this.viewpager, getContext());

                /** sinon on prévient l'utilisateur qu'il faut une connexion */
            }else{
                this.flLoading.setVisibility(View.GONE);
                this.flConnection.setVisibility(View.VISIBLE);
                this.llFiche_auteur.setVisibility(View.GONE);
            }

    }



    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_refresh_fragment_fiche_auteur : onViewStateRestored(this.savedInstanceStateTampon);
                                                          break;

            case R.id.btn_autre_citation :  this.viewpager.setCurrentItem(1);
                                            break;
/*
            case R.id.btn_back : getActivity().onBackPressed();
                                 getActivity().overridePendingTransition(R.anim.slide_enter , R.anim.slide_leave);
                                 break;*/

            default :    break;
        }
    }

}
