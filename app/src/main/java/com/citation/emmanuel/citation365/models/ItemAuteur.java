package com.citation.emmanuel.citation365.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by emmanuel on 13/02/2016.
 */
public class ItemAuteur implements Parcelable {

    private String nom_auteur = null;
    private String url_auteur = null;
    private String image_url = null;
    private String fonction_auteur = null;

    public ItemAuteur(String nom_auteur, String url_auteur, String image_url, String fonction_auteur){

        this.nom_auteur = nom_auteur;
        this.url_auteur = url_auteur;
        this.image_url = image_url;
        this.fonction_auteur = fonction_auteur;
    }

    public String getNom_auteur() {
        return nom_auteur;
    }

    public void setNom_auteur(String nom_auteur) {
        this.nom_auteur = nom_auteur;
    }

    public String getUrl_auteur() {
        return url_auteur;
    }

    public void setUrl_auteur(String url_auteur) {
        this.url_auteur = url_auteur;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getFonction_auteur() {
        return fonction_auteur;
    }

    public void setFonction_auteur(String fonction_auteur) {
        this.fonction_auteur = fonction_auteur;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nom_auteur);
        dest.writeString(this.url_auteur);
        dest.writeString(this.fonction_auteur);
        dest.writeString(this.image_url);
    }

    public static final Parcelable.Creator<ItemAuteur> CREATOR = new Parcelable.Creator<ItemAuteur>()
    {
        @Override
        public ItemAuteur createFromParcel(Parcel source)
        {
            return new ItemAuteur(source);
        }

        @Override
        public ItemAuteur[] newArray(int size)
        {
            return new ItemAuteur[size];
        }
    };

    public ItemAuteur(Parcel in) {
        this.nom_auteur = in.readString();
        this.url_auteur = in.readString();
        this.fonction_auteur = in.readString();
        this.image_url = in.readString();
    }
}