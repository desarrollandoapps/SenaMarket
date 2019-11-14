package com.desarrollandoapps.senamarket.informes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;
import com.desarrollandoapps.senamarket.adaptadores.AdaptadorFilaMovimientos;
import com.desarrollandoapps.senamarket.caja.MovimientoCaja;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MovimientosActivity extends AppCompatActivity {

    ListView lvMovimientosCaja;
    LinearLayout layout;
    TextView lblFNC;

    BaseDeDatos dbTienda;
    int dia, mes, anio;
    boolean mensual;
    ArrayList<MovimientoCaja> movimientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimientos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvMovimientosCaja = (ListView) findViewById(R.id.lvMovimientosCaja);
        lblFNC = (TextView) findViewById(R.id.lblFlujoNetoCaja);
        layout = (LinearLayout) findViewById(R.id.layoutFNC);

        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
        Intent i = getIntent();

        if (i.hasExtra("dia") && i.hasExtra("mes") && i.hasExtra("anio") && i.hasExtra("diario") ) {
            dia = i.getExtras().getInt("dia");
            mes = i.getExtras().getInt("mes");
            anio = i.getExtras().getInt("anio");
            mensual = i.getExtras().getBoolean("diario");
        }

        String mesStr= convertirMes(mes);
        String diaStr= convertirMes(dia);

        int ingresos = dbTienda.calcularIngresosMes(mesStr, String.valueOf(anio));
        int egresos = dbTienda.calcularEgresosMes(mesStr, String.valueOf(anio));
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        int flujoNetoCaja = ingresos - egresos;
        movimientos = dbTienda.darMovimientosCajaMes(diaStr, mesStr, String.valueOf(anio));

        if (dbTienda.esMesInicio(mesStr, String.valueOf(anio))) {
            flujoNetoCaja += dbTienda.darValorInicialCajaFuerte();
            movimientos.add(new MovimientoCaja("Entrada", "", String.valueOf(dbTienda.darValorInicialCajaFuerte()), "Valor inicial Caja"));
        }

        if (flujoNetoCaja > 0){
            layout.setBackgroundColor(getResources().getColor(R.color.colorAbono));
        } else {
            layout.setBackgroundColor(getResources().getColor(R.color.colorFia));
            flujoNetoCaja = - flujoNetoCaja;
        }

        lblFNC.setText(nf.format(flujoNetoCaja));
        AdaptadorFilaMovimientos adaptador = new AdaptadorFilaMovimientos(this, movimientos);
        lvMovimientosCaja.setAdapter(adaptador);


    }

    private String convertirMes(int mes) {

        String respuesta = "";

        if (mes < 10) {
            respuesta = "0" + mes;
        } else {
            respuesta = String.valueOf(mes);
        }

        return respuesta;
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
