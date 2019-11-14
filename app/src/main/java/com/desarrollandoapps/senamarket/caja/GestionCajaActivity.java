package com.desarrollandoapps.senamarket.caja;

import android.content.Intent;
import android.database.Cursor;
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
import com.desarrollandoapps.senamarket.configuracion.BaseFragment;

public class GestionCajaActivity extends AppCompatActivity implements BaseFragment.NuevaBase, IngresarEfectivoFragment.NuevoIngresoDinero,
        OtrosIngresosFragment.NuevoOtrosIngresos, ObligacionesFragment.NuevaObligacion, OtrasObligacionesFragment.NuevoOtrosEgresos,
        DineroEfectivoFragment.NuevoDineroEfectivo {

    SQLiteDatabase db;
    BaseDeDatos dbTienda;
    int dineroInicial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_caja);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);

        if(!dbTienda.hayBase()) {
            DineroEfectivoFragment dialogo = new DineroEfectivoFragment();
            dialogo.show(getSupportFragmentManager(), "dineroInicial");
            android.app.Fragment fragment = getFragmentManager().findFragmentByTag("dineroInicial");
            if (fragment != null) {
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }

    }

    public void abrirEfectivoEnCaja(View view) {

        IngresarEfectivoFragment dialogoActivity = new IngresarEfectivoFragment();
        dialogoActivity.show(getFragmentManager(), "ingresoEfectivo");

        android.app.Fragment fragment = getFragmentManager().findFragmentByTag("ingresoEfectivo");
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    public void abrirOtrosIngresos(View v) {

        OtrosIngresosFragment dialogo = new OtrosIngresosFragment();
        dialogo.show(getFragmentManager(), "otrosIngresos");

        android.app.Fragment fragment = getFragmentManager().findFragmentByTag("otrosIngresos");
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }

    }

    public void abrirObligaciones(View v) {
        ObligacionesFragment dialogo = new ObligacionesFragment();
        dialogo.show(getFragmentManager(), "Obligaciones");

        android.app.Fragment fragment = getFragmentManager().findFragmentByTag("Obligaciones");
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    public void abrirOtrasObligaciones(View v) {

        OtrasObligacionesFragment dialogo = new OtrasObligacionesFragment();
        dialogo.show(getFragmentManager(),"OtrasObligaciones");

        android.app.Fragment fragment = getFragmentManager().findFragmentByTag("OtrasObligaciones");
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }
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
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finalizarDialogoIngresarEfectivo(String efectivo) {

        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
        db = dbTienda.getWritableDatabase();

        try {
            efectivo = efectivo.replace(".", "");

            int otrosIngresos = dbTienda.darOtrosIngresos();
            int efectivoInt = Integer.parseInt(efectivo) - otrosIngresos - dbTienda.darValorBase();
            db.execSQL("INSERT INTO Movimiento (tipoMovimiento, claseMovimiento, monto) VALUES ('Entrada', 'Cierre de Caja'," + efectivoInt + ")");

            dbTienda.cierreDeCaja(efectivo);

            Toast.makeText(this, "Efectivo insertado", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finalizarDialogoOtrosIngresos(String efectivo, String tipo) {
        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
        db = dbTienda.getWritableDatabase();
        try {
            efectivo = efectivo.replace(".", "");
            int efectivoInt = Integer.parseInt(efectivo);
            db.execSQL("INSERT INTO Movimiento (tipoMovimiento, claseMovimiento, monto) VALUES ('Entrada', '" +
                    tipo + "'," + efectivoInt + ")");

            Toast.makeText(this, "Otro Ingreso insertado", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finalizarDialogoObligaciones(String efectivo, String clase) {
        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
        db = dbTienda.getWritableDatabase();
        try {
            efectivo = efectivo.replace(".", "");
            int efectivoInt = Integer.parseInt(efectivo);
            if (dbTienda.darValorCajaFuerte() > efectivoInt) {
                db.execSQL("INSERT INTO Movimiento (tipoMovimiento, claseMovimiento, monto) VALUES ('Salida', '" +
                        clase + "'," + efectivoInt + ")");

                int totalObligacionMes = 0;

                Cursor cursor = db.rawQuery("SELECT sum(monto) FROM Movimiento WHERE strftime('%m', fecha) = strftime('%m', date('now')) " +
                        "AND claseMovimiento = '" + clase + "'", null);

                if (cursor.moveToFirst()) {
                    do {
                        totalObligacionMes += cursor.getInt(0);
                    } while (cursor.moveToNext());
                }

                db.execSQL("UPDATE Presupuesto SET ejecutado = " + totalObligacionMes + " WHERE strftime('%m', fecha) = strftime('%m', date('now')) " +
                        "AND strftime('%Y', fecha) = strftime('%Y', date('now')) " +
                        "AND categoria = '" + clase + "'");

                int cajaFuerte = dbTienda.darValorCajaFuerte() - efectivoInt;
                db.execSQL("INSERT INTO MovCajaFuerte(tipoMovimiento, monto) VALUES('Salida', " + efectivo + ")");
                db.execSQL("UPDATE ValoresFijos SET cajaFuerte = " + cajaFuerte);

                Toast.makeText(this, "Obligación ingresada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No hay efectivo para cancelar la obligación", Toast.LENGTH_LONG).show();
            }

        } catch (SQLException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finalizarDialogoOtrosEgresos(String efectivo, String tipo) {
        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
        db = dbTienda.getWritableDatabase();
        try {
            efectivo = efectivo.replace(".", "");
            int efectivoInt = Integer.parseInt(efectivo);
            if (dbTienda.darValorCajaFuerte() > efectivoInt) {
                db.execSQL("INSERT INTO Movimiento (tipoMovimiento, claseMovimiento, monto) VALUES ('Salida', '" +
                        tipo + "'," + efectivoInt + ")");

                int cajaFuerte = dbTienda.darValorCajaFuerte() - efectivoInt;

                db.execSQL("INSERT INTO MovCajaFuerte(tipoMovimiento, monto) VALUES('Salida', " + efectivo + ")");
                db.execSQL("UPDATE ValoresFijos SET cajaFuerte = " + cajaFuerte);

                Toast.makeText(this, "Obligación ingresada", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No hay efectivo para cancelar la obligación", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finalizarDialogoDineroEfectivoInicial(String monto) {

        try {
            monto = monto.replace(".", "");
            dineroInicial = Integer.parseInt(monto);

            dbTienda.crearCajaFuerte(dineroInicial);

            BaseFragment dialogo = new BaseFragment();
            dialogo.show(getSupportFragmentManager(), "base");

            android.app.Fragment fragment = getFragmentManager().findFragmentByTag("base");
            if (fragment != null) {
                getFragmentManager().beginTransaction().remove(fragment).commit();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finalizarDialogoBase(String monto) {
        db = dbTienda.getWritableDatabase();

        try {
            monto = monto.replace(".", "");
            int montoInt = Integer.parseInt(monto);

            int montoCF = dineroInicial - montoInt;
            db.execSQL("UPDATE ValoresFijos SET inicialCajaFuerte = " + montoCF + ", cajaFuerte = " + montoCF);

            if (dbTienda.crearBase(montoInt)) {
                Toast.makeText(this, "Registrado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se ha Registrado", Toast.LENGTH_SHORT).show();
            }


        } catch (SQLException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
