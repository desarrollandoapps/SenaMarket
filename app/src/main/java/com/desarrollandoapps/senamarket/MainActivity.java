package com.desarrollandoapps.senamarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.desarrollandoapps.senamarket.caja.GestionCajaActivity;
import com.desarrollandoapps.senamarket.cartera.CarteraActivity;
import com.desarrollandoapps.senamarket.configuracion.ConfiguracionActivity;
import com.desarrollandoapps.senamarket.informes.InformesActivity;
import com.desarrollandoapps.senamarket.calendario.CalendarioActivity;
import com.desarrollandoapps.senamarket.presupuesto.VerPresupuestosActivity;

public class MainActivity extends AppCompatActivity {

    BaseDeDatos dbTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton btnCaja = (ImageButton) findViewById(R.id.btnCaja);
        ImageButton btnCartera = (ImageButton) findViewById(R.id.btnCartera);
        ImageButton btnConfiguracion = (ImageButton) findViewById(R.id.btnConfiguracion);

        // Crear DB
         dbTienda = new BaseDeDatos(this, "Tienda", null, 1);

    }

    public void abrirGestionCaja(View view) {
        Intent i = new Intent(getApplicationContext(), GestionCajaActivity.class);
        startActivity(i);
    }

    public void abrirInformes(View v) {
        Intent i = new Intent(getApplicationContext(), InformesActivity.class);
        startActivity(i);
    }

    public void abrirCartera(View v) {
        Intent i = new Intent(getApplicationContext(), CarteraActivity.class);
        startActivity(i);
    }

    public void abrirPresupuesto(View v) {
        Intent i = new Intent(getApplicationContext(), VerPresupuestosActivity.class);
        startActivity(i);
    }

    public void abrirCalendario(View v) {
        Intent i = new Intent(getApplicationContext(), CalendarioActivity.class);
        startActivity(i);
    }

    public void abrirConfiguracion(View v) {
        Intent i = new Intent(getApplicationContext(), ConfiguracionActivity.class);
        startActivity(i);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_acerca_de:
                Intent i = new Intent(this, AcercaDeActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
