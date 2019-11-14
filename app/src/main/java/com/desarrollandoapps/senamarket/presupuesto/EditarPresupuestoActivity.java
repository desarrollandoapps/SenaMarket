package com.desarrollandoapps.senamarket.presupuesto;

import android.content.Intent;
import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;

public class EditarPresupuestoActivity extends AppCompatActivity {

    TextView txtCategoria;
    TextView txtMonto;

    BaseDeDatos dbtienda;
    String categoria;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_presupuesto);

        txtCategoria = (TextView) findViewById(R.id.txtCategoriaPresupuesto);
        txtMonto = (TextView) findViewById(R.id.txtMontoPresupuesto);

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

        Intent i = getIntent();
        if (i.hasExtra("categoria")) {
            categoria = i.getExtras().getString("categoria");
            txtCategoria.setText(categoria);
        }
        if (i.hasExtra("id")) {
            id = i.getExtras().getInt("id");
        }

        dbtienda = new BaseDeDatos(this, "Tienda", null, 1);

    }

    public void editarPresupuesto(View v) {

        if (!txtMonto.getText().toString().isEmpty()) {
            String monto = txtMonto.getText().toString();

            try {
                dbtienda.modificarPresupuesto(id, monto);
                Toast.makeText(this, "Presupuesto modificado", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(this, "No se ha modificado el presupuesto", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(this, "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        Intent i = new Intent();
        setResult(RESULT_OK, i);
        super.finish();
    }
}
