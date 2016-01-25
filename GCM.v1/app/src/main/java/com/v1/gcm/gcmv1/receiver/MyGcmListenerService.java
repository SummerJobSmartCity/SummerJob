package com.v1.gcm.gcmv1.receiver;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.v1.gcm.gcmv1.domain.PushMessage;

import de.greenrobot.event.EventBus;

/**
 * Created by cesar on 21/01/16.
 */
public class MyGcmListenerService extends GcmListenerService{
    public static final String TAG = "LOG";
    @Override
    public void onMessageReceived(String from, Bundle data) {
        //super.onMessageReceived(from,data);
        String title = data.getString("title");
        String message = data.getString("message");

        EventBus.getDefault().post(new PushMessage(title,message));
    }

}
