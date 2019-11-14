package com.desarrollandoapps.senamarket.caja;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class OtrasObligacionesFragment extends DialogFragment implements Button.OnClickListener {

    java.util.Date utilDate;
    ArrayList<String> tipoEgreso;
    EditText txtOtroEgreso;
    EditText txtTipoEgreso;


    public OtrasObligacionesFragment() {
        // Required empty public constructor
    }

    public interface NuevoOtrosEgresos {
        void finalizarDialogoOtrosEgresos(String efectivo, String tipo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_otras_obligaciones, container);

        utilDate = new java.util.Date(); //fecha actual


        TextView lblFechaOtroEgreso = (TextView) view.findViewById(R.id.lblFechaOtroEgreso);
        txtOtroEgreso = (EditText) view.findViewById(R.id.txtOtroEgreso);

        txtOtroEgreso.addTextChangedListener(new TextWatcher() {
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

        //Cambiar a edittext
        txtTipoEgreso = (EditText) view.findViewById(R.id.txtTipoEgreso);


        Button btnIngresarOtroEgreso = (Button) view.findViewById(R.id.btnIngresarOtroEgreso);
        btnIngresarOtroEgreso.setOnClickListener(this);

        txtTipoEgreso.requestFocus();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Otros Egresos");

        SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formateadorHora = new SimpleDateFormat("hh:mm a");

        lblFechaOtroEgreso.setText(formateadorFecha.format(utilDate) + "\n" + formateadorHora.format(utilDate));

        return view;

    }

    @Override
    public void onClick(View v) {
        if(txtOtroEgreso.getText().toString().isEmpty() || txtTipoEgreso.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            NuevoOtrosEgresos activity = (NuevoOtrosEgresos) getActivity();
            activity.finalizarDialogoOtrosEgresos(txtOtroEgreso.getText().toString(), txtTipoEgreso.getText().toString());
            this.dismiss();
        }
    }
}
