package uk.ac.shef.oak.com4510.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import uk.ac.shef.oak.com4510.MapViewModel;
import uk.ac.shef.oak.com4510.database.LocAndSensorData;

public class MyMap{

    private static final int ACCESS_FINE_LOCATION = 123;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private Context context;
    private Barometer barometer;
    private boolean started;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private ArrayList<LatLng> latLngs  = new ArrayList<LatLng>();
    private int tripNumber;
    private MapViewModel mapViewModel;

    public MyMap(Context context,Barometer barometer,MapViewModel mapViewModel){

        this.context = context;
        this.barometer = barometer;
        this.mapViewModel = mapViewModel;
        started = false;
        tripNumber = 0;
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);
    }

    public void stopLocationUpdates(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        latLngs = new ArrayList<LatLng>();
    }

    public void resume(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        startLocationUpdates();
    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            mCurrentLocation = locationResult.getLastLocation();
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

            if(started) {
                latLngs.add(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
                Log.i("MAP", tripNumber+": new location " + mCurrentLocation.toString());
                Log.i("BARMAP","Barometer"+barometer.toString());
                LocAndSensorData l = new LocAndSensorData(barometer.getLatestValue(),barometer.getLatestAccuracy(),mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),tripNumber);
                mapViewModel.insertOneData(l);
            }
        }
    };

    public void permissionsResult(int requestCode,
                                  String permissions[], int[] grantResults){
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, null /* Looper */);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void setStarted(boolean started) {
        this.started = started;
        if(!started)
            tripNumber++;
    }

    public boolean getStarted(){ return started;}

    public ArrayList<LatLng> getLatLngs() {
        return latLngs;
    }
}
