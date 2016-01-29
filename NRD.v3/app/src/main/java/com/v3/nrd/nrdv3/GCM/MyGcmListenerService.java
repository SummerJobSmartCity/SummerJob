package com.v3.nrd.nrdv3.GCM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;


/**
 * Created by cesar on 27/01/16.
 */
public class MyGcmListenerService extends GcmListenerService {
    public static final String TAG = "LOG";
    public static final int CONSTANTE_ACT_PEDIDO = 1;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String comando = data.getString("comando");
        String nomeDoador = data.getString("nomedoador");
        String emaildoador = data.getString("emaildoador");
        String nomeColetor = data.getString("nomecoletor");
        String latitude = data.getString("latitude");
        String longitude = data.getString("longitude");


        Intent it = new Intent("nrd.UpdateActPedido");
        Intent it2 = new Intent("nrd.UpdateActResultado");

        Log.i(TAG, comando);
        it.putExtra("comando", comando);
        it.putExtra("nomedoador", nomeDoador);
        it.putExtra("emaildoador", emaildoador);
        it.putExtra("latitude", latitude);
        it.putExtra("longitude", longitude);
        it2.putExtra("nomecoletor", nomeColetor);

        sendBroadcast(it);
        sendBroadcast(it2);

    }
}
