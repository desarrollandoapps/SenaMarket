package com.desarrollandoapps.senamarket.caja;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;


public class DineroEfectivoFragment extends DialogFragment implements Button.OnClickListener {

    EditText txtMonto;

    public DineroEfectivoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dinero_efectivo, container);
        txtMonto = (EditText) view.findViewById(R.id.txtMontoDineroEfectivoInicial);
        Button btnAgregarCajaFuerte = (Button) view.findViewById(R.id.btnAgregarDineroEfectivoInicial);
        btnAgregarCajaFuerte.setOnClickListener(this);
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

        getDialog().setTitle("Dinero en Efectivo");

        return view;
    }

    public interface NuevoDineroEfectivo {
        void finalizarDialogoDineroEfectivoInicial(String monto);
    }

    @Override
    public void onClick(View v) {
        if (txtMonto.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Debe ingresar todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            DineroEfectivoFragment.NuevoDineroEfectivo activity = (DineroEfectivoFragment.NuevoDineroEfectivo) getActivity();
            activity.finalizarDialogoDineroEfectivoInicial(txtMonto.getText().toString());
            this.dismiss();
        }
    }
}
