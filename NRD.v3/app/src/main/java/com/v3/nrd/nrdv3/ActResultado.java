package com.v3.nrd.nrdv3;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

public class ActResultado extends AppCompatActivity {

    private Button btnColetaEfetuada;
    private RequestQueue requestQueue;

    String fbJsonObjToString;
    JSONObject jsonObj;

    String id;
    String tipo;

    private static String mNomeColetor = "";
    private static String idcoletor = "";

    UpdateActResultado mReceiver;
    private static boolean flag = false;
    private TextView mNomeColetorTextView;
    ProgressDialog mProgressDialog;
    private static String mComando = "";


    public class ProcessDataResultado extends AsyncTask<Void, Void, Void> {

        public ProcessDataResultado(){
            mProgressDialog = new ProgressDialog(ActResultado.this);
            mProgressDialog.setTitle("Buscando coletor");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent it = new Intent(ActResultado.this, MainActivity.class);

                    try {
                        tipo = "";
                        jsonObj.put("tipo", tipo);
                        jsonObj.put("avaliarColetor", "0");
                        jsonObj.put("avaliarDoador", "0");
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
                                    Toast.makeText(ActResultado.this, "Atualizando usuario -> NULL. Coleta cancelada.", Toast.LENGTH_LONG).show();
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
                    // Use either finish() or return() to either close the activity or just the dialog
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
            mNomeColetorTextView.setText(mNomeColetor);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_resultado);

        mReceiver = new UpdateActResultado();
        registerReceiver(mReceiver, new IntentFilter("nrd.UpdateActResultado")); // Register receiver

        requestQueue = Volley.newRequestQueue(this);

        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");
        System.out.println("STRING NO RESULTADO ======================>   " + fbJsonObjToString);

        try {
            jsonObj = new JSONObject(fbJsonObjToString);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        btnColetaEfetuada = (Button) findViewById(R.id.btnColetaEfetuada);
        btnColetaEfetuada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActResultado.this, AvaliarColetor.class);
                it.putExtra("fbJsonObj",jsonObj.toString());
                startActivity(it);
            }
        });

        mNomeColetorTextView = (TextView) findViewById(R.id.nome_coletor);

    }

    public void onBackPressed(){
        Intent it = new Intent(ActResultado.this, MainActivity.class);
        try {
            jsonObj = new JSONObject(fbJsonObjToString);
            it.putExtra("fbJsonObj",jsonObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(it);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new ProcessDataResultado().execute();
    }

    public class UpdateActResultado extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            mComando = intent.getStringExtra("comando");
            if(mComando.equals("notfound")){
                mProgressDialog.setTitle("Coletor não encontrado.");
                mProgressDialog.setMessage("Tente mais tarde!");
            }
            else {
                System.out.println("Doador SAINDO DO PROGRESS DIALOG");
                flag = true;
                mNomeColetor = intent.getStringExtra("nomecoletor");
                idcoletor = intent.getStringExtra("idcoletor");

                try {
                    jsonObj.put("idcoletor", idcoletor );
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
                                Toast.makeText(ActResultado.this, "Atualizando posição -> coletor", Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                );
                requestQueue.add(jsonObjectRequest);

                Log.d("UpdateActResultado", "COLETOR OLAOLAOLA");
            }
        }
    }

}
