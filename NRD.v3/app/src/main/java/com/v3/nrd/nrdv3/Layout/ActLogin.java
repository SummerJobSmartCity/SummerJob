package com.v3.nrd.nrdv3.Layout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.v3.nrd.nrdv3.R;

public class ActLogin extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);

        btnLogin = (Button)findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //a classe responsável por chamar a outra tela é intent
        Intent it = new Intent(this, MainActivity.class);
        //2 parametros(a classe que está chamando,a classe que quero chamar)

        startActivity(it);


    }
}
