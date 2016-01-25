package com.example.cesar.rotamapagps;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap gMap;
    GoogleApiClient mGoogleApiClient;

    double lat;
    double lng;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //On récupère les composants graphiques
        gMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

        //On récupère le départ et l'arrivée
        String editDepart = getIntent().getStringExtra("ORIGEM");
        final String editArrivee = getIntent().getStringExtra("DESTINO");

        //Appel de la méthode asynchrone
        createLocationRequest();
        editDepart = String.valueOf(lat) + String.valueOf(lng);
        editDepart = "-8.0599384,-34.8726423";
        new ItineraireTask(this, gMap, editDepart, editArrivee).execute();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected void createLocationRequest() {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
        }

    }


    @Override
    public void onConnected(Bundle bundle) {
        createLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}