package com.v3.nrd.nrdv3;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
    implements
        View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Button btnColetor;
    private Button btnDoador;
    private GoogleApiClient client;
    private RequestQueue requestQueue;

    double lat;
    double lng;

    GoogleApiClient mGoogleApiClient;
    JSONObject jsonObj;
    String fbJsonObjToString;
    String id;
    String tipo;
    String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", "");
        System.out.println("TOKEN NA MAIN!!! =============>    " + token);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");
        requestQueue = Volley.newRequestQueue(this);

        try {
            jsonObj = new JSONObject(fbJsonObjToString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnColetor = (Button) findViewById(R.id.btnColetor);
        btnDoador = (Button) findViewById(R.id.btnDoador);

        btnColetor.setOnClickListener(this);
        btnDoador.setOnClickListener(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onStart() {
        super.onStart();

        System.out.println("AQUIIIII       :        " + fbJsonObjToString);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.v3.nrd.nrdv3/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.v3.nrd.nrdv3/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnColetor:
                Intent it = new Intent(this, ActPedido.class);

                tipo = "coletor";

                try {
                    jsonObj.put("tipo",tipo);
                    jsonObj.put("latitude", lat );
                    jsonObj.put("longitude", lng );
                    jsonObj.put("gcmToken", token);
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
                                Toast.makeText(MainActivity.this, "Atualizando usuario -> coletor", Toast.LENGTH_LONG).show();
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
                break;
            case R.id.btnDoador:
                Intent it2 = new Intent(this, ActDoador.class);

                tipo = "doador";

                try {
                    jsonObj.put("tipo",tipo);
                    jsonObj.put("latitude", lat );
                    jsonObj.put("longitude", lng );
                    jsonObj.put("gcmToken", token);
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
                                Toast.makeText(MainActivity.this, "Atualizando usuario -> doador", Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                );
                requestQueue.add(jsonObjectRequest2);
                it2.putExtra("fbJsonObj",jsonObj.toString());
                startActivity(it2);
                break;
        }
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

        lat = lastKnownLocation.getLatitude();
        lng = lastKnownLocation.getLongitude();


        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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