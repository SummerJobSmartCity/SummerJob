package com.v3.nrd.nrdv3.Layout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.v3.nrd.nrdv3.R;

public class AvaliarColetor extends AppCompatActivity {

    private Button btnOk1;

    RatingBar rtAvaliação;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avaliar_coletor);

        btnOk1=(Button)findViewById(R.id.btnOk1);
        btnOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(AvaliarColetor.this, ActDoador.class);
                startActivity(it);
            }
        });

    }

}
