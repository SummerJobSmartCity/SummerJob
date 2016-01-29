package com.v3.nrd.nrdv3;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class ActResultado extends AppCompatActivity {

    private Button btnColetaEfetuada;
    private Button btnRecusar;
    String fbJsonObjToString;
    JSONObject jsonObj;

    private static String mNomeColetor = "";
    UpdateActResultado mReceiver;
    private static boolean flag = false;
    private TextView mNomeColetorTextView;

    public class ProcessDataResultado extends AsyncTask<Void, Void, Void> {
        ProgressDialog mProgressDialog;

        public ProcessDataResultado(){
            mProgressDialog = new ProgressDialog(ActResultado.this);
            mProgressDialog.setTitle("Buscando coletor");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setButton("Cancelar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent it = new Intent(ActResultado.this, ActDoador.class);
                    it.putExtra("fbJsonObj", jsonObj.toString());

                    startActivity(it);
                    // Use either finish() or return() to either close the activity or just the dialog
                    return;
                }
            });
        }

        @Override
        protected void onPreExecute(){
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while(!flag);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            flag = false;
            mProgressDialog.dismiss();
            mNomeColetorTextView.setText(mNomeColetor);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_resultado);

        mReceiver = new UpdateActResultado();
        registerReceiver(mReceiver, new IntentFilter("nrd.UpdateActResultado")); // Register receiver


        fbJsonObjToString = getIntent().getStringExtra("fbJsonObj");
        System.out.println("STRING NO RESULTADO ======================>   " + fbJsonObjToString);

        try {
            jsonObj = new JSONObject(fbJsonObjToString);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        btnColetaEfetuada = (Button) findViewById(R.id.btnColetaEfetuada);
        btnColetaEfetuada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActResultado.this, AvaliarColetor.class);
                it.putExtra("fbJsonObj",jsonObj.toString());
                startActivity(it);
            }
        });

        mNomeColetorTextView = (TextView) findViewById(R.id.nome_coletor);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new ProcessDataResultado().execute();
    }

    public class UpdateActResultado extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            //mComando = intent.getStringExtra("comando");
            System.out.println("Doador SAINDO DO PROGRESS DIALOG");
            flag = true;
            mNomeColetor = intent.getStringExtra("nomecoletor");
            Log.d("UpdateActResultado", "COLETOR OLAOLAOLA");
        }
    }

}
