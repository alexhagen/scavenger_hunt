package io.github.alexhagen.scavenger_hunt;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class external_clue extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, SensorEventListener {

    private GoogleApiClient mGoogleApiClient;
    public static final String TAG = external_clue.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    double lat;
    double lon;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor geomagnetic;
    float[] mGravity;
    float[] mGeomagnetic;
    float targetBearing;
    Location endingLocation;
    Location currloc;
    Marker mark;
    Polyline pline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_clue);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        geomagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        lat = extras.getDouble("CLUE_LAT");
        lon = extras.getDouble("CLUE_LON");
        String clue_text = extras.getString("CLUE_TEXT");
        TextView tv = (TextView) findViewById(R.id.external_clue_text);
        tv.setText(clue_text);
        Button fb = (Button) findViewById(R.id.external_clue_finished);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                external_clue.this.finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();
    }

    @Override
    public void onConnected(Bundle bundle){
        Log.i(TAG, "Location services connected.");
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i){
        Log.i(TAG, "location services suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, geomagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        mSensorManager.unregisterListener(this);
    }

    private void handleNewLocation(Location location){

        Log.d(TAG, location.toString());
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);
        currloc = location;

        MarkerOptions options = new MarkerOptions()
                .position(latLng);
        if (mark != null) {
            mark.remove();
        }
        mark = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

        GeomagneticField geoField = new GeomagneticField(
                Double.valueOf(location.getLatitude()).floatValue(),
                Double.valueOf(location.getLongitude()).floatValue(),
                Double.valueOf(location.getAltitude()).floatValue(),
                System.currentTimeMillis()
        );
        //heading += geoField.getDeclination();
        //heading = bearing - (bearing + heading);

        // get our l
        //Get the target location
        endingLocation = new Location("ending point");
        endingLocation.setLatitude(lat);
        endingLocation.setLongitude(lon);

        //Find the Bearing from current location to next location
        targetBearing = location.bearingTo(endingLocation);
        TextView tv = (TextView) findViewById(R.id.distance);
        tv.setText(String.format("%.2f mi.", location.distanceTo(endingLocation)/1609.344));
        Log.d("TARGET_BEARING", String.format("%f", targetBearing));
        PolylineOptions line=
                new PolylineOptions().add(new LatLng(endingLocation.getLatitude(),
                                                     endingLocation.getLongitude()),
                                          new LatLng(currloc.getLatitude(),
                                                     currloc.getLongitude()))
                        .width(5).color(R.color.colorAccent);
        if (pline != null) {
            pline.remove();
        }
        pline = mMap.addPolyline(line);
    }


    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    private void setUpMap() {
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimut = (float) Math.toDegrees(orientation[0]);
                //float azimuthInDegress = (float) (Math.toDegrees(orientation[0])+360)%360;
                targetBearing = (float) 5.0 * Math.round(azimut/5);// - currloc.bearingTo(endingLocation);
                Log.d("BEARING_CHANGE", String.format("Bearing: %f, %f", targetBearing, azimut));
                double currentLatitude = currloc.getLatitude();
                double currentLongitude = currloc.getLongitude();
                LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.builder().target(latLng).zoom(17).bearing(targetBearing).build()));
            }
        }
    }

}
