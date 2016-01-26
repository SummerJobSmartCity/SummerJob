package com.v3.nrd.nrdv3;

import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class ActLogin extends AppCompatActivity{

    private TextView info;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private RequestQueue requestQueue;
    private JSONObject fbJsonObj = new JSONObject();

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

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
// App code
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
                                            Toast.makeText(ActLogin.this,"DEU CERTO PORRA", Toast.LENGTH_LONG).show();

                                        }
                                    },

                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                        }
                                    }

                            );

                            requestQueue.add(jsonObjectRequest);

                            String user_name = object.getString("name"); //Obtendo o nome do usuario do facebook. Enviar para o servidor!

                            info.setText("Login:    " + user_name);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

}
