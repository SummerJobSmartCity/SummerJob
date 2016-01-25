package com.v1.gcm.gcmv1.receiver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.v1.gcm.gcmv1.R;

import de.greenrobot.event.EventBus;
import com.v1.gcm.gcmv1.domain.PushMessage;

/**
 * Created by cesar on 21/01/16.
 */

public class MainActivity extends AppCompatActivity {


    private static final String SERVER_API_KEY = "AIzaSyDW7CS4GIXuggF-zvuvya7ZXfoAk20CKGw";
    private static final String SENDER_ID = "312239874000";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private TextView tvTitle;
    private TextView tvMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMessage = (TextView) findViewById(R.id.tv_message);


        if (checkPlayServices()){//se possui Google Play Service
            //chamar o registrationId
            Intent it = new Intent(this, RegistrationIntentService.class);
            startService(it);
        }

    }

//verificar se há o Google Play Services
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("LOG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    // LISTENER
    public void onEvent( final PushMessage pushMessage ){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTitle.setText( pushMessage.getTitle() );
                tvMessage.setText( pushMessage.getMessage() );
            }
        });
    }


}
