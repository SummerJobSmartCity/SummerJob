package com.v3.nrd.nrdv3;

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

public class AvaliarColetor extends AppCompatActivity {

    private Button btnOk1;
    private RatingBar rating_Bar;
    private RequestQueue requestQueue;
    private static String idcoletor = "";
    private TextView mNomeColetorTextView;
    private TextView emailColetorTextView;
    private static String mNomeColetor = "";
    private static String emailColetor = "";

    JSONObject jsonObj;
    JSONObject jsonObj2;
    JSONObject jsonObjDados;

    String fbJsonObjToString;
    String fbJsonObjToStringDados;
    String avaliarColetor;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliar_coletor);

        requestQueue = Volley.newRequestQueue(this);

        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");
        fbJsonObjToStringDados = getIntent().getStringExtra("jsonObjDados");

        try {
            jsonObj = new JSONObject(fbJsonObjToString);
            id = jsonObj.getString("id");

            jsonObjDados = new JSONObject(fbJsonObjToStringDados);
            mNomeColetor = jsonObjDados.getString("nomeColetor");
            emailColetor = jsonObjDados.getString("emailColetor");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listenerForRatingBar();

        btnOk1=(Button)findViewById(R.id.btnOk1);
        btnOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AvaliarColetor.this, ActDoador.class);
                it.putExtra("fbJsonObj", jsonObj.toString());

                try {
                    idcoletor = jsonObj.getString("idcoletor");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "http://172.28.144.181:5000/api/avaliarColetor/" + idcoletor,
                        jsonObj2,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(AvaliarColetor.this, "Atualizando avaliação do doador", Toast.LENGTH_LONG).show();
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

        mNomeColetorTextView = (TextView) findViewById(R.id.textView10);
        emailColetorTextView = (TextView) findViewById(R.id.textView12);

        mNomeColetorTextView.setText(mNomeColetor);
        emailColetorTextView.setText(emailColetor);

    }

    public void onBackPressed(){
        Intent it = new Intent(AvaliarColetor.this, MainActivity.class);
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
        rating_Bar=(RatingBar)findViewById(R.id.rtAvaliação);
        rating_Bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                    @Override
                                                    public void onRatingChanged(RatingBar rating_Bar, float rating, boolean fromUser) {
                                                        avaliarColetor = String.valueOf(rating);
                                                        System.out.println("IMPRIMINDO VALOR DO AVALIARDOADOR==========================>         " + avaliarColetor);
                                                        try {
                                                            jsonObj2 = new JSONObject();
                                                            jsonObj2.put("avaliarColetor", avaliarColetor);     //avaliação do coletor para o doador
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
        );
    }

}
