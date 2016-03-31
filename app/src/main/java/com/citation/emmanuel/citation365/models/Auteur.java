package com.citation.emmanuel.citation365.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by emmanuel on 05/10/2015.
 *
 * classe métier d'une citation, contenant les informations sous format permettant le transfert de données.
 */
public class Auteur implements Parcelable{

    private String nom_auteur = null;
    private String fonction_auteur = null;
    private String biographie_auteur = null;
    private String citation_initiale = null;
    private String image_url = null;
    private String autre_citation = null;

    public Auteur(){

    }

    public Auteur(String nom_auteur, String fonction_auteur, String biographie_auteur, String citation_initiale, String image_url, String autre_citation) {

        this.nom_auteur = nom_auteur;
        this.fonction_auteur = fonction_auteur;
        this.biographie_auteur = biographie_auteur;
        this.citation_initiale = citation_initiale;
        this.image_url = image_url;
        this.autre_citation = autre_citation;
    }


    public String getNom_auteur() {
        return nom_auteur;
    }

    public void setNom_auteur(String nom_auteur) {
        this.nom_auteur = nom_auteur;
    }

    public String getBiographie_auteur() {
        return biographie_auteur;
    }

    public void setBiographie_auteur(String biographie_auteur) {
        this.biographie_auteur = biographie_auteur;
    }

    public String getFonction_auteur() {
        return fonction_auteur;
    }

    public void setFonction_auteur(String fonction_auteur) {
        this.fonction_auteur = fonction_auteur;
    }

    public String getCitation_initiale() {
        return citation_initiale;
    }

    public void setCitation_initiale(String citation_initiale) {
        this.citation_initiale = citation_initiale;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public String getAutre_citation() {
        return autre_citation;
    }

    public void setAutre_citation(String autre_citation) {
        this.autre_citation = autre_citation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nom_auteur);
        dest.writeString(this.fonction_auteur);
        dest.writeString(this.biographie_auteur);
        dest.writeString(this.citation_initiale);
        dest.writeString(this.image_url);
        dest.writeString(this.autre_citation);
    }

    public static final Parcelable.Creator<Auteur> CREATOR = new Parcelable.Creator<Auteur>()
    {
        @Override
        public Auteur createFromParcel(Parcel source)
        {
            return new Auteur(source);
        }

        @Override
        public Auteur[] newArray(int size)
        {
            return new Auteur[size];
        }
    };

    public Auteur(Parcel in) {
        this.nom_auteur = in.readString();
        this.fonction_auteur = in.readString();
        this.biographie_auteur = in.readString();
        this.citation_initiale = in.readString();
        this.image_url = in.readString();
        this.autre_citation = in.readString();
    }
}