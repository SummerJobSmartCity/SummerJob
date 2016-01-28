package com.v3.nrd.nrdv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.v3.nrd.nrdv3.GCM.MyGcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

public class BuscarColetor extends AppCompatActivity {

    private Button btnOk1;
    String fbJsonObjToString;
    JSONObject jsonObj;



//    recebe aviso do servidor/GCM que achou o coletor mais proximo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buscar_coletor);
        MyGcmListenerService mygcm = new MyGcmListenerService();

        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");
        System.out.println("STRING NO BUSCA COLETOR ======================>   " + fbJsonObjToString);

        try {
            jsonObj = new JSONObject(fbJsonObjToString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnOk1=(Button)findViewById(R.id.btnOk1);
        btnOk1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(BuscarColetor.this, ActResultado.class);
                it.putExtra("fbJsonObj", jsonObj.toString());
                startActivity(it);
            }
        });
    }
}
