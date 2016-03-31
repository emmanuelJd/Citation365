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
import com.citation.emmanuel.citation365.fragments.AuteurFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by emmanuel on 14/10/2015.
 */
public class Auteur extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private String url_auteur = null;

    private ViewPager viewPager = null;

    private List<Fragment> listFragments = null;

    private MyFragmentPagerAdapter myFragmentPagerAdapter = null;

    private ArrayList<String> listItem = null;

    public Auteur(){
        super();
    }

    public Auteur (String url_auteur){
        super();

        this.url_auteur = url_auteur;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fiche_auteur);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_auteur);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        String url_auteur = getIntent().getStringExtra("URL_AUTEUR");

        this.listItem = new ArrayList<String>();

        listItem.add("Auteur");
        listItem.add("Citation de l'auteur");

        listFragments = new ArrayList<Fragment>();

        /** Ajout des fragments dans la liste qui sera ajouter dans le viewpager */

        this.myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), listFragments);

        AuteurFragment frag_auteur = new AuteurFragment();

        frag_auteur.setUrl_auteur(url_auteur);
        frag_auteur.setMyFragmentPagerAdapter(this.myFragmentPagerAdapter);
        frag_auteur.setListFragments(this.listFragments);



        this.myFragmentPagerAdapter.notifyDataSetChanged();

        viewPager = (ViewPager) findViewById(R.id.view_pager_auteur);
        viewPager.addOnPageChangeListener(this);

        frag_auteur.setViewpager(this.viewPager);
        this.listFragments.add(frag_auteur);

        /**  mise en mémoire des pages précédentes et suivante de la page actuelle */
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(myFragmentPagerAdapter);

        /** initialisation du titre avec le nom du fragment */
        setTitle(listItem.get(0));
    }

    /** création du menu dans l'action bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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