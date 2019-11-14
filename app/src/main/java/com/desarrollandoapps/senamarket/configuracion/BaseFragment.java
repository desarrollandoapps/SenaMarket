package com.desarrollandoapps.senamarket.configuracion;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

public class BaseFragment extends DialogFragment implements Button.OnClickListener {

    EditText txtMonto;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container);
        TextView lblBaseActual = (TextView) view.findViewById(R.id.lblBaseActual);
        BaseDeDatos dbTienda = new BaseDeDatos(getContext(), "Tienda", null, 1);
        NumberFormat nf = NumberFormat.getCurrencyInstance();

        lblBaseActual.setText(lblBaseActual.getText() + nf.format(dbTienda.darValorBase()));
        txtMonto = (EditText) view.findViewById(R.id.txtMontoBase);
        Button btnAgregarBase = (Button) view.findViewById(R.id.btnAgregarBase);
        btnAgregarBase.setOnClickListener(this);
        txtMonto.requestFocus();

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

        getDialog().setTitle("Dinero de Base");

        return view;
    }

    public interface NuevaBase {
        void finalizarDialogoBase(String monto);
    }

    @Override
    public void onClick(View v) {
        if (txtMonto.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            NuevaBase activity = (NuevaBase) getActivity();
            activity.finalizarDialogoBase(txtMonto.getText().toString());
            this.dismiss();
        }
    }
}
