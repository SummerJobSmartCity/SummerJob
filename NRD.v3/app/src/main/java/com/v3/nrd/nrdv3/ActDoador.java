package com.v3.nrd.nrdv3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;


public class ActDoador extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Button btnSolicitar;
    private Button btnDesistir;
    private RequestQueue requestQueue;
    public double lat;
    public double lng;

    GoogleApiClient mGoogleApiClient;
    GoogleMap mMap;
    Marker mMarkerAtual;
    JSONObject jsonObj;
    String fbJsonObjToString;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_doador);
        requestQueue = Volley.newRequestQueue(this);

        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");

        btnDesistir = (Button) findViewById(R.id.btnDesistir);
        btnDesistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActDoador.this, MainActivity.class);
                try {
                    jsonObj = new JSONObject(fbJsonObjToString);
                    it.putExtra("fbJsonObj",jsonObj.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(it);
                finish();
//                int pid = android.os.Process.myPid();
//                android.os.Process.killProcess(pid);
            }
        });

        btnSolicitar = (Button) findViewById(R.id.btnSolicitar);
        btnSolicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActDoador.this, ActResultado.class);

                try {
                    jsonObj = new JSONObject(fbJsonObjToString);
                    jsonObj.put("latitude", lat );
                    jsonObj.put("longitude", lng );
//                    jsonObj.put("avaliarColetor", "0");
//                    jsonObj.put("avaliarDoador", "0");
                    id = jsonObj.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.POST,
                        "http://172.28.144.181:5000/api/users/" + id,
                        jsonObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(ActDoador.this, "Enviando localização ao Servidor", Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                );
                requestQueue.add(jsonObjectRequest2);

                try {
                    jsonObj = new JSONObject(fbJsonObjToString);
                    jsonObj.put("latitude", lat );
                    jsonObj.put("longitude", lng );
//                    jsonObj.put("avaliarColetor", "0");
//                    jsonObj.put("avaliarDoador", "0");
                    id = jsonObj.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final JsonObjectRequest jsonObjectRequest3 = new JsonObjectRequest(Request.Method.POST,
                        "http://172.28.144.181:5000/api/postColeta",
                        jsonObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
//                                Toast.makeText(ActDoador.this, "Enviado post ao servidor", Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                );
                requestQueue.add(jsonObjectRequest3);

                it.putExtra("fbJsonObj", jsonObj.toString());
                startActivity(it);
                finish();
//                int pid = android.os.Process.myPid();
//                android.os.Process.killProcess(pid);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
    }

    public void onBackPressed(){
        Intent it = new Intent(ActDoador.this, MainActivity.class);
        try {
            jsonObj = new JSONObject(fbJsonObjToString);
            jsonObj.put("tipo","");
            jsonObj.put("latitude", "");
            jsonObj.put("longitude", "");
//            jsonObj.put("gcmToken", "");
//            jsonObj.put("avaliarColetor", "0");
//            jsonObj.put("avaliarDoador", "0");
            id = jsonObj.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                "http://172.28.144.181:5000/api/users/" + id,
                jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(ActDoador.this, "", Toast.LENGTH_LONG).show();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);

        it.putExtra("fbJsonObj", jsonObj.toString());
        startActivity(it);
        finish();
//        int pid = android.os.Process.myPid();
//        android.os.Process.killProcess(pid);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        mMarkerAtual = mMap.addMarker(
                new MarkerOptions().title("Local atual").icon(icon).position(
                        new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
        );

        lat = lastKnownLocation.getLatitude();
        lng = lastKnownLocation.getLongitude();


        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        createLocationRequest();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
        mMarkerAtual.setPosition(latLng);
    }
}