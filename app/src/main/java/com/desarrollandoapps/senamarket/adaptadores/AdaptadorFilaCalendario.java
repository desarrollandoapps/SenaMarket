package com.desarrollandoapps.senamarket.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.calendario.EventoCalendario;
import com.desarrollandoapps.senamarket.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by JoseO on 2/11/16.
 */

public class AdaptadorFilaCalendario extends BaseAdapter {

    Context context;
    ArrayList<EventoCalendario> eventos;
    LayoutInflater inflater;

    public AdaptadorFilaCalendario(Context context, ArrayList<EventoCalendario> eventos) {
        this.context = context;
        this.eventos = eventos;
    }

    @Override
    public int getCount() {
        return eventos.size();
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

        View itemView = inflater.inflate(R.layout.fila_calendario, parent, false);

        TextView txtFecha = (TextView) itemView.findViewById(R.id.txtFechaFilaCalendario);
        TextView txtNombre = (TextView) itemView.findViewById(R.id.txtNombreFilaCalendario);
        TextView txtMonto = (TextView) itemView.findViewById(R.id.txtMontoFilaCalendario);

        EventoCalendario ec = eventos.get(position);

        DateFormat df = DateFormat.getDateInstance();
        String fechaStr = df.format(ec.darFecha());
        txtFecha.setText(fechaStr);

        txtNombre.setText(ec.darAcreedor());

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        txtMonto.setText(nf.format(ec.darMonto()));

        return itemView;
    }
}
