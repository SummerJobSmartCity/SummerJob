package com.v3.nrd.nrdv3.GCM;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.v3.nrd.nrdv3.ActPedido;


/**
 * Created by cesar on 27/01/16.
 */
public class MyGcmListenerService extends GcmListenerService {
    public static final String TAG = "LOG";
    public static final int CONSTANTE_ACT_PEDIDO = 1;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from,data);
        String title = data.getString("title");
//        String message = data.getString("message");
//
//        String nome_doador=data.getString("nomedoador");
//        String telefone_doador=data.getString("telefonedoador");
//        String title="Rubão!!!!!!";

        //Intent it= new Intent(MyGcmListenerService.this, ActPedido.class);

//        String nome_coletor=data.getString("nomecoletor");
//        String telefone_coletor=data.getString("telefonecoletor");

//        Log.i(TAG, title);
//        Log.i(TAG, message);


        Bundle dados_doador= new Bundle();
        dados_doador.putString("nomedoador",title);

        Intent it= new Intent(this, ActPedido.class);


        it.putExtras(dados_doador);
        //it.putExtra("telefonedoador", json_tDoador.toString() );
        startActivity(it,dados_doador);


    }
}
