package com.v3.nrd.nrdv3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.v3.nrd.nrdv3.GCM.RegistrationIntentService;

public class ActLogin extends AppCompatActivity implements View.OnClickListener {

    private static final String SERVER_API_KEY = "AIzaSyBmfw1eEDkMQCPVwQPFC43ywxSoLDCfVDA";
    private static final String SENDER_ID = "1023915214454";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private TextView tvTitle;
    private TextView tvMessage;

    private Button btnLogin;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvMessage = (TextView) findViewById(R.id.tv_message);


        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);

        if (checkPlayServices()) {//se possui Google Play Service
            //chamar o registrationId
            Intent it = new Intent(this, RegistrationIntentService.class);
            System.out.println("intent");
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


    @Override
    public void onClick(View v) {
        //a classe responsável por chamar a outra tela é intent
        Intent it = new Intent(this, MainActivity.class);
        //2 parametros(a classe que está chamando,a classe que quero chamar)

        startActivity(it);


    }
}
