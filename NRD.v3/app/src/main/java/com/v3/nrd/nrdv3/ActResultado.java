package com.v3.nrd.nrdv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActResultado extends AppCompatActivity {

    private Button btnColetaEfetuada;
    private Button btnRecusar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_resultado);

        btnColetaEfetuada = (Button) findViewById(R.id.btnColetaEfetuada);
        btnColetaEfetuada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActResultado.this, AvaliarColetor.class);
                startActivity(it);
            }
        });

        btnRecusar = (Button) findViewById(R.id.btnRecusar);
        btnRecusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActResultado.this, ActDoador.class);
                startActivity(it);
                //buscar doador
            }
        });
    }
}
