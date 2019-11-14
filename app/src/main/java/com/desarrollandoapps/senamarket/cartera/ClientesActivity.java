package com.desarrollandoapps.senamarket.cartera;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;

import java.util.ArrayList;

public class ClientesActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    ListView lvClientes;
    ArrayList<Cliente> clientes;
    public static final int REQUEST_CODE_CLIENTE = 2;

    BaseDeDatos dbTienda;
    SQLiteDatabase db;
    ArrayList<String> nombres;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvClientes = (ListView) findViewById(R.id.lvClientes);
        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
        clientes = dbTienda.darClientes();
        nombres = dbTienda.darNombresClientes();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, nombres);
        lvClientes.setAdapter(adapter);
        lvClientes.setOnItemClickListener(this);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
        clientes = dbTienda.darClientes();
        Cliente cliente = clientes.get(position);

        Intent i = new Intent(this, ClienteActivity.class);
        i.putExtra("cedula", cliente.darCedula());
        i.putExtra("nombre", cliente.darNombre());
        i.putExtra("direccion", cliente.darDireccion());
        i.putExtra("telefono", cliente.darTelefono());
        i.putExtra("deuda", cliente.darDeuda());
        startActivityForResult(i, REQUEST_CODE_CLIENTE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        boolean resultado;
        if (data != null) {

            String idCliente = data.getExtras().getString("idCliente");
            int monto = data.getExtras().getInt("monto");

            if (requestCode == REQUEST_CODE_CLIENTE && resultCode == ClienteActivity.RESULT_ABONO) {
                if (monto > dbTienda.darDeudaCliente(idCliente)) {
                    Toast.makeText(this, "El abono no puede ser superior a la deuda", Toast.LENGTH_SHORT).show();
                } else {
                    resultado = dbTienda.agregarMovimientoCartera(idCliente, "Abono", monto);
                    if (resultado) {
                        Toast.makeText(this, "Ingresado con éxito", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No se ingresó", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == REQUEST_CODE_CLIENTE && resultCode == ClienteActivity.RESULT_DEUDA) {
                resultado = dbTienda.agregarMovimientoCartera(idCliente, "Fia", monto);
                if (resultado) {
                    Toast.makeText(this, "Ingresado con éxito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se ingresó", Toast.LENGTH_SHORT).show();
                }
            }
        }

        nombres = dbTienda.darNombresClientes();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, nombres);
        lvClientes.setAdapter(adapter);

    }
}
