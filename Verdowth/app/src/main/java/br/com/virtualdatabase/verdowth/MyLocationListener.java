package br.com.virtualdatabase.verdowth;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Susiane on 23/11/2016.
 */

public class MyLocationListener implements LocationListener {
    private Context contex;

    public MyLocationListener(Context contex) {
        this.contex = contex;
    }

    @Override
    public void onLocationChanged(Location location) {

        /*editLocation.setText("");
        pb.setVisibility(View.INVISIBLE);*/
        Toast.makeText(
                contex,
                "Location changed: Lat: " + location.getLatitude() + " Lng: "
                        + location.getLongitude(), Toast.LENGTH_SHORT).show();

        String longitude = "Longitude: " + location.getLongitude();
        //Log.v(TAG, longitude);
        String latitude = "Latitude: " + location.getLatitude();
        //Log.v(TAG, latitude);

        String cityName = null;
        Geocoder gcd = new Geocoder(contex, Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = gcd.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            if (addresses.size() > 0) {
                System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                + cityName;
        //editLocation.setText(s);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
