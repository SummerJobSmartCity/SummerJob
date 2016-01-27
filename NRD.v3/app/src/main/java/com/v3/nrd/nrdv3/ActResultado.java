package com.v3.nrd.nrdv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

public class ActResultado extends AppCompatActivity {

    private Button btnColetaEfetuada;
    private Button btnRecusar;
    String fbJsonObjToString;
    JSONObject jsonObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_resultado);

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

        btnRecusar = (Button) findViewById(R.id.btnRecusar);
        btnRecusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActResultado.this, ActDoador.class);
                it.putExtra("fbJsonObj",jsonObj.toString());
                startActivity(it);
                //buscar doador
            }
        });
    }
}
