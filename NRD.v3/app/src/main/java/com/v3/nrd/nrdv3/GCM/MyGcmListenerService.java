package com.v3.nrd.nrdv3.GCM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.v3.nrd.nrdv3.ActPedido;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cesar on 25/01/16.
 */
public class MyGcmListenerService extends GcmListenerService {
    public static final String TAG = "LOG";
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //super.onMessageReceived(from,data);
        String title = data.getString("title");
        String message = data.getString("message");

        String nome_doador=data.getString("nomedoador");
        String telefone_doador=data.getString("telefonedoador");
        try {
            JSONObject json_nDoador = new JSONObject( data.getString("nomedoador") );
            JSONObject json_tDoador = new JSONObject( data.getString("telefonedoador") );

            Intent it= new Intent(MyGcmListenerService.this, ActPedido.class);


            it.putExtra("nomedoador", json_nDoador.toString() );
            it.putExtra("telefonedoador", json_tDoador.toString() );
            startActivity(it, ActPedido.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        String nome_coletor=data.getString("nomecoletor");
        String telefone_coletor=data.getString("telefonecoletor");


//
//        Log.i(TAG, title);
//        Log.i(TAG, message);

    }

}
