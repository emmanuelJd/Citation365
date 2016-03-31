package com.citation.emmanuel.citation365.models;

/**
 * Created by emmanuel on 02/10/2015.
 */
public class NavItem {

    private String title;
    private int navIcon;


    public NavItem(String title, int navIcon) {
        super();
        this.title = title;
        this.navIcon = navIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNavIcon() {
        return navIcon;
    }

    public void setNavIcon(int navIcon) {
        this.navIcon = navIcon;
    }
}
