package com.citation.emmanuel.citation365;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.citation.emmanuel.citation365.adapters.MyFragmentPagerAdapter;
import com.citation.emmanuel.citation365.fragments.ListeAuteurFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by emmanuel on 14/10/2015.
 */
public class ListeAuteur extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private String url_auteur = null;

    private ViewPager viewPager = null;

    private List<Fragment> listFragments = null;

    private MyFragmentPagerAdapter myFragmentPagerAdapter = null;

    private ArrayList<String> listItem = null;

    public ListeAuteur(){
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.liste_auteur);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_liste_auteur);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.listItem = new ArrayList<String>();

        listItem.add("Liste des auteurs");
        listItem.add("Liste des auteurs");

        listFragments = new ArrayList<Fragment>();

        /** Ajout des fragments dans la liste qui sera ajouter dans le viewpager */

        this.myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), this.listFragments);

        ListeAuteurFragment liste_auteur = new ListeAuteurFragment();

        this.listFragments.add(liste_auteur);

        viewPager = (ViewPager) findViewById(R.id.view_pager_liste_auteur);
        viewPager.addOnPageChangeListener(this);

        /**  mise en mémoire des pages précédentes et suivante de la page actuelle */
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(myFragmentPagerAdapter);

        /** initialisation du titre avec le nom du fragment */
        setTitle(listItem.get(0));
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        setTitle(this.listItem.get(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}