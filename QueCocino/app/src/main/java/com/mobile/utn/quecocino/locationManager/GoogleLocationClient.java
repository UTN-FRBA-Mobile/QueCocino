package com.mobile.utn.quecocino.locationManager;

/**
 * Created by Martin on 30/09/2017.
        */

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mobile.utn.quecocino.MainActivity.MainActivity;

public class GoogleLocationClient implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {
    private MainActivity mainActivity;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private static final int PETICION_PERMISO_LOCALIZACION = 99;

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;

    }

    public void setApiClient (GoogleApiClient gApiC) {
        this.mGoogleApiClient = gApiC;
    }

    public void apiConnect () {
        this.mGoogleApiClient.connect();
    }

    public void apiDisconnect () {
        this.mGoogleApiClient.disconnect();
    }
    @Override
    public void onConnected (Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        if (ContextCompat.checkSelfPermission(this.mainActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.mainActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this.mainActivity);
            ActivityCompat.requestPermissions(this.mainActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        }

    }
    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


}
