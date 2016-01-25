package com.v1.gcm.gcmv1.receiver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by cesar on 21/01/16.
 */

//para segurança
public class MyInstanceIDListenerService extends InstanceIDListenerService  {
    private static final String TAG = "LOG";

    @Override
    public void onTokenRefresh() {
        //super.onTokenRefresh();
        // se for ativado,logo, há um NOVO RegistrationId. Com isso, chama o registration
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.edit().putBoolean("status", false ).apply();

        Intent it = new Intent(this, RegistrationIntentService.class);
        startService(it);
    }
}
