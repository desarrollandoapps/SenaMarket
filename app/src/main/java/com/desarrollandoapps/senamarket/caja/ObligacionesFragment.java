package com.desarrollandoapps.senamarket.caja;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ObligacionesFragment extends DialogFragment implements Button.OnClickListener{

    java.util.Date utilDate;
    ArrayList<String> tipoObligacion;
    EditText txtObligacion;
    Spinner cboTipoObligacion;
    TextView lblFechaObligacion;


    public ObligacionesFragment() {
        // Required empty public constructor
    }

    public interface NuevaObligacion {
        void finalizarDialogoObligaciones(String efectivo, String clase);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tipoObligacion = new ArrayList<>();
        tipoObligacion.add("Proveedores");
        tipoObligacion.add("Bancos");
        tipoObligacion.add("Arriendo");
        tipoObligacion.add("Servicios públicos");

        View view = inflater.inflate(R.layout.fragment_obligaciones, container);

        utilDate = new java.util.Date(); //fecha actual

        lblFechaObligacion = (TextView) view.findViewById(R.id.lblFechaObligacion);
        txtObligacion = (EditText) view.findViewById(R.id.txtMontoObligacion);

        txtObligacion.addTextChangedListener(new TextWatcher() {
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

        cboTipoObligacion = (Spinner) view.findViewById(R.id.cboTipoObligacion);
        ArrayAdapter spinner_adapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, tipoObligacion);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboTipoObligacion.setAdapter(spinner_adapter);


        Button btnIngresarObligacion = (Button) view.findViewById(R.id.btnIngresarObligacion);
        btnIngresarObligacion.setOnClickListener(this);

        txtObligacion.requestFocus();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Obligaciones");

        SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formateadorHora = new SimpleDateFormat("hh:mm a");

        lblFechaObligacion.setText(formateadorFecha.format(utilDate) + "\n" + formateadorHora.format(utilDate));

        return view;

    }

    @Override
    public void onClick(View v) {
        if(txtObligacion.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            NuevaObligacion activity = (NuevaObligacion) getActivity();
            activity.finalizarDialogoObligaciones(txtObligacion.getText().toString(), cboTipoObligacion.getSelectedItem().toString());
            this.dismiss();
        }
    }

}
