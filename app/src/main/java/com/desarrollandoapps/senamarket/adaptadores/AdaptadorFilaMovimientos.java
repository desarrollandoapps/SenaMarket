package com.desarrollandoapps.senamarket.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.caja.MovimientoCaja;
import com.desarrollandoapps.senamarket.R;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by JoseO on 10/11/16.
 */

public class AdaptadorFilaMovimientos extends BaseAdapter {

    Context context;
    ArrayList<MovimientoCaja> movimientos;
    LayoutInflater inflater;

    TextView txtFecha;
    TextView txtMonto;
    TextView txtTipoMovimiento;

    public AdaptadorFilaMovimientos(Context context, ArrayList<MovimientoCaja> movimientos) {
        this.context = context;
        this.movimientos = movimientos;
    }

    @Override
    public int getCount() {
        return movimientos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.fila_movimientos, parent, false);

        txtFecha = (TextView) itemView.findViewById(R.id.txtFechaMovimiento);
        txtMonto = (TextView) itemView.findViewById(R.id.txtMontoMovimiento);
        txtTipoMovimiento = (TextView) itemView.findViewById(R.id.txtTipoMovimiento);

        MovimientoCaja movimiento = movimientos.get(position);

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        int montInt = Integer.parseInt(movimiento.darMonto());

        txtFecha.setText(movimiento.darFecha());
        txtMonto.setText(nf.format(montInt));
        txtTipoMovimiento.setText(movimiento.darClaseMovimiento());

        if (movimiento.darTipoMovimiento().equals("Entrada")) {
            txtFecha.setTextColor(itemView.getResources().getColor(R.color.colorAbono));
            txtMonto.setTextColor(itemView.getResources().getColor(R.color.colorAbono));
            txtTipoMovimiento.setTextColor(itemView.getResources().getColor(R.color.colorAbono));
        } else {
            txtFecha.setTextColor(itemView.getResources().getColor(R.color.colorFia));
            txtMonto.setTextColor(itemView.getResources().getColor(R.color.colorFia));
            txtTipoMovimiento.setTextColor(itemView.getResources().getColor(R.color.colorFia));
        }

        return itemView;
    }
}
