package io.github.alexhagen.scavenger_hunt;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class external_clue extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_clue);

        // TODO: get current position
        // TODO: get current heading
        // TODO: remove any touch callbacks so you cant move map
        // TODO: measure distance from given coordinates
        // TODO: measure heading to given coordinates
        // TODO: plot current position, current heading, heading to given coords, and distance to given coords on map

        /*
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = service.getBestProvider(criteria, false);
        Location location = service.getLastKnownLocation(provider);
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());*/
        Log.d("MAPPING", "hi");
    }

    // TODO: update plot on positionChange

}
