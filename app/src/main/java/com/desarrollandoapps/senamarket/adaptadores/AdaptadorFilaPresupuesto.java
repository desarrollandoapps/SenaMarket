package com.desarrollandoapps.senamarket.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.presupuesto.Presupuesto;
import com.desarrollandoapps.senamarket.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by JoseO on 19/10/16.
 */

public class AdaptadorFilaPresupuesto extends BaseAdapter {

    Context context;
    ArrayList<Presupuesto> presupuestos;
    ArrayList<Integer> id;
    ArrayList<Date> fechas;
    ArrayList<String> categoria;
    ArrayList<Integer> valoresMaximos;
    ArrayList<Double> porcentajes;
    ArrayList<Integer> ejecutados;
    ArrayList<Integer> saldos;
    LayoutInflater inflater;

    public AdaptadorFilaPresupuesto(Context context, ArrayList<Presupuesto> presupuestos) {
        this.context = context;
        this.presupuestos = presupuestos;
        categoria = new ArrayList<>();
        valoresMaximos = new ArrayList<>();
        porcentajes = new ArrayList<>();
        ejecutados = new ArrayList<>();
        saldos = new ArrayList<>();
        id = new ArrayList<>();
        fechas = new ArrayList<>();
        inicializar();
    }

    private void inicializar() {
        Presupuesto presupuesto;

        for (int i = 0; i < presupuestos.size(); i++) {
            presupuesto = presupuestos.get(i);
            id.add(presupuesto.darId());
            categoria.add(presupuesto.darCategoria());
            fechas.add(presupuesto.darFecha());
            valoresMaximos.add(presupuesto.darMonto());
            porcentajes.add(presupuesto.darPorcentajeEjecutado());
            ejecutados.add(presupuesto.darEjecutado());
            saldos.add(presupuesto.darSaldo());
        }

    }

    @Override
    public int getCount() {
        return categoria.size();
    }

    @Override
    public Object getItem(int position) {
        return presupuestos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.fila_presupuesto, parent, false);

        TextView txtCategoria = (TextView) itemView.findViewById(R.id.txtCategoriaPresupuesto);
        TextView txtMaximo = (TextView) itemView.findViewById(R.id.txtMaximo);
        TextView txtPorcentajeEjecutado = (TextView) itemView.findViewById(R.id.txtPorcentajeEjecutado);
        TextView txtEjecutado = (TextView) itemView.findViewById(R.id.txtEjecutadoPresupuesto);
        TextView txtSaldo = (TextView) itemView.findViewById(R.id.txtSaldoPresupuesto);
        ProgressBar pbPresupuesto = (ProgressBar) itemView.findViewById(R.id.pbPresupuesto);

        txtCategoria.setText(categoria.get(position));

        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String maximoStr = nf.format(valoresMaximos.get(position));

        txtMaximo.setText(String.valueOf(maximoStr));

        int ejecutado = ejecutados.get(position);
        int saldo = saldos.get(position);

        if (saldo >= 0) {
            txtSaldo.setTextColor(itemView.getResources().getColor(R.color.colorAbono));
        } else {
            txtSaldo.setTextColor(itemView.getResources().getColor(R.color.colorFia));
            saldo = -saldo;
        }

        String saldoStr = nf.format(saldo);
        String ejecutadoStr = nf.format(ejecutado);
        txtSaldo.setText(saldoStr);
        txtEjecutado.setText(ejecutadoStr);

        DecimalFormat pf= new DecimalFormat("#%");
        String porcentajeStr = pf.format(porcentajes.get(position));
        txtPorcentajeEjecutado.setText(porcentajeStr);

        pbPresupuesto.setMax(valoresMaximos.get(position));
        pbPresupuesto.setProgress((int) (porcentajes.get(position) * valoresMaximos.get(position)));

        return itemView;
    }
}
