package com.citation.emmanuel.citation365;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.citation.emmanuel.citation365.adapters.MyFragmentPagerAdapter;
import com.citation.emmanuel.citation365.fragments.Accueil;
import com.citation.emmanuel.citation365.fragments.Favoris;
import com.citation.emmanuel.citation365.fragments.Recherche;

import java.util.ArrayList;
import java.util.List;


/*
* Point d'entré de l'application, initialisation des différents fragments, du viewpager et du menu de navigation
* */

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    DrawerLayout drawerLayout;
    RelativeLayout drawerPane;
    ListView lvNav;

    List<String> listNavItem;
    List<Fragment> listFragments;

    ActionBarDrawerToggle actionBarDrawerToggle;

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
        actionBar.setDisplayHomeAsUpEnabled(true);

        /** initialisation du volet de navigation */

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        listNavItem = new ArrayList<String>();


        listNavItem.add("Accueil");
        listNavItem.add("Favoris");
        listNavItem.add("Recherche");
        listNavItem.add("Liste d'auteur");
        listNavItem.add("Préférence");

        listFragments = new ArrayList<Fragment>();

        /** Ajout des fragments dans la liste qui sera ajouter dans le viewpager */
        listFragments.add(new Accueil());
        listFragments.add(new Favoris());
        listFragments.add(new Recherche());


        viewPager = (ViewPager) findViewById(R.id.view_pager);

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), listFragments);

        /**  mise en mémoire des pages précédentes et suivante de la page actuelle */
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(myFragmentPagerAdapter);

        viewPager.addOnPageChangeListener(this);

        /** initialisation du titre avec le nom du fragment */
        setTitle(listNavItem.get(0));

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                //Toast.makeText(MainActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();

                switch (menuItem.getItemId()){

                    case R.id.menu_accueil :    viewPager.setCurrentItem(0);
                                                setTitle(menuItem.getTitle());
                                                return true;

                    case R.id.menu_favoris :    viewPager.setCurrentItem(1);
                                                setTitle(menuItem.getTitle());
                                                return true;

                    case R.id.menu_recherche :  viewPager.setCurrentItem(2);
                                                setTitle(menuItem.getTitle());
                                                return true;

                    case R.id.menu_liste_auteur :   Intent intent = new Intent(MainActivity.this, ListeAuteur.class);
                                                    MainActivity.this.startActivity(intent);
                                                    ((Activity)MainActivity.this).overridePendingTransition(R.anim.slide_enter_2, R.anim.slide_leave_2);
                                                    return true;

                    case R.id.menu_preference : Intent intent1 = new Intent(MainActivity.this, ParametreApp.class);
                                                MainActivity.this.startActivity(intent1);
                                                ((Activity)MainActivity.this).overridePendingTransition(R.anim.slide_enter_2, R.anim.slide_leave_2);
                                                return true;

                    case R.id.menu_quit :   finish();
                                            System.exit(0);
                                            return true;

                }
                return true;
            }
        });

    }


    /** création du menu dans l'action bar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
        //return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.action_sync:
                                    recreate();
                                    return true;

            case android.R.id.home:
                                    drawerLayout.openDrawer(GravityCompat.START);
                                    return true;

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

        actionBarDrawerToggle.syncState();
    }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /** listener à la sélection de la page pour changer le titre dans l'entête */
        @Override
        public void onPageSelected(int position) {

            setTitle(listNavItem.get(position));

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }


}
