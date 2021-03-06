package com.v3.nrd.nrdv3.GCM;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {
    public static final String TAG = "LOG";
    public static final int CONSTANTE_ACT_PEDIDO = 1;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        String comando      = data.getString("comando");
        String nomeDoador   = data.getString("nomedoador");
        String emaildoador  = data.getString("emaildoador");
        String emailcoletor = data.getString("emailcoletor");
        String nomeColetor  = data.getString("nomecoletor");
        String latitude     = data.getString("latitude");
        String longitude    = data.getString("longitude");
        String iddoador     = data.getString("iddoador");
        String idcoletor    = data.getString("idcoletor");


        Intent it   =    new Intent("nrd.UpdateActPedido");
        Intent it2  =    new Intent("nrd.UpdateActResultado");
        Intent it3  =    new Intent("nrd.UpdateAvaliarDoador");



        Log.i(TAG, comando);
        it.putExtra("comando"       , comando);
        it.putExtra("nomedoador"    , nomeDoador);
        it.putExtra("emaildoador"   , emaildoador);
        it.putExtra("latitude"      , latitude);
        it.putExtra("longitude"     , longitude);
        it.putExtra("idcoletor"     , idcoletor);
        it.putExtra("iddoador"      , iddoador);
        it.putExtra("nomecoletor"   , nomeColetor);
        it.putExtra("emailcoletor"  , emailcoletor);

        it2.putExtra("comando"      , comando);
        it2.putExtra("nomedoador"   , nomeDoador);
        it2.putExtra("emaildoador"  , emaildoador);
        it2.putExtra("latitude"     , latitude);
        it2.putExtra("longitude"    , longitude);
        it2.putExtra("iddoador"     , iddoador);
        it2.putExtra("idcoletor"    , idcoletor);
        it2.putExtra("nomecoletor"  , nomeColetor);
        it2.putExtra("emailcoletor" , emailcoletor);


        it3.putExtra("comando"      , comando);
        it3.putExtra("nomedoador"   , nomeDoador);
        it3.putExtra("emaildoador"  , emaildoador);
        it3.putExtra("latitude"     , latitude);
        it3.putExtra("longitude"    , longitude);
        it3.putExtra("idcoletor"    , idcoletor);
        it3.putExtra("iddoador"     , iddoador);
        it3.putExtra("nomecoletor"  , nomeColetor);
        it3.putExtra("emailcoletor" , emailcoletor);

        System.out.println("COMANDO   DOADOR NO MYGCM ==========================>>                 " + comando);
        System.out.println("ID        DOADOR NO MYGCM ==========================>>                 " + iddoador);
        System.out.println("ID        COLETOR NO MYGCM ==========================>>                " + idcoletor);

        sendBroadcast(it);
        sendBroadcast(it2);
        sendBroadcast(it3);
    }
}
