package com.mobile.utn.quecocino.locationManager;

import android.location.Address;
import android.location.Geocoder;

import java.util.List;

/**
 * Created by Martin on 30/09/2017.
 */

public class LatLonTranslator {
    private Geocoder geoCod;

    public void setGeocoder(Geocoder geoCod){
        this.geoCod = geoCod;
    }

    public String reverseGeocodeRegion (double latitud, double longitud){
        List<Address> listAddress;
        try {
            listAddress = geoCod.getFromLocation(latitud, longitud, 1);
            if (listAddress.size() > 0) {
                return listAddress.get(0).getAdminArea()+ ", "+ listAddress.get(0).getCountryCode();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


}
