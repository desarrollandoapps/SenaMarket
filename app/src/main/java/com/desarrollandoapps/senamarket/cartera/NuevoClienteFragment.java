package com.desarrollandoapps.senamarket.cartera;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


/**
 * A simple {@link Fragment} subclass.
 */
public class NuevoClienteFragment extends DialogFragment implements Button.OnClickListener {

    Button btnIngresarNuevoCliente;
    EditText txtCedula;
    EditText txtNombre;
    EditText txtDireccion;
    EditText txtTelefono;
    EditText txtDeuda;

    public NuevoClienteFragment() {
        // Required empty public constructor
    }

    public interface nuevoCliente {
        void finalizarDialogoNuevoCliente(String cedula, String nombre, String direccion, String telefono, int deuda);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nuevo_cliente, container, false);

        getDialog().setTitle("Nuevo Cliente");

        btnIngresarNuevoCliente = (Button) view.findViewById(R.id.btnIngresarNuevoCliente);
        txtCedula = (EditText) view.findViewById(R.id.txtCedulaNuevoCliente);
        txtNombre = (EditText) view.findViewById(R.id.txtNombreNuevoCliente);
        txtDireccion = (EditText) view.findViewById(R.id.txtDireccionNuevoCliente);
        txtTelefono = (EditText) view.findViewById(R.id.txtTelefonoNuevoCliente);
        txtDeuda = (EditText) view.findViewById(R.id.txtDeudaNuevoCliente);

        txtDeuda.addTextChangedListener(new TextWatcher() {
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

        btnIngresarNuevoCliente.setOnClickListener(this);
        txtCedula.requestFocus();

        return view;
    }

    @Override
    public void onClick(View v) {
        if(txtCedula.getText().toString().isEmpty() || txtNombre.getText().toString().isEmpty() || txtDeuda.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            nuevoCliente activity = (nuevoCliente) getActivity();
            activity.finalizarDialogoNuevoCliente(txtCedula.getText().toString().trim(),
                    txtNombre.getText().toString().trim(), txtDireccion.getText().toString().trim(), txtTelefono.getText().toString().trim(),
                    Integer.parseInt(txtDeuda.getText().toString().replace(".", "")));
            this.dismiss();
        }
    }
}
