package com.desarrollandoapps.senamarket.presupuesto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;

import java.util.Calendar;
import java.util.Date;

public class PresupuestoActivity extends AppCompatActivity {

    Calendar calendar;
    Date fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presupuesto);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();
        fecha = calendar.getTime();

    }

    public void lanzarAgregarPresupuesto(View v) {
        Intent i = new Intent(this, AgregarPresupuestoActivity.class);
        startActivity(i);
    }

    public void lanzarVerPresupuesto(View v) {
        Intent i = new Intent(this, VerPresupuestosActivity.class);
        startActivity(i);
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
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
