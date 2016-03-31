package com.citation.emmanuel.citation365.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmanuel on 19/01/2016.
 */
public class ItemListAuteur {

    private String Index = null;

    private List<ItemAuteur> listAuteur = null;

    public ItemListAuteur(){
        this.listAuteur = new ArrayList<ItemAuteur>();
    }

    public void addItemListAuteur(String nom_auteur, String url_auteur, String image_url, String fonction_auteur){
        this.listAuteur.add( new ItemAuteur(nom_auteur, url_auteur, image_url, fonction_auteur) );
    }

    public String getIndex() {
        return Index;
    }

    public void setIndex(String index) {
        Index = index;
    }

    public List<ItemAuteur> getListAuteur() {
        return listAuteur;
    }

    public void setListAuteur(List<ItemAuteur> listAuteur) {
        this.listAuteur = listAuteur;
    }


}


