package com.citation.emmanuel.citation365.models;

import android.view.View;

/**
 * Created by emmanuel on 07/10/2015.
 */
public class CitationViewObject {

    CitationItem citation;
    View view;

    public CitationViewObject(CitationItem citation, View view) {
        this.citation = citation;
        this.view = view;
    }

    public CitationItem getCitation() {
        return citation;
    }

    public void setCitation(CitationItem citation) {
        this.citation = citation;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
