package com.example.cesar.rotamapagps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
    private EditText editDepart;
    private EditText editArrivee;
    private Button btnRechercher;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editDepart = (EditText) findViewById(R.id.editOrigem);
        editArrivee = (EditText) findViewById(R.id.editDestino);
        btnRechercher = (Button) findViewById(R.id.btnSearch);

        btnRechercher.setOnClickListener(new OnClickListener() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void onClick(final View v) {
                if("".equals(editDepart.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "Imposivel sem origem", Toast.LENGTH_SHORT).show();
                }
                else if("".equals(editArrivee.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, "IMpossivel sem destino", Toast.LENGTH_SHORT).show();
                }
                else {
                    //On transmet les données à l'activité suivante
                    final Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    intent.putExtra("ORIGEM", editDepart.getText().toString().trim());
                    intent.putExtra("DESTINO", editArrivee.getText().toString().trim());

                    MainActivity.this.startActivity(intent);
                }
            }
        });
    }
}