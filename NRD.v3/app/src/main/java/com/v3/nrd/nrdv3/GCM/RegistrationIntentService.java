package com.v3.nrd.nrdv3.GCM;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    public static final String LOG = "LOG";
    String token;

    public RegistrationIntentService(){
        super(LOG);
        System.out.println("ta entrando no REGISTRATIONINTENTSERVICE =================   |||||||||||  ");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = preferences.getBoolean("status", false);
        System.out.println("ta entrando no REGISTRATIONINTENTSERVICE =================   |||||||||||  " + status);
        synchronized (LOG) {
            InstanceID instanceID = InstanceID.getInstance(this);
            try{
                if (!status) {             //ocorre quando há um cadastro, como se a pessoa estivesse acessando pela primeira vez na app
                    System.out.println("entrou  NO IF DO CADASTRO PARA IMPRIMIR O TOKEN");

                    token = instanceID.getToken("1023915214454",
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                            null);
                    Log.i(LOG, "TOKEN:" + token);
                    System.out.println("VAMO VER O TOKEN!! ================  |||| >    " + token);

                    preferences.edit().putString("token",token != null && token.trim().length() > 0 ? token : "").apply();
                    // sendRegistrationId(token );//enviar para servidor

                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("saiu");
        }
    }
}
