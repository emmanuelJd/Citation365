package com.citation.emmanuel.citation365;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;


/**
 * Created by emmanuel on 14/10/2015.
 */
public class ListeAuteurIndex extends AppCompatActivity {

    /** contener de la liste, page de chargement et du text error no connection */
    private FrameLayout flLoading = null;
    private FrameLayout flConnection = null;
    private ListView lvAuteurList = null;


    public ListeAuteurIndex(){
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.liste_auteur_index);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_liste_auteur_index);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        String liste_auteur_title = getIntent().getStringExtra("LIST_AUTEUR_TITLE");

        setTitle(liste_auteur_title);

    }


    /** création du menu dans l'action bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.slide_enter, R.anim.slide_leave);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}