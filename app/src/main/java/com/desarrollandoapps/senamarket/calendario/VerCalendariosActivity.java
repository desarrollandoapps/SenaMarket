package com.desarrollandoapps.senamarket.calendario;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;
import com.desarrollandoapps.senamarket.adaptadores.AdaptadorFilaCalendario;

import java.util.ArrayList;

public class VerCalendariosActivity extends AppCompatActivity implements ListView.OnItemClickListener, ListView.OnItemLongClickListener {

    ListView lvCalendario;
    ArrayList<EventoCalendario> eventos;
    BaseDeDatos dbTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_calendarios);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvCalendario = (ListView) findViewById(R.id.lvCalendario);

        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);

        refrescar();

        lvCalendario.setOnItemClickListener(this);
        lvCalendario.setOnItemLongClickListener(this);
    }

    private void refrescar() {
        eventos = dbTienda.darEventosCalendario();
        AdaptadorFilaCalendario adaptador = new AdaptadorFilaCalendario(this, eventos);
        lvCalendario.setAdapter(adaptador);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sena Market")
                .setMessage("¿Pagar?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        EventoCalendario evento = eventos.get(position);
                        if (dbTienda.pagarEvento(evento)) {
                            Toast.makeText(VerCalendariosActivity.this, "Pagado", Toast.LENGTH_SHORT).show();
                            refrescar();
                        } else {
                            Toast.makeText(VerCalendariosActivity.this, "No se ha pagado", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sena Market")
                .setMessage("¿Borrar el evento?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        EventoCalendario evento = eventos.get(position);
                        if (dbTienda.eliminarEvento(String.valueOf(evento.darId()))) {
                            Toast.makeText(VerCalendariosActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();
                            refrescar();
                        } else {
                            Toast.makeText(VerCalendariosActivity.this, "No se ha eliminado", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        return true;
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
