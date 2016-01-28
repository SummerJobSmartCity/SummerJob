package com.v3.nrd.nrdv3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.v3.nrd.nrdv3.GCM.RegistrationIntentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class ActLogin extends AppCompatActivity{

    private static final String SERVER_API_KEY = "AIzaSyBOL9JDjuKHKvw6QHZ16lo5XXk9ffmfcUo";
    private static final String SENDER_ID = "1023915214454";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private RequestQueue requestQueue;
    private JSONObject fbJsonObj = new JSONObject();
    String user_name;

    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        requestQueue = Volley.newRequestQueue(this);
        setContentView(R.layout.act_login);

        info = (TextView)findViewById(R.id.info);
        callbackManager = CallbackManager.Factory.create();

        final EditText edtNome = (EditText)findViewById(R.id.edtNome);
        final EditText edtNome2 = (EditText)findViewById(R.id.editText);

        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));

        Button btn = (Button)findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActLogin.this,
                        "Logado! " + edtNome.getText().toString(),
                        Toast.LENGTH_SHORT).show();

                Intent it = new Intent(ActLogin.this, MainActivity.class);
                System.out.println("FBOBJ========>    " + fbJsonObj.toString());
                it.putExtra("fbJsonObj",fbJsonObj.toString());
                startActivity(it);
            }
        });

        System.out.println("VAI CHAMAR O IF DO CHECKPLAYSERVICES");
        if (checkPlayServices()) {//se possui Google Play Service
            System.out.println("ENTROU NO IF CHECKPLAYSERVICES");
            //chamar o registrationId
            Intent it = new Intent(this, RegistrationIntentService.class);
            System.out.println("intent");
            startService(it);
        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
//Application code
                        Log.v("LoginActivity", object.toString());
                        String stringdolog = object.toString();
                        fbJsonObj = response.getJSONObject();

                        try {
// info.setText("Login " + response.getJSONObject().toString());

                            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://172.28.144.181:5000/api/users", fbJsonObj,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Toast.makeText(ActLogin.this,"Enviado para o serividor.", Toast.LENGTH_LONG).show();

                                        }
                                    },

                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                        }
                                    }

                            );

                            requestQueue.add(jsonObjectRequest);

                            user_name = object.getString("name"); //Obtendo o nome do usuario do facebook. Enviar para o servidor!

                            info.setText("Login:    " + user_name);
                            Toast.makeText(ActLogin.this,
                                    "Olá " + user_name + edtNome.getText().toString(),
                                    Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent it = new Intent(ActLogin.this, MainActivity.class);
                        System.out.println("FBOBJ========>    " + fbJsonObj.toString());
                        it.putExtra("fbJsonObj", fbJsonObj.toString());
                        startActivity(it);

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,email,first_name,last_name,gender");
                request.setParameters(parameters);
                request.executeAsync();



            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");
            }
        });

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.v3.nrd.nrdv3/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.v3.nrd.nrdv3/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
