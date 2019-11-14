package com.desarrollandoapps.senamarket.informes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.desarrollandoapps.senamarket.R;

public class InformesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ir_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_ir_inicio:
                onBackPressed();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void lanzarFlujoNeto (View v) {
        Intent i = new Intent(this, FlujoNetoCajaActivity.class);
        startActivity(i);
    }

    public void lanzarEstadoCartera (View v) {
        Intent i = new Intent(this, EstadoCarteraActivity.class);
        startActivity(i);
    }

    public void lanzarEstadoNegocio (View v) {
        Intent i = new Intent(this, EstadoNegocioActivity.class);
        startActivity(i);
    }

    public void lanzarCuentasPorPagar (View v) {
        Intent i = new Intent(this, CuentasPorPagarActivity.class);
        startActivity(i);
    }
}
