package com.v3.nrd.nrdv3;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //criar os objetos
    private Button btnColetor;
    private Button btnDoador;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    String fbJsonObjToString;
    JSONObject jsonObj;
    private RequestQueue requestQueue;
    String id;
    String tipo;
    ProgressDialog mProgressDialog;




//    protected JSONObject fbJsonObj = new JSONObject(fbJsonObjToString);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                Intent it = new Intent(this, ActColetor.class);

//                mProgressDialog.setTitle(getString(R.string.lbl_loading));
//                mProgressDialog.setCancelable(false);
//                mProgressDialog.show();

//                ProcessData p = new ProcessData();
//                p.execute();

                //2 parametros(a classe que está chamando,a classe que quero chamar)
//              enviar para o servidor id Coletor
                tipo = "coletor";

                try {
                    jsonObj.put("tipo",tipo);
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

//                mProgressDialog.setTitle(getString(R.string.lbl_loading));
//                mProgressDialog.setCancelable(false);
//                mProgressDialog.show();

                //2 parametros(a classe que está chamando,a classe que quero chamar)
//              enviar para o servidor id do doador
                tipo = "doador";

                try {
                    jsonObj.put("tipo",tipo);
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

    public class ProcessData extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            //2 parametros(a classe que está chamando,a classe que quero chamar)
//              enviar para o servidor id Coletor
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    "http://172.28.144.181:5000/api/users",
                    jsonObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(MainActivity.this, "Deu certo no MainActivity", Toast.LENGTH_LONG).show();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
            return null;
        }
    }
}