package com.v3.nrd.nrdv3.GCM;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

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
//
//        String nomedoador=data.getString("nomedoador");
//        String telefonedoador=data.getString("telefonedoador");

        Log.i(TAG, title);
        Log.i(TAG, message);

    }

}
