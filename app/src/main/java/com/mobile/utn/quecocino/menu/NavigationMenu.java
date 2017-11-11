package com.mobile.utn.quecocino.menu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.detailrecipe.DetailRecipe;
import com.mobile.utn.quecocino.fragments.OnFragmentInteractionCollapse;
import com.mobile.utn.quecocino.ingredientSearch.IngredientSearchFragment;
import com.mobile.utn.quecocino.locationManager.GoogleLocationClient;
import com.mobile.utn.quecocino.locationManager.LatLonTranslator;
import com.mobile.utn.quecocino.model.Recipe;
import com.mobile.utn.quecocino.recipegallery.activities.RecipeGallery;
import com.mobile.utn.quecocino.recipes.RecipeUtils;
import com.mobile.utn.quecocino.recipes.filter.Filter;
import com.mobile.utn.quecocino.recipes.filter.FiltersFragment;
import com.mobile.utn.quecocino.recipes.filter.TrivialFilter;
import com.mobile.utn.quecocino.recipes.results.RecipesResultsFragment;
import com.mobile.utn.quecocino.timer.AlarmUtils;
import com.mobile.utn.quecocino.timer.TimerCountdownFragment;
import com.mobile.utn.quecocino.timer.TimerEditFragment;
import com.mobile.utn.quecocino.timer.TimerRingFragment;
import com.mobile.utn.quecocino.utils.WithoutConnectionFragment;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class NavigationMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    OnFragmentInteractionCollapse,
                            LocationListener {

    //For getLocation
    private GoogleLocationClient mGoogleLocationClient;
    private LatLonTranslator mLatLonTranslator;
    private Geocoder geoCod;

    private DrawerLayout drawerLayout;
    private ImageView imageView;
    private CollapsingToolbarLayout collapsingToolbar;
    private View viewGradient;
    private Menu menu;

    private Filter filter;
    
    private Recipe currentRecipe;

    private String location;

    private String lastPage = "search";

    private FiltersFragment filterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        filter = new TrivialFilter();

        //set the fragment initially
        String fragmentStr = "";
        Bundle args = getIntent().getExtras();
        if(args!=null) {
            fragmentStr = (args.containsKey("fragment")) ? args.getString("fragment") : "";
        }

        Fragment fragment;
        switch (fragmentStr) {
            case "timerRing":
                fragment = new TimerRingFragment();
                break;
            case "timerCountdown":
                fragment = new TimerCountdownFragment();
                break;
            default:
                fragment = new IngredientSearchFragment();
                break;
        }

        if(!isDeviceConnected()){
            fragment = new WithoutConnectionFragment();
        }

        fragment.setArguments(args);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.navigation_container, fragment);
        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.navigation_toolbar);
        setSupportActionBar(toolbar);

        //for collapsing toolbar
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.navigation_collapsing_toolbar);
        imageView = (ImageView) findViewById(R.id.navigation_mainImage);
        viewGradient = (View) findViewById(R.id.navigation_gradient);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void syncFrags() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.navigation_container);
        if (fragment instanceof DetailRecipe) {
            enableCollapse();
        }else{
            disableCollapse();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        syncFrags();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        this.menu = menu;
        showMenuItems(Arrays.asList(R.id.navigation_action_buscarRecetas,
                                        R.id.navigation_action_favoritos,
                                            R.id.navigation_action_timers));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        if (id == R.id.navigation_action_buscarRecetas) {
            fragment = new IngredientSearchFragment();
        } else if (id == R.id.navigation_action_favoritos) {
            fragment = new RecipesResultsFragment();
            Bundle args = new Bundle();
            args.putString("page","favorites");
            fragment.setArguments(args);
        } else if (id == R.id.navigation_action_timers) {
            if(AlarmUtils.hasAlarms(this)){
                fragment = new TimerCountdownFragment();
            } else {
                fragment = new TimerEditFragment();
            }
        } else if (id == R.id.detailRecipe_buttonFavorite) {

            if(RecipeUtils.isFavorite(this, currentRecipe.getIdRecipe()))
                RecipeUtils.removeFavorite(this, currentRecipe.getIdRecipe());
            else
                RecipeUtils.addFavorite(this, currentRecipe.getIdRecipe());

            this.refreshFavoriteIcon();

        } else if(id == R.id.detailRecipe_buttonGallery) {

            Intent intent = new Intent(this, RecipeGallery.class);
            intent.putExtra("idReceta", currentRecipe.getIdRecipe());
            intent.putExtra("titleReceta",currentRecipe.getTitle());
            startActivity(intent);

        } else if(id == R.id.recipeResults_buttonFilters){
            fragment = FiltersFragment.newInstance();
        }

        if(fragment != null){
            fragmentTransaction.replace(R.id.navigation_container, fragment);
            fragmentTransaction.addToBackStack(null);
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
            fragment = new IngredientSearchFragment();
        } else if (id == R.id.navigation_favorites) {
            fragment = new RecipesResultsFragment();
            Bundle args = new Bundle();
            args.putString("page","favorites");
            fragment.setArguments(args);
        } else if(id == R.id.navigation_populars){
            fragment = new RecipesResultsFragment();
            Bundle args = new Bundle();
            args.putString("page","popular");
            fragment.setArguments(args);
        } else if (id == R.id.navigation_timers) {
            if(AlarmUtils.hasAlarms(this)){
                fragment = new TimerCountdownFragment();
            } else {
                fragment = new TimerEditFragment();
            }
        }

        if(fragment != null){
            fragmentTransaction.replace(R.id.navigation_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void disableCollapse() {
        imageView.setVisibility(View.GONE);
        viewGradient.setVisibility(View.GONE);
        collapsingToolbar.setTitleEnabled(false);
    }

    @Override
    public void enableCollapse() {
        imageView.setVisibility(View.VISIBLE);
        viewGradient.setVisibility(View.VISIBLE);
        collapsingToolbar.setTitleEnabled(true);
    }
  
    public void createLocations(FiltersFragment fragment) {
  
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

        this.filterFragment = fragment;

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = mLatLonTranslator.reverseGeocodeRegion(location.getLatitude(), location.getLongitude());
        mGoogleLocationClient.apiDisconnect();
        if (filterFragment != null){
            filterFragment.gpsLoaded();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 99: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mGoogleLocationClient.requestLocationUpdates();
                } else {
                    filterFragment.gpsLoaded();
                }
                return;
            }

        }
    }

    public void refreshFavoriteIcon() {
        MenuItem favoriteButton = this.getMenuItem(R.id.detailRecipe_buttonFavorite);
        if(RecipeUtils.isFavorite(this, currentRecipe.getIdRecipe()))
            favoriteButton.setIcon(R.drawable.detailrecipe_favorite_set);
        else
            favoriteButton.setIcon(R.drawable.detailrecipe_favorite_unset);
    }

    public boolean isDeviceConnected() {

        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networks = connec.getAllNetworkInfo();

        for (int i = 0; i < networks.length; i++) {
            if (networks[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    public void showMenuItems(List<Integer> itemsId) {
        if(menu != null){
            for(int i = 0; i < menu.size(); i++){

                MenuItem item = menu.getItem(i);

                if(itemsId.contains(item.getItemId())){
                    item.setVisible(true);
                }else{
                    item.setVisible(false);
                }

            }
        }
    }

    public MenuItem getMenuItem(int idItem){
        return menu.findItem(idItem);
    }

    public Filter getFilter() {
        return filter;
    }
    public void setFilter(Filter filter) {
        this.filter = filter;
    }
    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }
    public void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public CollapsingToolbarLayout getCollapsingToolbar() {
        return collapsingToolbar;
    }
    public String getLastPage() {
        return lastPage;
    }
    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }
    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }
}
