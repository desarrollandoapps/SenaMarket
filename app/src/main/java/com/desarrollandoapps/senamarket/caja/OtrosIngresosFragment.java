package com.desarrollandoapps.senamarket.caja;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class OtrosIngresosFragment extends DialogFragment implements Button.OnClickListener {

    java.util.Date utilDate;
    ArrayList<String> tipoIngreso;
    EditText txtOtroIngreso;
    EditText txtTipoIngreso;


    public OtrosIngresosFragment() {
        // Required empty public constructor
    }

    public interface NuevoOtrosIngresos {
        void finalizarDialogoOtrosIngresos(String efectivo, String tipo);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_otros_ingresos, container);

        utilDate = new java.util.Date(); //fecha actual


        TextView lblFechaOtroIngreso = (TextView) view.findViewById(R.id.lblFechaOtroIngreso);
        txtOtroIngreso = (EditText) view.findViewById(R.id.txtOtroIngreso);

        txtOtroIngreso.addTextChangedListener(new TextWatcher() {
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
        txtTipoIngreso = (EditText) view.findViewById(R.id.txtTipoIngreso);


        Button btnIngresarOtroIngreso = (Button) view.findViewById(R.id.btnIngresarOtroIngreso);
        btnIngresarOtroIngreso.setOnClickListener(this);

        txtTipoIngreso.requestFocus();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Otros Ingresos");

        SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formateadorHora = new SimpleDateFormat("hh:mm a");

        lblFechaOtroIngreso.setText(formateadorFecha.format(utilDate) + "\n" + formateadorHora.format(utilDate));

        return view;
    }


    @Override
    public void onClick(View v) {
        if(txtOtroIngreso.getText().toString().isEmpty() || txtTipoIngreso.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            NuevoOtrosIngresos activity = (NuevoOtrosIngresos) getActivity();
            activity.finalizarDialogoOtrosIngresos(txtOtroIngreso.getText().toString(), txtTipoIngreso.getText().toString());
            this.dismiss();
        }
    }

}
