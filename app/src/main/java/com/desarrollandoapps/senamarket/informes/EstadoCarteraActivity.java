package com.desarrollandoapps.senamarket.informes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;
import com.desarrollandoapps.senamarket.adaptadores.AdaptadorListaCartera;
import com.desarrollandoapps.senamarket.cartera.Cliente;

import java.text.NumberFormat;
import java.util.ArrayList;

public class EstadoCarteraActivity extends AppCompatActivity {

    AdaptadorListaCartera adaptador;
    ArrayList<Cliente> clientes;
    ListView lvCartera;
    TextView txtDeuda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_cartera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvCartera = (ListView) findViewById(R.id.lvCartera);
        txtDeuda = (TextView) findViewById(R.id.txtDeudaTotal);
        BaseDeDatos dbTienda = new BaseDeDatos(this, "Tienda", null, 1);

        clientes = dbTienda.darClientesConDeuda();

        adaptador = new AdaptadorListaCartera(this, clientes);
        lvCartera.setAdapter(adaptador);

        int deuda = 0;

        for (int i = 0; i < clientes.size(); i++){
            Cliente cliente = clientes.get(i);
            deuda += cliente.darDeuda();
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String deudaStr = nf.format(deuda);
        txtDeuda.setText(deudaStr);


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
