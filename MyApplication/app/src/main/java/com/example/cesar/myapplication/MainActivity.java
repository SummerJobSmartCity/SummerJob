package com.example.cesar.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.*;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button start;
    TextView textView;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);


        start = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);

        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://172.28.144.181:3000/api/students",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this,response, Toast.LENGTH_LONG).show();

                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                textView.append(error.getMessage());
                                System.out.println(error.getMessage());

                            }
                        }

                ){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("firstname" , "OQTACONTECENO");
                        params.put("lastname","OQTACONTECENO");
                        params.put("age", "OQTACONTECENO");
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }

//                final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "http://172.28.144.181:3000/api/students",
//                        new Response.Listener<JSONArray>(){
//                            @Override
//                            public void onResponse(JSONArray response){
//
//                                try {
//                                //    System.out.println(response.toString());
//                                    JSONArray jsonArray = response;//.getJSONArray("students");
//
//                                    for(int i = 0; i < jsonArray.length(); i++){
//                                        JSONObject student = jsonArray.getJSONObject(i);
//
//                                        String firstname = student.getString("firstname");
//                                        String lastname = student.getString("lastname");
//                                        String age = student.getString("age");
//
//                                        textView.append(firstname + " \n " + lastname + " \n " + age + " \n ");
//
//                                    }
//                                } catch (JSONException e){
//                                    e.printStackTrace();
//                                }
//                            }
//                        },
//
//                        new Response.ErrorListener(){
//                            @Override
//                            public void onErrorResponse(VolleyError error){
//                                textView.append(error.getMessage());
//                                System.out.println(error.getMessage());
//
//                            }
//                        }
//                );
//                requestQueue.add(jsonArrayRequest);
//
//            }
        });
    }
}
