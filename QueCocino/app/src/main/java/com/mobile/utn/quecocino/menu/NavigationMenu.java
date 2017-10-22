package com.mobile.utn.quecocino.menu;

import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.detailrecipe.DetailRecipe;
import com.mobile.utn.quecocino.fragments.FragmentFavorites;
import com.mobile.utn.quecocino.fragments.FragmentTimer;
import com.mobile.utn.quecocino.locationManager.GoogleLocationClient;
import com.mobile.utn.quecocino.locationManager.LatLonTranslator;
import com.mobile.utn.quecocino.model.Recipe;

import java.util.Locale;

public class NavigationMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DetailRecipe.OnFragmentInteractionListener, LocationListener {

    //For getLocation
    private GoogleLocationClient mGoogleLocationClient;
    private LatLonTranslator mLatLonTranslator;
    private Geocoder geoCod;

    public NavigationView navigationView = null;
    public Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //set the fragment initially
        MainFragment fragment = new MainFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.navigation_container, fragment);
        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        createLocations();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        if (id == R.id.navigation_action_buscarRecetas) {
            fragment = new MainFragment();
        } else if (id == R.id.navigation_action_favoritos) {
            fragment = new FragmentFavorites();
        } else if (id == R.id.navigation_action_timers) {
            fragment = new FragmentTimer();
        }

        if(fragment != null){
            fragmentTransaction.replace(R.id.navigation_container, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        if (id == R.id.navigation_main) {
            fragment = new MainFragment();
        } else if (id == R.id.navigation_favorites) {
            fragment = new FragmentFavorites();
        } else if (id == R.id.navigation_timers) {
            fragment = new FragmentTimer();
        }else if(id == R.id.navigation_detailRecipe){
            fragment = DetailRecipe.newInstance(new Recipe("r1", "Ensalada Caesar", "Franco Pesce", 45, "http://www.buenapetitopr.com/images/recetas/ensaladas/ensalada_caesar_300x200.png", "Oven"));
        }

        if(fragment != null){
            fragmentTransaction.replace(R.id.navigation_container, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void createLocations() {

        mGoogleLocationClient = new GoogleLocationClient();
        mGoogleLocationClient.setApiClient(new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mGoogleLocationClient)
                .addOnConnectionFailedListener(mGoogleLocationClient)
                .build());
        mGoogleLocationClient.setNavigationMenu(this);
        mGoogleLocationClient.apiConnect();

        mLatLonTranslator = new LatLonTranslator();
        geoCod = new Geocoder(NavigationMenu.this, Locale.getDefault());
        mLatLonTranslator.setGeocoder(geoCod);

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}