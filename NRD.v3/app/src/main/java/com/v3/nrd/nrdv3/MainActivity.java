package com.v3.nrd.nrdv3;


import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.content.*; //para chamar outra activity

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.v3.nrd.nrdv3.GCM.RegistrationIntentService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

        //criar os objetos
    private Button btnColetor;
    private Button btnDoador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        System.out.println("main");

        btnColetor = (Button) findViewById(R.id.btnColetor);
        btnDoador = (Button) findViewById(R.id.btnDoador);

        btnColetor.setOnClickListener(this);
        btnDoador.setOnClickListener(this);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnColetor:
                Intent it = new Intent(this, ActColetor.class);
                //2 parametros(a classe que está chamando,a classe que quero chamar)

                startActivity(it);
                break;
            case R.id.btnDoador:
                Intent it2 = new Intent(this, ActDoador.class);
                //2 parametros(a classe que está chamando,a classe que quero chamar)

                startActivity(it2);
                break;
        }
    }
}