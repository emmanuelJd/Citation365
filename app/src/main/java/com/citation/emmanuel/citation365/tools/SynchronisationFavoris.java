package com.citation.emmanuel.citation365.tools;

import com.citation.emmanuel.citation365.adapters.CitationListAdapterFavoris;
import com.citation.emmanuel.citation365.models.CitationItem;

import java.util.ArrayList;


/**
 * Created by emmanuel on 06/01/2016.
 * SynchronisationFavoris permet de vérifié l'existance d'une citation en favoris afin d'être flag comme tel
 */
public class SynchronisationFavoris {

    /**
     *
     * @param listCitationItem la liste des citations que l'on souhaite vérifier l'appartenance à la liste des favoris
     * @param adapterFavoris l'adapter des citations favorite
     *
     * fonction pour synchroniser une liste de citation avec les favories
     */
    public static ArrayList<CitationItem> synchronisationElement(ArrayList<CitationItem> listCitationItem, CitationListAdapterFavoris adapterFavoris){

        for( int i = 0; i < listCitationItem.size(); i++){
            boolean citationFavoris = false;
            int j = 0;
            listCitationItem.get(i).setFavoris(0);
            while( j < adapterFavoris.getCount() && !citationFavoris ){
                if(listCitationItem.get(i).getId_citation_link().equals(adapterFavoris.getItem(j).getId_citation_link())){
                    listCitationItem.get(i).setFavoris(1);
                    citationFavoris = true;
                }
                j++;
            }
        }

        return listCitationItem;

    }


}
