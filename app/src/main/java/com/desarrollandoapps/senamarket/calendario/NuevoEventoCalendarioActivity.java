package com.desarrollandoapps.senamarket.calendario;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class NuevoEventoCalendarioActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private int anio;
    private int mes;
    private int dia;
    private BaseDeDatos dbTienda;

    private EditText txtFechaEvento;
    private EditText txtMonto;
    private EditText txtAcreedor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_evento_calendario);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtFechaEvento = (EditText) findViewById(R.id.txtFechaEvento);
        txtAcreedor = (EditText) findViewById(R.id.txtNombreAcreedor);
        txtMonto = (EditText) findViewById(R.id.txtMontoEventoCalendario);

        txtMonto.addTextChangedListener(new TextWatcher() {
            boolean isEditing;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isEditing) return;
                isEditing = true;

                try {
                    String str = s.toString().replaceAll("[^\\d]", "");
                    int s1 = Integer.parseInt(str);

                    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
                    otherSymbols.setDecimalSeparator(',');
                    otherSymbols.setGroupingSeparator('.');
                    DecimalFormat df = new DecimalFormat("###,###", otherSymbols);

                    s.replace(0, s.length(), df.format(s1));


                } catch (Exception e) {

                }

                isEditing = false;
            }
        });

        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);

        Calendar calendar = Calendar.getInstance();

        anio = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH) + 1;
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        mostrarFecha();

    }

    public void mostrarFecha() {
        txtFechaEvento.setText(dia + "/" + mes + "/" + anio);
    }

    public void mostrarCalendario(View v) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "Fecha");
    }

    public void agregarEvento(View v) {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, dia);
        cal.set(Calendar.MONTH, mes);
        cal.set(Calendar.YEAR, anio);
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);

        Date fecha = cal.getTime();

        if (txtMonto.getText().toString().isEmpty() || txtFechaEvento.getText().toString().isEmpty() ||
                txtAcreedor.getText().toString().isEmpty()) {
            Toast.makeText(this, "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        } else {

            String acreedor = txtAcreedor.getText().toString();
            String montoStr = txtMonto.getText().toString();
            montoStr = montoStr.replace(".", "");
            int monto = Integer.parseInt(montoStr);
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            montoStr = format.format(monto);
            EventoCalendario ec = new EventoCalendario(fecha, acreedor, monto);

            if (dbTienda.agregarEventoCalendario(ec)) {
                Intent i = new Intent(Intent.ACTION_EDIT);
                i.setType("vnd.android.cursor.item/event");

                i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
                i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis() + 60 * 60 * 1000);

                i.putExtra(CalendarContract.Events.ALL_DAY, false);
                i.putExtra(CalendarContract.Events.TITLE, "Pago a " + acreedor);
                i.putExtra(CalendarContract.Events.DESCRIPTION, "Debe pagar a " + acreedor + " la suma de " + montoStr);

                startActivityForResult(i, 0);

                Toast.makeText(this, "Evento agregado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se ha agregado el evento", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    public void agregarEventoAlCalendario() {

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DAY_OF_MONTH, dia);
        cal.set(Calendar.MONTH, mes);
        cal.set(Calendar.YEAR, anio);

        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);

        Intent i = new Intent(Intent.ACTION_EDIT);
        i.setType("vnd.android.cursor.item/event");

        i.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis());
        i.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis()+60*60*1000);

        i.putExtra(CalendarContract.Events.ALL_DAY, false);
        i.putExtra(CalendarContract.Events.TITLE, "Título de vuestro evento");
        i.putExtra(CalendarContract.Events.DESCRIPTION, "Descripción");

        startActivity(i);
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
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar c = new GregorianCalendar(year, monthOfYear, dayOfMonth);
        anio = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);
        mostrarFecha();
    }

    public static class DatePickerFragment extends DialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int anio = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), anio, mes, dia);
        }
    }
}
