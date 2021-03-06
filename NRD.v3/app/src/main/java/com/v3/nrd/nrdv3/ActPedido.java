package com.v3.nrd.nrdv3;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
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

//implementar o mapa e os botões

public class ActPedido extends AppCompatActivity
    implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private Button btnColetaEfetuada;
    private static String mNomeDoador = "";
    private static String iddoador = "";
    private static String mEmail = "";
    private static String mComando = "";
    private static boolean flag = false;
    private TextView mNomeDoadorTextView;
    private TextView mEmailDoador;
    private RequestQueue requestQueue;

    public double lat_coletor;
    public double lng_coletor;
    public String origem;
    public String destino;                  //informado pelo servidor, posição do doador
    public String ip = "172.28.145.152";


    GoogleApiClient mGoogleApiClient;
    GoogleMap mMap;
    Marker mMarkerAtual;
    JSONObject jsonObj;
    JSONObject jsonObjDados;
    String fbJsonObjToString;
    String id;
    String tipo;
    UpdateActPedido mReceiver;
    Location lastKnownLocation;


    public class ProcessData extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;

        public ProcessData(){
            mProgressDialog = new ProgressDialog(ActPedido.this);
            mProgressDialog.setTitle(getString(R.string.lbl_loading));
            mProgressDialog.setCancelable(false);
            mProgressDialog.setButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent it= new Intent(ActPedido.this,MainActivity.class);

                    try {
                        tipo = null;
                        jsonObj.put("tipo",tipo);
                        id = jsonObj.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.POST,
                            "http://"+ip+":5000/api/users/" + id,
                            jsonObj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
//                                    Toast.makeText(ActPedido.this, "Atualizando usuario -> doador", Toast.LENGTH_LONG).show();
                                }
                            },

                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                }
                            }
                    );
                    requestQueue.add(jsonObjectRequest2);

                    it.putExtra("fbJsonObj", jsonObj.toString());
                    startActivity(it);
                    finish();
                    return;
                }
            });
        }

        @Override
        protected void onPreExecute(){
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while(!flag);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            flag = false;
            mProgressDialog.dismiss();
            mNomeDoadorTextView.setText(mNomeDoador);
            mEmailDoador.setText(mEmail);

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pedido);

        requestQueue = Volley.newRequestQueue(this);

        mReceiver = new UpdateActPedido();
        registerReceiver(mReceiver, new IntentFilter("nrd.UpdateActPedido")); // Register receiver


        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");

        try {
            jsonObj = new JSONObject(fbJsonObjToString);
            jsonObjDados = new JSONObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnColetaEfetuada= (Button) findViewById(R.id.btnColetaEfetuada);
        btnColetaEfetuada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActPedido.this, AvaliarDoador.class);
                it.putExtra("fbJsonObj", jsonObj.toString());
                it.putExtra("jsonObjDados",jsonObjDados.toString());
                startActivity(it);
                finish();
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

        mNomeDoadorTextView = (TextView) findViewById(R.id.nome_doador);
        mEmailDoador = (TextView) findViewById(R.id.telefone_doador);

    }

    public void onBackPressed(){
        Intent it = new Intent(ActPedido.this, MainActivity.class);
        try {
            jsonObj = new JSONObject(fbJsonObjToString);
            it.putExtra("fbJsonObj",jsonObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        it.putExtra("fbJsonObj", jsonObj.toString());
        startActivity(it);
        finish();
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
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new ProcessData().execute();
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
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        BitmapDescriptor icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
//        mMarkerAtual = mMap.addMarker(
//                new MarkerOptions().title("Local atual").icon(icon).position(
//                        new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
//        );
        System.out.println("Adicionando MARKER               CREATE LOCATION REQUESTE");


        lat_coletor = lastKnownLocation.getLatitude();
        lng_coletor = lastKnownLocation.getLongitude();
        origem = String.valueOf(lastKnownLocation.getLatitude()) + "," + String.valueOf(lastKnownLocation.getLongitude());

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        new ItineraireTask(this, mMap, origem, destino).execute();
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
//        TextView txt = (TextView)findViewById(R.id.textView1);
//        try {
//            txt.setText("Nome: " + jsonObj.getString("name"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        if(mMarkerAtual == null){
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.mipmap.pin);
            mMarkerAtual = mMap.addMarker(
                    new MarkerOptions().title("Local atual").icon(icon).position(
                            new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()))
            );
        }else {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            mMarkerAtual.setPosition(latLng);
            System.out.println("Adicionando MARKER               LOCATION CHANGE");
        }
    }

    public class UpdateActPedido extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            mComando = intent.getStringExtra("comando");

            if(mComando.equals("update")){
                lat_coletor = lastKnownLocation.getLatitude();
                lng_coletor = lastKnownLocation.getLongitude();

                try {
                    jsonObj.put("latitude", lat_coletor );
                    jsonObj.put("longitude", lng_coletor );
//                    jsonObj.put("avaliarColetor", "0");
//                    jsonObj.put("avaliarDoador", "0");
                    id = jsonObj.getString("id");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "http://"+ip+":5000/api/users/" + id,
                        jsonObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(ActPedido.this, "Pedido de coleta encontrado", Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                );
                requestQueue.add(jsonObjectRequest);
            }
            else{
                flag = true;
                mNomeDoador = intent.getStringExtra("nomedoador");
                iddoador = intent.getStringExtra("iddoador");
                mEmail = intent.getStringExtra("emaildoador");
                destino = intent.getStringExtra("latitude") + "," + intent.getStringExtra("longitude");


                try {
                    jsonObj.put("iddoador", iddoador );
                    id = jsonObj.getString("id");

                    jsonObjDados.put("nomeDoador", mNomeDoador);
                    jsonObjDados.put("emailDoador", mEmail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "http://"+ip+":5000/api/users/" + id,
                        jsonObj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(ActPedido.this, "Obtendo dados do Doador", Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                );
                requestQueue.add(jsonObjectRequest);
                createLocationRequest();
            }
        }
    }

}