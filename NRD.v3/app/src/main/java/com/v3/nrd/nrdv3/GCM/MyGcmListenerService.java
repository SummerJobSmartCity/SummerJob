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
        String comando      = data.getString("comando");
        String nomeDoador   = data.getString("nomedoador");
        String emaildoador  = data.getString("emaildoador");
        String nomeColetor  = data.getString("nomecoletor");
        String latitude     = data.getString("latitude");
        String longitude    = data.getString("longitude");
        String iddoador     = data.getString("iddoador");
        String idcoletor    = data.getString("idcoletor");


        Intent it   =    new Intent("nrd.UpdateActPedido");
        Intent it2  =    new Intent("nrd.UpdateActResultado");
        Intent it3  =    new Intent("nrd.UpdateAvaliarDoador");



        Log.i(TAG, comando);
        it.putExtra("comando", comando);
        it.putExtra("nomedoador"    , nomeDoador);
        it.putExtra("emaildoador"   , emaildoador);
        it.putExtra("latitude"      , latitude);
        it.putExtra("longitude"     , longitude);

        it.putExtra("iddoador"     , iddoador);
        it2.putExtra("iddoador"     , iddoador);
        it3.putExtra("iddoador"     , iddoador);

        it.putExtra("idcoletor"     , idcoletor);
        it2.putExtra("idcoletor"     , idcoletor);
        it3.putExtra("idcoletor"     , idcoletor);



        it2.putExtra("nomecoletor", nomeColetor);
        it2.putExtra("comando"      , comando);


        System.out.println("COMANDO        DOADOR NO MYGCM ==========================>>                 " + comando);
        System.out.println("ID        DOADOR NO MYGCM ==========================>>                 " + iddoador);
        System.out.println("ID        COLETOR NO MYGCM ==========================>>                 " + idcoletor);

        sendBroadcast(it);
        sendBroadcast(it2);
        sendBroadcast(it3);
    }
}
