package com.desarrollandoapps.senamarket.informes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.calendario.EventoCalendario;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;
import com.desarrollandoapps.senamarket.adaptadores.AdaptadorFilaCalendario;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CuentasPorPagarActivity extends AppCompatActivity {

    TextView lblTotal;
    ListView lvCuentas;

    ArrayList<EventoCalendario> eventos;
    BaseDeDatos dbTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuentas_por_pagar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);

        lblTotal = (TextView) findViewById(R.id.lblTotalCuentasPorPagar);
        lvCuentas = (ListView) findViewById(R.id.lvCuentasPorPagar);

        eventos = dbTienda.darEventosCalendario();
        AdaptadorFilaCalendario adaptador = new AdaptadorFilaCalendario(this, eventos);
        lvCuentas.setAdapter(adaptador);

        int total = 0;

        for (int i = 0; i < eventos.size(); i++) {
            EventoCalendario e = eventos.get(i);
            total += e.darMonto();
        }

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        lblTotal.setText(nf.format(total));
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
