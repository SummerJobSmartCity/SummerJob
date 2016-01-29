package com.v3.nrd.nrdv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
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
    String id;

    String avaliarDoador;
    private RatingBar rating_Bar;
    private RequestQueue requestQueue;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliar_doador);

        requestQueue = Volley.newRequestQueue(this);


        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");
        System.out.println("CHECAR STRING NO AVALIAR DOADOR =================== >    " + fbJsonObjToString);

        listenerForRatingBar();

        try {
            jsonObj = new JSONObject(fbJsonObjToString);
            System.out.println("IMPRIMINDO VALOR DO AVALIARDOADOR==========================>         " + avaliarDoador);
            jsonObj.put("avaliarDoador",avaliarDoador);     //avaliação do coletor para o doador
            id = jsonObj.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnOk1=(Button)findViewById(R.id.btnOk1);
        btnOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AvaliarDoador.this, ActPedido.class);
                it.putExtra("fbJsonObj",jsonObj.toString());

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        "http://172.28.144.181:5000/api/users/" + id,
                        jsonObj,
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
            }
        });
    }

    public void listenerForRatingBar(){
        rating_Bar=(RatingBar)findViewById(R.id.ratingBar);
        rating_Bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                    @Override
                                                    public void onRatingChanged(RatingBar rating_Bar, float rating, boolean fromUser) {
                                                        avaliarDoador = String.valueOf(rating_Bar.getRating());
                                                    }
                                                }
        );
    }
}
