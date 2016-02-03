package com.v3.nrd.nrdv3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
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

public class AvaliarDoador extends AppCompatActivity {

    private Button btnOk1;
    String fbJsonObjToString;
    JSONObject jsonObj;
    JSONObject jsonObj2;

    String id;

    String avaliarDoador;
    private RatingBar rating_Bar;
    private RequestQueue requestQueue;
    private static String mNomeDoador = "";
    private static String iddoador = "";
    private static String mEmail = "";
    private static String mComando = "";

    private TextView mNomeDoadorTextView;
    private TextView mEmailDoador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliar_doador);

       requestQueue = Volley.newRequestQueue(this);


        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");
        try {
            jsonObj = new JSONObject(fbJsonObjToString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listenerForRatingBar();

        btnOk1=(Button)findViewById(R.id.btnOk1);
        btnOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AvaliarDoador.this, ActPedido.class);
                it.putExtra("fbJsonObj", jsonObj.toString());

                try {
                    iddoador = jsonObj.getString("iddoador");
                    jsonObj2.put("id", jsonObj.getString("id"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "http://172.28.144.181:5000/api/avaliarDoador/" + iddoador,
                        jsonObj2,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(AvaliarDoador.this, "Atualizando avaliação do doador", Toast.LENGTH_LONG).show();
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        }
                );
                requestQueue.add(jsonObjectRequest);


                startActivity(it);
                finish();
            }
        });

        mNomeDoadorTextView = (TextView) findViewById(R.id.nome_doador);
        mEmailDoador = (TextView) findViewById(R.id.telefone_doador);

    }

    public class UpdateActPedido extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {
            mComando = intent.getStringExtra("comando");
            if(mComando.equals("doadorEscolhido")){
                mNomeDoador = intent.getStringExtra("nomedoador");
                mEmail = intent.getStringExtra("emaildoador");

                mNomeDoadorTextView.setText(mNomeDoador);
                mEmailDoador.setText(mEmail);
            }
        }
    }

   public void onBackPressed(){
        Intent it = new Intent(AvaliarDoador.this, MainActivity.class);
        try {
            jsonObj = new JSONObject(fbJsonObjToString);
            it.putExtra("fbJsonObj",jsonObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(it);
        finish();
    }


    public void listenerForRatingBar(){
        rating_Bar=(RatingBar)findViewById(R.id.ratingBar);
        rating_Bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                    @Override
                                                    public void onRatingChanged(RatingBar rating_Bar, float rating, boolean fromUser) {
                                                        avaliarDoador = String.valueOf(rating);
                                                        System.out.println("IMPRIMINDO VALOR DO AVALIARDOADOR==========================>         " + avaliarDoador);
                                                        try {
                                                            jsonObj2 = new JSONObject();
                                                            jsonObj2.put("avaliarDoador", avaliarDoador);     //avaliação do coletor para o doador
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
        );
    }

}
