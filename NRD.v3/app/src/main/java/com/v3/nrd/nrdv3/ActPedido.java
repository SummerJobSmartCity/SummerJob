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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

//implementar o mapa e os botões

public class ActPedido extends AppCompatActivity
        implements
            GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
            LocationListener {


    private Button btnAceitar;
    private Button btnRecusar1;

//  receber lat e lng do doador informado pelo servidor
//    receber dados do doador para mostrar na tela, informado pelo servidor
    public double lat_coletor;
    public double lng_coletor;
    public String origem;
    public String destino; //informado pelo servidor, posição do doador


    GoogleApiClient mGoogleApiClient;
    GoogleMap mMap;
    Marker mMarkerAtual;
    String fbJsonObjToString;
    JSONObject jsonObj;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("ENTROU ONCREATE:       ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pedido);


        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");
        System.out.println("STRING NO BUSCA COLETOR ======================>   " + fbJsonObjToString);

        try {
            jsonObj = new JSONObject(fbJsonObjToString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnAceitar = (Button) findViewById(R.id.btnAceitar);
        btnAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActPedido.this, ActAceitar.class);

                try {
                    jsonObj.put("latitude", lat_coletor);
                    jsonObj.put("longitude", lng_coletor);
//                    jsonObj.put("tipo",tipo);
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
                                Toast.makeText(ActPedido.this, "Enviado posição do doador para servidor", Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                );

                it.putExtra("fbJsonObj", jsonObj.toString());
                it.putExtra("latlng_doador", destino);
                startActivity(it);
            }
        });

        btnRecusar1 = (Button) findViewById(R.id.btnRecusar1);
        btnRecusar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActPedido.this, ActColetor.class);
                it.putExtra("fbJsonObj", jsonObj.toString());
                startActivity(it);
                //buscar doador
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map2);
        mMap = mapFragment.getMap();

        //origem = "-8.0599384,-34.8726423";
        destino = "-8.049414,-34.881700";
    }

    @Override
    protected void onResume() {
        System.out.println("ENTROU ONRESUME:       ");
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        System.out.println("ENTROU ONPAUSE:       ");
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    protected void createLocationRequest() {
        System.out.println("ENTROU CREATELOCATIONREQUEST:       ");

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
                        new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()))
        );

        lat_coletor = lastKnownLocation.getLatitude();
        lng_coletor = lastKnownLocation.getLongitude();
        origem = String.valueOf(lastKnownLocation.getLatitude()) + "," + String.valueOf(lastKnownLocation.getLongitude());

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        System.out.println("VAI CHAMAR ITINERAIRETASK:       ");
        new ItineraireTask(this, mMap, origem, destino).execute();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("ENTROU ONCONNECTED:       ");
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
        TextView txt = (TextView)findViewById(R.id.textView1);
        try {
            txt.setText("Nome do Json: " + jsonObj.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("ENTROU ONLOCATIONCHANGED:       ");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        mMarkerAtual.setPosition(latLng);
    }

}