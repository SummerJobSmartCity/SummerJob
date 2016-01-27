package com.v3.nrd.nrdv3.GCM;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by cesar on 27/01/16.
 */
public class RegistrationIntentService extends IntentService {

    public static final String LOG = "LOG";

    public RegistrationIntentService(){
        super(LOG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences preferences;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean status = preferences.getBoolean("status", false);
        System.out.println(status);
        synchronized (LOG) {
            InstanceID instanceID = InstanceID.getInstance(this);
            try{
                if (!status) {//ocorre quando há um cadastro, como se a pessoa estivesse acessando pela primeira vez na app
                    System.out.println("entrou");

                    String token = instanceID.getToken("1023915214454",
                            GoogleCloudMessaging.INSTANCE_ID_SCOPE,
                            null);
                    Log.i(LOG, "TOKEN:" + token);

                    preferences.edit().putString("token",
                            token != null && token.trim().length() > 0 ? token : "").apply();
                    // sendRegistrationId(token );//enviar para servidor

                }
            }catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("saiu");
        }
    }
}
