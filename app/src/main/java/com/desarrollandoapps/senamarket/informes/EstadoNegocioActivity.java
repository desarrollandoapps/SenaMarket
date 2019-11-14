package com.desarrollandoapps.senamarket.informes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;
import com.desarrollandoapps.senamarket.cartera.Cliente;

import java.text.NumberFormat;
import java.util.ArrayList;

public class EstadoNegocioActivity extends AppCompatActivity {

    TextView txtEstadoNegocio;
    TextView txtCaja;
    TextView txtTotalCartera;
    TextView txtCuentasPagar;

    BaseDeDatos dbTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estado_negocio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtEstadoNegocio = (TextView) findViewById(R.id.txtEstadoNegocio);
        txtCaja = (TextView) findViewById(R.id.txtSaldoCaja);
        txtTotalCartera = (TextView) findViewById(R.id.txtTotalCartera);
        txtCuentasPagar = (TextView) findViewById(R.id.txtCuentasPorPagar);

        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        int deudaClientes = 0;
        ArrayList<Cliente> clientes = dbTienda.darClientesConDeuda();
        for (int i = 0; i < clientes.size(); i++){
            Cliente cliente = clientes.get(i);
            deudaClientes += cliente.darDeuda();
        }
        int estadoNegocio = dbTienda.darValorCajaFuerte() + deudaClientes - dbTienda.darCuentasPorPagar();

        txtCaja.setText(nf.format(dbTienda.darValorCajaFuerte()));
        txtTotalCartera.setText(nf.format(deudaClientes));
        txtCuentasPagar.setText(nf.format((dbTienda.darCuentasPorPagar())));
        txtEstadoNegocio.setText(nf.format(estadoNegocio));


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
