package com.citation.emmanuel.citation365.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;

import com.citation.emmanuel.citation365.ListeAuteurIndex;
import com.citation.emmanuel.citation365.R;
import com.citation.emmanuel.citation365.adapters.IndexAdapterListeAuteur;
import com.citation.emmanuel.citation365.adapters.MyFragmentPagerAdapter;
import com.citation.emmanuel.citation365.models.ItemListAuteur;
import com.citation.emmanuel.citation365.tools.Connect;
import com.citation.emmanuel.citation365.tools.RequeteListeAuteur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmanuel on 02/10/2015.
 */
public class ListeAuteurFragment extends Fragment implements View.OnClickListener {

    /** contener de la liste, page de chargement et du text error no connection */

    private FrameLayout flLoading = null;
    private FrameLayout flConnection = null;
    private GridView gvIndexAuteur = null;

    private RequeteListeAuteur requete = null;

    //private ImageButton btn_back = null;
    private FloatingActionButton btn_refresh = null;


    private Bundle savedInstanceStateTampon = null;

    public static ArrayList<ItemListAuteur> liste_auteur_tableau = null;
    public static IndexAdapterListeAuteur indexAdapterListeAuteur = null;

    private List<Fragment> listFragments = null;
    private MyFragmentPagerAdapter myFragmentPagerAdapter = null;
    private ViewPager viewpager = null;

    public ListeAuteurFragment(){
        super();
        if(this.liste_auteur_tableau == null) {
            this.liste_auteur_tableau = new ArrayList<ItemListAuteur>();
        }
    }
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
        View v = inflater.inflate(R.layout.fragment_liste_auteur, container, false);

        /** récupération de la vue de chargement  */
        if(this.flLoading == null) {
            this.flLoading = (FrameLayout) v.findViewById(R.id.loading_data_fragment_liste_auteur);
        }

        /** récupération de la vue de manque de connection  */
        if(this.flConnection == null) {
            this.flConnection = (FrameLayout) v.findViewById(R.id.connection_invalide_fragment_liste_auteur);
        }

        /** récupération de la vue de la fiche auteur  */
        if(this.gvIndexAuteur == null) {
            this.gvIndexAuteur = (GridView) v.findViewById(R.id.gridview_index_auteur);

        }

        /** on initialise l'adapter de la liste */
        if(this.indexAdapterListeAuteur == null) {
            this.indexAdapterListeAuteur = new IndexAdapterListeAuteur(getActivity(), R.layout.item_gridview_index, this.liste_auteur_tableau, getActivity().getSupportFragmentManager());
        }


        /** on initialise le button des autres citations de l'auteur */
        if(this.btn_refresh == null) {
            this.btn_refresh = (FloatingActionButton) v.findViewById(R.id.btn_refresh_fragment_liste_auteur);
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
            this.gvIndexAuteur.setVisibility(View.VISIBLE);

            if(this.liste_auteur_tableau != null && this.liste_auteur_tableau.size() > 0) {
                ListeAuteurFragment.indexAdapterListeAuteur = new IndexAdapterListeAuteur(getContext(), R.layout.item_gridview_index, ListeAuteurFragment.liste_auteur_tableau, getActivity().getSupportFragmentManager());
                this.gvIndexAuteur.setAdapter(ListeAuteurFragment.indexAdapterListeAuteur);
                this.gvIndexAuteur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                        ItemListAuteur itemListAuteur = (ItemListAuteur) view.getTag();
                        Intent intent = new Intent(getActivity(), ListeAuteurIndex.class);
                        intent.putParcelableArrayListExtra("LISTE_AUTEUR",(ArrayList)itemListAuteur.getListAuteur());
                        intent.putExtra("LIST_AUTEUR_TITLE", "Auteur en  \"" + itemListAuteur.getIndex()+"\"");
                        getContext().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.slide_enter_2, R.anim.slide_leave_2);
                    }
                });
            }else{
                this.requete.getListeAuteur(getContext(), this.flLoading, this.flConnection, this.gvIndexAuteur, this.getActivity().getSupportFragmentManager());
            }
            /** sinon on prévient l'utilisateur qu'il faut une connexion */
        }else{
            this.flLoading.setVisibility(View.GONE);
            this.flConnection.setVisibility(View.VISIBLE);
            this.gvIndexAuteur.setVisibility(View.GONE);
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
