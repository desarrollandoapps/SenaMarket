package com.desarrollandoapps.senamarket.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.caja.MovimientoCaja;
import com.desarrollandoapps.senamarket.R;

import java.util.ArrayList;

/**
 * Created by JoseO on 7/10/16.
 */

public class AdaptadorMovimientosCliente extends BaseAdapter {

    Context context;
    ArrayList<MovimientoCaja> movimientos;
    LayoutInflater inflater;

    TextView txtFecha;
    TextView txtMonto;

    public AdaptadorMovimientosCliente(Context context, ArrayList<MovimientoCaja> movimientos) {
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

        View itemView = inflater.inflate(R.layout.fila_cartera_cliente, parent, false);

        txtFecha = (TextView) itemView.findViewById(R.id.txtFechaCarteraCliente);
        txtMonto = (TextView) itemView.findViewById(R.id.txtMontoCartera);

        MovimientoCaja movimiento = movimientos.get(position);

        txtFecha.setText(movimiento.darFecha());
        txtMonto.setText(movimiento.darMonto());

        if (movimiento.darTipoMovimiento().equals("Abono")) {
            txtFecha.setTextColor(itemView.getResources().getColor(R.color.colorAbono));
            txtMonto.setTextColor(itemView.getResources().getColor(R.color.colorAbono));
        } else {
            txtFecha.setTextColor(itemView.getResources().getColor(R.color.colorFia));
            txtMonto.setTextColor(itemView.getResources().getColor(R.color.colorFia));
        }

        return itemView;
    }
}
