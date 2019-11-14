package com.desarrollandoapps.senamarket.presupuesto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.R;

import java.util.Calendar;
import java.util.Date;

public class AgregarPresupuestoActivity extends AppCompatActivity {

    Spinner cboCategoriaPresupuesto;
    EditText txtMonto;

    BaseDeDatos dbTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_presupuesto);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cboCategoriaPresupuesto = (Spinner) findViewById(R.id.cboCategoriaPresupuesto);
        txtMonto = (EditText) findViewById(R.id.txtMontoPresupuesto);

        String[] tipo = {"Proveedores", "Bancos", "Arriendo", "Servicios públicos"};

        ArrayAdapter spinner_adapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, tipo);
        spinner_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboCategoriaPresupuesto.setAdapter(spinner_adapter1);

        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
    }

    public void agregarPresupuesto(View v) {

        if (!txtMonto.getText().toString().isEmpty()) {

            String categoria = cboCategoriaPresupuesto.getSelectedItem().toString();
            String monto = txtMonto.getText().toString();
            int montoInt = Integer.parseInt(monto);
            Date fecha = Calendar.getInstance().getTime();

            Presupuesto presupuesto = new Presupuesto(fecha, categoria, montoInt, 0);

            if (dbTienda.agregarPresupuesto(presupuesto)) {
                Toast.makeText(this, "Presupuesto creado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ya existe un presupuesto para el mes", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(this, "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        }
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
}
