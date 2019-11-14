package com.desarrollandoapps.senamarket.caja;

import android.app.DialogFragment;
import android.os.Bundle;
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

public class IngresarEfectivoFragment extends DialogFragment implements Button.OnClickListener {

    java.util.Date utilDate;
    EditText txtEfectivoEnCaja;
    View view;

    public IngresarEfectivoFragment() {

    }

    public interface NuevoIngresoDinero {
        void finalizarDialogoIngresarEfectivo(String efectivo);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_ingresar_efectivo, container);

        utilDate = new java.util.Date(); //fecha actual

        TextView lblFecha = (TextView) view.findViewById(R.id.lblFecha);
        txtEfectivoEnCaja = (EditText) view.findViewById(R.id.txtEfectivoEnCaja);
        Button btnIngresar = (Button) view.findViewById(R.id.btnIngresarEfectivo);
        btnIngresar.setOnClickListener(this);

        txtEfectivoEnCaja.requestFocus();
        getDialog().setTitle("Efectivo en caja");

        SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formateadorHora = new SimpleDateFormat("hh:mm a");

        lblFecha.setText(formateadorFecha.format(utilDate) + "\n" + formateadorHora.format(utilDate));


        txtEfectivoEnCaja.addTextChangedListener(new TextWatcher() {
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

        return view;
    }

    @Override
    public void onClick(View v) {
        if(txtEfectivoEnCaja.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            NuevoIngresoDinero activity = (NuevoIngresoDinero) getActivity();
            activity.finalizarDialogoIngresarEfectivo(txtEfectivoEnCaja.getText().toString());
            this.dismiss();
        }
    }
}
