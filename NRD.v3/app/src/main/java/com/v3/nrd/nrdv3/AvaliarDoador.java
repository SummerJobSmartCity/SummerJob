package com.v3.nrd.nrdv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

public class AvaliarDoador extends AppCompatActivity {

    private RatingBar rating_Bar;
    String avaliarDoador;


    private Button btnOk1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliar_doador);

        listenerForRatingBar();
        btnOk1=(Button)findViewById(R.id.btnOk1);
        btnOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AvaliarDoador.this, ActColetor.class);
                startActivity(it);
            }
        });


    }
    public void listenerForRatingBar(){
        rating_Bar=(RatingBar)findViewById(R.id.ratingBar);
        rating_Bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                                    @Override
                                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                                        avaliarDoador= String.valueOf(rating_Bar.getRating());
                                                    }
                                                }
        );

    }
}
