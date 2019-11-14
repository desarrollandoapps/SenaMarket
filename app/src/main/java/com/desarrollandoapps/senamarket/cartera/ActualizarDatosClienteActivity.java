package com.desarrollandoapps.senamarket.cartera;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.desarrollandoapps.senamarket.R;

public class ActualizarDatosClienteActivity extends AppCompatActivity implements Button.OnClickListener {

    String cedula;
    String nombre;
    String direccion;
    String telefono;

    EditText txtCedula;
    EditText txtNombre;
    EditText txtDireccion;
    EditText txtTelefono;
    Button btnActualizarCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_datos_cliente);

        txtCedula = (EditText) findViewById(R.id.txtCedulaActualizarCliente);
        txtNombre = (EditText) findViewById(R.id.txtNombreActualizarCliente);
        txtDireccion = (EditText) findViewById(R.id.txtDireccionActualizarCliente);
        txtTelefono = (EditText) findViewById(R.id.txtTelefonoActualizarCliente);
        btnActualizarCliente = (Button) findViewById(R.id.btnActualizarCliente);

        txtNombre.requestFocus();
        btnActualizarCliente.setOnClickListener(this);

        Intent i = getIntent();
        cedula = i.getExtras().getString("cedula");
        txtCedula.setText(cedula);
        nombre = i.getExtras().getString("nombre");
        txtNombre.setText(nombre);
        direccion = i.getExtras().getString("direccion");
        txtDireccion.setText(direccion);
        telefono = i.getExtras().getString("telefono");
        txtTelefono.setText(telefono);
    }

    @Override
    public void onClick(View v) {
        nombre = txtNombre.getText().toString();
        direccion = txtDireccion.getText().toString();
        telefono = txtTelefono.getText().toString();

        finish();
    }

    @Override
    public void finish() {
        Intent i = new Intent();
        if (nombre != "") {
            i.putExtra("nombre", nombre);
        }
        if (direccion != "") {
            i.putExtra("direccion", direccion);
        }
        if (telefono != "") {
            i.putExtra("telefono", telefono);
        }
        setResult(RESULT_OK, i);
        super.finish();
    }
}
