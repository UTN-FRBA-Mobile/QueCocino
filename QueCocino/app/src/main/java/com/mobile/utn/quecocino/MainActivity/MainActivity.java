package com.mobile.utn.quecocino.MainActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.mobile.utn.quecocino.R;
import com.mobile.utn.quecocino.fragments.FragmentFavoritos;
import com.mobile.utn.quecocino.fragments.FragmentOpciones;
import com.mobile.utn.quecocino.locationManager.GoogleLocationClient;
import com.mobile.utn.quecocino.locationManager.LatLonTranslator;
import com.mobile.utn.quecocino.menu.ItemSlideMenu;
import com.mobile.utn.quecocino.menu.SlidingMenuAdapter;
import com.mobile.utn.quecocino.timer.AlarmUtils;
import com.mobile.utn.quecocino.timer.TimerCountdownFragment;
import com.mobile.utn.quecocino.timer.TimerEditFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private List<ItemSlideMenu> listSliding;
    private SlidingMenuAdapter adapter;
    private ListView listViewSliding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private GoogleLocationClient mGoogleLocationClient;
    private LatLonTranslator mLatLonTranslator;
    private Geocoder geoCod;
    private TextView currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.sliding_menu);

        listViewSliding = (ListView) findViewById(R.id.sl);
        drawerLayout = (DrawerLayout) findViewById(R.id.sliding_menu);
        listSliding = new ArrayList<>();
        currentLocation = (TextView) findViewById(R.id.textView);

        listSliding.add(new ItemSlideMenu(android.R.drawable.ic_menu_help,"Opciones"));
        listSliding.add(new ItemSlideMenu(R.drawable.timer1600, "Temporizador"));
        listSliding.add(new ItemSlideMenu(R.drawable.unnamed, "Favoritos"));

        mGoogleLocationClient = new GoogleLocationClient();
        mGoogleLocationClient.setApiClient(new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mGoogleLocationClient)
                .addOnConnectionFailedListener(mGoogleLocationClient)
                .build());
        mGoogleLocationClient.setMainActivity(this);
        mGoogleLocationClient.apiConnect();

        mLatLonTranslator = new LatLonTranslator();
        geoCod = new Geocoder(MainActivity.this, Locale.getDefault());
        mLatLonTranslator.setGeocoder(geoCod);


        adapter = new SlidingMenuAdapter(this, listSliding);
        listViewSliding.setAdapter(adapter);

        currentLocation.setText("Obteniendo ubicación actual..");

        setTitle(listSliding.get(0).getTitulo());

        listViewSliding.setItemChecked(0, true);

        drawerLayout.closeDrawer(listViewSliding);

        replaceFragment(0);

        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                setTitle(listSliding.get(position).getTitulo());
                listViewSliding.setItemChecked(position, true);
                replaceFragment(position);
                drawerLayout.closeDrawer(listViewSliding);
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed){

            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceStated){
        super.onPostCreate(savedInstanceStated);
        actionBarDrawerToggle.syncState();
    }

    private void replaceFragment(int pos){
        Fragment fragment = null;
        switch (pos){
            case 0:
                fragment = new FragmentOpciones();
                break;
            case 1:
                if(AlarmUtils.hasAlarms(this)){
                    fragment = new TimerCountdownFragment();
                } else {
                    fragment = new TimerEditFragment();
                }
                break;
            case 2:
                fragment = new FragmentFavoritos();
                break;
            default:
                fragment = new FragmentFavoritos();
                break;
        }

        if(fragment != null){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_content, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public void onLocationChanged(Location location) {
        currentLocation.setText("Ubicación actual: " + mLatLonTranslator.reverseGeocodeRegion(location.getLatitude(),location.getLongitude()));
        mGoogleLocationClient.apiDisconnect();
    }

}
