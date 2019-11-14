package com.desarrollandoapps.senamarket.cartera;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;

public class CarteraActivity extends AppCompatActivity implements NuevoClienteFragment.nuevoCliente {

    SQLiteDatabase db;
    BaseDeDatos dbTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_caja, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.volver:
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

    public void lanzarNuevoCliente(View v) {
        NuevoClienteFragment dialogo = new NuevoClienteFragment();
        dialogo.show(getFragmentManager(), "nuevoCliente");

        android.app.Fragment fragment = getFragmentManager().findFragmentByTag("nuevoCliente");
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    public void lanzarClientes(View v) {
        Intent i = new Intent(this, ClientesActivity.class);
        startActivity(i);
    }

    @Override
    public void finalizarDialogoNuevoCliente(String cedula, String nombre, String direccion, String telefono, int deuda) {
        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
        db = dbTienda.getWritableDatabase();

        try {
            db.execSQL("INSERT INTO Cliente (idCliente, nombre, telefono, direccion, deuda) VALUES ('" + cedula + "', '" +
                    nombre + "', '" + telefono + "', '" + direccion + "', " + deuda + ")");
            dbTienda.agregarMovimientoCartera(cedula, "Inicial", deuda);
            Toast.makeText(this, "Cliente agregado", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(this, "Error al ingresar los datos", Toast.LENGTH_LONG).show();
        }
    }
}
