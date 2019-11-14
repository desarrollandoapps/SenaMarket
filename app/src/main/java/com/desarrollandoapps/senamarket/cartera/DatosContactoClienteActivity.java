package com.desarrollandoapps.senamarket.cartera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DatosContactoClienteActivity extends AppCompatActivity {

    TextView txtCedula;
    TextView txtNombre;
    TextView txtDireccion;
    TextView txtTelefono;

    String cedula;
    String nombre;
    String direccion;
    String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_contacto_cliente);

        txtCedula = (TextView) findViewById(R.id.txtCedulaCliente);
        txtNombre = (TextView) findViewById(R.id.txtNombreCliente);
        txtDireccion = (TextView) findViewById(R.id.txtDireccionCliente);
        txtTelefono = (TextView) findViewById(R.id.txtTelefonoCliente);

        Intent i = getIntent();
        cedula = i.getExtras().getString("cedula");
        int cedulaInt = Integer.parseInt(cedula);
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat("#,###,###", simbolo);
        String cedulaFormateada = df.format(cedulaInt);
        txtCedula.setText(cedulaFormateada);

        nombre = i.getExtras().getString("nombre");
        txtNombre.setText(nombre);
        direccion = i.getExtras().getString("direccion");
        txtDireccion.setText(direccion);
        telefono = i.getExtras().getString("telefono");
        txtTelefono.setText(telefono);

    }
}
