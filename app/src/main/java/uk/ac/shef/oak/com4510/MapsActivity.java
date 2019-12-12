
package uk.ac.shef.oak.com4510;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import uk.ac.shef.oak.com4510.database.LocAndSensorData;
import uk.ac.shef.oak.com4510.model.Accelerometer;
import uk.ac.shef.oak.com4510.model.Barometer;
import uk.ac.shef.oak.com4510.model.MyMap;

public class MapsActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Button mButtonStart;
    private Button mButtonEnd;
    private Barometer barometer;
    private Accelerometer accelerometer;
    private MyMap map;
    private Marker current_loc_marker;
    private MapViewModel mapViewModel;
    private Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        barometer= new Barometer(this);
        current_loc_marker = null;
        accelerometer= new Accelerometer(this, barometer);

        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mapViewModel.getLocAndSensorDataLiveData().observe(this, new Observer<LocAndSensorData>() {
            @Override
            public void onChanged(@NonNull final LocAndSensorData locAndSensorData) {

                if(locAndSensorData != null) {
                    if (current_loc_marker == null) {
                        current_loc_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(locAndSensorData.getLatitude(), locAndSensorData.getLongitude())));
                    } else {
                        current_loc_marker.setPosition(new LatLng(locAndSensorData.getLatitude(), locAndSensorData.getLongitude()));
                        Log.i("marker",""+locAndSensorData.getLatitude()+" , "+locAndSensorData.getLongitude());
                    }

                    if(map.getStarted()) {
                        ArrayList<LatLng> latLngs = map.getLatLngs();
                        if (polyline == null)
                            polyline = mMap.addPolyline(new PolylineOptions().addAll(latLngs).width(10).color(Color.RED));
                        else
                            polyline.setPoints(latLngs);
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locAndSensorData.getLatitude(), locAndSensorData.getLongitude()), 14.0f));
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map = new MyMap(this, mapFragment,barometer,mapViewModel);

        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.startLocationUpdates();
                map.setStarted(true);

                accelerometer.startAccelerometerRecording();
                if (mButtonEnd != null)
                    mButtonEnd.setEnabled(true);
                mButtonStart.setEnabled(false);
            }
        });
        mButtonStart.setEnabled(true);

        mButtonEnd = (Button) findViewById(R.id.button_end);
        mButtonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.stopLocationUpdates();
                map.setStarted(false);
                accelerometer.stopAccelerometer();
                if (mButtonStart != null)
                    mButtonStart.setEnabled(true);
                mButtonEnd.setEnabled(false);
            }
        });
        mButtonEnd.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.resume();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        map.permissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
}

