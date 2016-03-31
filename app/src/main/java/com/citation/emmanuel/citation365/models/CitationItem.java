package com.citation.emmanuel.citation365.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by emmanuel on 05/10/2015.
 *
 * classe métier d'une citation, contenant les informations sous format permettant le transfert de données.
 */
public class CitationItem implements Parcelable{

    private String id_citation_link;
    private String id_auteur_link;
    private String citation;
    private String auteur;
    private int favoris;

    public CitationItem(String citation, String auteur, String id_citation_link, String id_auteur_link, int favoris) {
        this.citation = citation;
        this.auteur = auteur;
        this.id_citation_link = id_citation_link;
        this.id_auteur_link = id_auteur_link;
        this.favoris = favoris;
    }

    public String getCitation() {
        return citation;
    }

    public void setCitation(String citation) {
        this.citation = citation;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getId_citation_link() {
        return id_citation_link;
    }

    public void setId_citation_link(String id_citation_link) {
        this.id_citation_link = id_citation_link;
    }

    public String getId_auteur_link() {
        return id_auteur_link;
    }

    public void setId_auteur_link(String id_auteur_link) {
        this.id_auteur_link = id_auteur_link;
    }

    public int getFavoris() {
        return favoris;
    }

    public void setFavoris(int favoris) {
        this.favoris = favoris;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id_citation_link);
        dest.writeString(this.id_auteur_link);
        dest.writeString(this.citation);
        dest.writeString(this.auteur);
        dest.writeInt(this.favoris);
    }

    public static final Parcelable.Creator<CitationItem> CREATOR = new Parcelable.Creator<CitationItem>()
    {
        @Override
        public CitationItem createFromParcel(Parcel source)
        {
            return new CitationItem(source);
        }

        @Override
        public CitationItem[] newArray(int size)
        {
            return new CitationItem[size];
        }
    };

    public CitationItem(Parcel in) {
        this.id_citation_link = in.readString();
        this.id_auteur_link = in.readString();
        this.citation = in.readString();
        this.auteur = in.readString();
        this.favoris = in.readInt();
    }
}
