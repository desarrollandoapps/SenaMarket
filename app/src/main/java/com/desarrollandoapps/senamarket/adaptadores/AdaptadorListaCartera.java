package com.desarrollandoapps.senamarket.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.cartera.Cliente;
import com.desarrollandoapps.senamarket.R;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by JoseO on 13/10/16.
 */

public class AdaptadorListaCartera extends BaseAdapter {

    //Atributos
    Context context;
    ArrayList<Cliente> clientes;
    LayoutInflater inflater;

    public AdaptadorListaCartera(Context context, ArrayList<Cliente> clientes) {
        this.context = context;
        this.clientes = clientes;
    }

    @Override
    public int getCount() {
        return clientes.size();
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
        TextView txtNombre;
        TextView txtDeuda;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.fila_cartera, parent, false);

        Cliente cliente = clientes.get(position);

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String deudaStr = nf.format(cliente.darDeuda());

        txtNombre = (TextView) itemView.findViewById(R.id.txtNombreFilaCartera);
        txtNombre.setText(cliente.darNombre());
        txtDeuda = (TextView) itemView.findViewById(R.id.txtDeudaFilaCartera);
        txtDeuda.setText(deudaStr);

        return itemView;
    }
}
