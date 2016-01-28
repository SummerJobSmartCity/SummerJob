package com.v3.nrd.nrdv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import org.json.JSONException;
import org.json.JSONObject;

public class AvaliarDoador extends AppCompatActivity {

    private Button btnOk1;
    private RatingBar rating_Bar;
    String fbJsonObjToString;
    JSONObject jsonObj;
    String id;
    String avaliarDoador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliar_doador);

        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");
        System.out.println("CHECAR STRING NO AVALIAR DOADOR =================== >    " + fbJsonObjToString);

        try {
            jsonObj = new JSONObject(fbJsonObjToString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnOk1=(Button)findViewById(R.id.btnOk1);
        btnOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AvaliarDoador.this, ActPedido.class);
                it.putExtra("fbJsonObj",jsonObj.toString());
                startActivity(it);
            }
        });
    }

    public void listenerForRatingBar(){
        rating_Bar=(RatingBar)findViewById(R.id.ratingBar);
        rating_Bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                    @Override
                                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                        avaliarDoador = String.valueOf(rating_Bar.getRating());
                                                    }
                                                }
        );
    }
}
