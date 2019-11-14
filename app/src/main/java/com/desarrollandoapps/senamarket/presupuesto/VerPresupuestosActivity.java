package com.desarrollandoapps.senamarket.presupuesto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;
import com.desarrollandoapps.senamarket.adaptadores.AdaptadorFilaPresupuesto;
import com.desarrollandoapps.senamarket.presupuesto.EditarPresupuestoActivity;
import com.desarrollandoapps.senamarket.presupuesto.Presupuesto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VerPresupuestosActivity extends AppCompatActivity implements ListView.OnItemClickListener,
        ListView.OnItemLongClickListener {

    ListView lvPresupuestos;
    ArrayList<Presupuesto> presupuestos;
    ArrayList<Presupuesto> presupuestosMes;
    BaseDeDatos dbTienda;

    public static final int REQUEST_CODE_EDITAR_PRESUPUESTO = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_presupuestos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvPresupuestos = (ListView) findViewById(R.id.lvPresupuestos);

        actualizar();
        lvPresupuestos.setOnItemClickListener(this);
        lvPresupuestos.setOnItemLongClickListener(this);
    }

    private void actualizar() {
        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
        Calendar calendar = Calendar.getInstance();
        Date hoy = calendar.getTime();

        if (!dbTienda.existenPresupuestosFecha(String.valueOf(Calendar.MONTH))) {
            Presupuesto p = dbTienda.darPresupuesto("Proveedores");
            dbTienda.agregarPresupuesto(new Presupuesto(hoy, "Proveedores", p.darMonto(), 0));
            p = dbTienda.darPresupuesto("Bancos");
            dbTienda.agregarPresupuesto(new Presupuesto(hoy, "Bancos", p.darMonto(), 0));
            p = dbTienda.darPresupuesto("Arriendo");
            dbTienda.agregarPresupuesto(new Presupuesto(hoy, "Arriendo", p.darMonto(), 0));
            p = dbTienda.darPresupuesto("Servicios públicos");
            dbTienda.agregarPresupuesto(new Presupuesto(hoy, "Servicios públicos", p.darMonto(), 0));
        }
        presupuestos = dbTienda.darPresupuestos();
        presupuestosMes = dbTienda.darPresupuestosMes();

        AdaptadorFilaPresupuesto adaptador = new AdaptadorFilaPresupuesto(this, presupuestosMes);
        lvPresupuestos.setAdapter(adaptador);
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
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


        Intent i = new Intent(this, EditarPresupuestoActivity.class);
        Presupuesto presupuesto = (Presupuesto) parent.getItemAtPosition(position);
        i.putExtra("categoria", presupuesto.darCategoria());
        i.putExtra("id", presupuesto.darId());
        startActivityForResult(i, REQUEST_CODE_EDITAR_PRESUPUESTO);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_EDITAR_PRESUPUESTO && resultCode == RESULT_OK) {
            actualizar();
        }
    }
}
