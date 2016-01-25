package com.v1.gcm.gcmv1.receiver;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.v1.gcm.gcmv1.domain.User;
import com.v1.gcm.gcmv1.domain.WrapObjToNetwork;
import com.v1.gcm.gcmv1.network.NetworkConnection;

import java.io.IOException;

/**
 * Created by cesar on 21/01/16.
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

                    String token = instanceID.getToken("312239874000",
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
        }
    }

    private void sendRegistrationId(String token){
//        User user = new User();
//        user.setRegistrationId( token );
//
//        NetworkConnection
//                .getInstance(this)
//                .execute(new WrapObjToNetwork(user,"save-user"), RegistrationIntentService.class.getName());


    }
}
