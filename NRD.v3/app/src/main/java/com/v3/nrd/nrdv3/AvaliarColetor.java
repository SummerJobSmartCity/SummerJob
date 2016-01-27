package com.v3.nrd.nrdv3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import org.json.JSONException;
import org.json.JSONObject;

public class AvaliarColetor extends AppCompatActivity {

    private Button btnOk1;
    String fbJsonObjToString;
    JSONObject jsonObj;

    RatingBar rtAvaliação;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliar_coletor);

        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");
        System.out.println("STRING NO AVALIAR COLETOR ======================>   " + fbJsonObjToString);

        try {
            jsonObj = new JSONObject(fbJsonObjToString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnOk1=(Button)findViewById(R.id.btnOk1);
        btnOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AvaliarColetor.this, ActDoador.class);
                it.putExtra("fbJsonObj",jsonObj.toString());
                startActivity(it);
            }
        });

    }

}
