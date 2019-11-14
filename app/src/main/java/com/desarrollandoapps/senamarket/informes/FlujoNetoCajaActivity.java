package com.desarrollandoapps.senamarket.informes;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FlujoNetoCajaActivity extends AppCompatActivity {

    TextView txtEgresos;
    TextView txtIngresos;
    Spinner spTipoInforme;
    TextView txtMes;
    ImageButton btnAtras;
    ImageButton btnAdelante;
    GraphView graficoFNC;
    TextView txtFlujoNC;

    int dia;
    int mes;
    int anio;
    BaseDeDatos db;
    Calendar calendar;
    boolean banderaDia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flujo_neto_caja);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtEgresos = (TextView) findViewById(R.id.txtEgresos);
        txtIngresos = (TextView) findViewById(R.id.txtIngresos);
        txtMes = (TextView) findViewById(R.id.txtMesFNC);
        spTipoInforme = (Spinner) findViewById(R.id.spTipoInforme);
        btnAtras = (ImageButton) findViewById(R.id.btnMesAtras);
        btnAdelante = (ImageButton) findViewById(R.id.btnMesAdelante);
        graficoFNC = (GraphView) findViewById(R.id.graficoFNC);
        txtFlujoNC = (TextView) findViewById(R.id.txtFlujoNetoCaja);

        String[] valores = {"Mensual", "Diario"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores);
        spTipoInforme.setAdapter(adapter);

        db = new BaseDeDatos(this, "Tienda", null, 1);
        calendar = Calendar.getInstance();
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH) + 1;
        anio = calendar.get(Calendar.YEAR);

        banderaDia = false;

        actualizarDatos(mes, anio);


        spTipoInforme.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0) {
                    banderaDia = false;
                    actualizarDatos(mes, anio);
                } else if(position == 1) {
                    banderaDia = true;
                    actualizarDatosADia(dia, mes, anio);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String mesStr(int mes) {
        String mesStr = "";

        switch (mes) {
            case 1:
                mesStr = "ENERO";
                break;
            case 2:
                mesStr = "FEBRERO";
                break;
            case 3:
                mesStr = "MARZO";
                break;
            case 4:
                mesStr = "ABRIL";
                break;
            case 5:
                mesStr = "MAYO";
                break;
            case 6:
                mesStr = "JUNIO";
                break;
            case 7:
                mesStr = "JULIO";
                break;
            case 8:
                mesStr = "AGOSTO";
                break;
            case 9:
                mesStr = "SEPTIEMBRE";
                break;
            case 10:
                mesStr = "OCTUBRE";
                break;
            case 11:
                mesStr = "NOVIEMBRE";
                break;
            case 12:
                mesStr = "DICIEMBRE";
                break;
        }
        return mesStr;
    }

    private String diaStr(int dia) {
        String respuesta = "0";

        if (dia < 10) {
            respuesta = respuesta.concat(String.valueOf(dia));
        } else {
            respuesta = String.valueOf(dia);
        }

        return respuesta;
    }

    private void configurarGrafico(ArrayList<DataPoint> ingreso, ArrayList<DataPoint> egreso) {

        DataPoint ingresos[] = new DataPoint[ingreso.size()];
        DataPoint egresos[] = new DataPoint[egreso.size()];

        double maxY = 0;

        for (int i = 0; i < ingreso.size(); i++) {
            ingresos[i] = ingreso.get(i);
            maxY = ingresos[i].getY();
        }
        for (int i = 0; i < egreso.size(); i++) {
            egresos[i] = egreso.get(i);
            if ( egresos[i].getY() > maxY) {
                maxY = egresos[i].getY();
            }
        }

        BarGraphSeries<DataPoint> serieIngresos = new BarGraphSeries<DataPoint>(ingresos);

        BarGraphSeries<DataPoint> serieEgresos = new BarGraphSeries<DataPoint>(egresos);

        //Dar estilo a la serie2 (Barras)
        //Color
        serieIngresos.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) { return Color.rgb(89, 181, 72);}
        });
        //Espaciado
        serieIngresos.setSpacing(30);
        //Animación
        serieIngresos.setAnimated(true);


        serieEgresos.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.RED;
            }
        });
        serieEgresos.setSpacing(30);
        serieEgresos.setAnimated(true);

        if(!graficoFNC.getSeries().isEmpty()) {
            graficoFNC.removeAllSeries();
        }

        // Configurar limite X manualmente
        graficoFNC.getViewport().setXAxisBoundsManual(true);
        graficoFNC.getViewport().setMinX(0);
        graficoFNC.getViewport().setMaxX(3);

        graficoFNC.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);

         // Configurar limite Y manualmente
        graficoFNC.getViewport().setYAxisBoundsManual(true);
        graficoFNC.getViewport().setMinY(0);
        graficoFNC.getViewport().setMaxY(1.1 * maxY);

        //Configurar leyenda
        serieIngresos.setTitle("Ingresos");
        serieEgresos.setTitle("Egresos");
        serieIngresos.setColor(Color.rgb(89, 181, 72));
        serieEgresos.setColor(Color.RED);
        graficoFNC.getLegendRenderer().setVisible(true);
        graficoFNC.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graficoFNC.getLegendRenderer().setBackgroundColor(Color.WHITE);

        //Agregar a gráfico
        graficoFNC.addSeries(serieIngresos);
        graficoFNC.addSeries(serieEgresos);
    }

    private void actualizarDatos(int mesInt, int anioInt) {

        String mesStr= convertirMes(mesInt);
        int ingresos = db.calcularIngresosMes(mesStr, String.valueOf(anioInt));
        int egresos = db.calcularEgresosMes(mesStr, String.valueOf(anioInt));
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String ingresoStr = nf.format(ingresos);
        String egresoStr = nf.format(egresos);

        int flujoNetoCaja = ingresos - egresos;

        if (db.esMesInicio(mesStr, String.valueOf(anioInt))) {
            flujoNetoCaja += db.darValorInicialCajaFuerte();
        } else if (db.esMayorFechaInicio(mesStr, String.valueOf(anioInt))){
            flujoNetoCaja += db.darValorCajaFuerte();
        }

        String flujoNetoCajaStr = nf.format(flujoNetoCaja);

        if (flujoNetoCaja > 0 ) {
            txtFlujoNC.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            txtFlujoNC.setTextColor(getResources().getColor(R.color.colorFia));
        }

        txtIngresos.setText(ingresoStr);
        txtEgresos.setText(egresoStr);
        txtFlujoNC.setText(flujoNetoCajaStr);
        txtMes.setText(mesStr(mesInt)+ ": " + anioInt);

        ArrayList<DataPoint> ingresosDP = new ArrayList<>();
        ingresosDP.add(new DataPoint(1, ingresos));
        ArrayList<DataPoint> egresosDP = new ArrayList<>();
        egresosDP.add(new DataPoint(2, egresos));
        configurarGrafico(ingresosDP, egresosDP);
    }

    private void actualizarDatosADia(int diaInt, int mesInt, int anioInt) {

        String mesStr= convertirMes(mesInt);
        String diaStr = diaStr(diaInt);
        int ingresos = db.calcularIngresosDia(diaStr, mesStr, String.valueOf(anioInt));
        int egresos = db.calcularEgresosDia(diaStr, mesStr, String.valueOf(anioInt));
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String ingresoStr = nf.format(ingresos);
        String egresoStr = nf.format(egresos);
        int flujoNetoCaja = ingresos - egresos;
        String flujoNetoCajaStr = nf.format(flujoNetoCaja);

        if (flujoNetoCaja > 0 ) {
            txtFlujoNC.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            txtFlujoNC.setTextColor(getResources().getColor(R.color.colorFia));
        }

        txtIngresos.setText(ingresoStr);
        txtEgresos.setText(egresoStr);
        txtFlujoNC.setText(flujoNetoCajaStr);
        txtMes.setText( mesStr(mesInt) + ": " + diaInt);

        ArrayList<DataPoint> ingresosDP = new ArrayList<>();
        ingresosDP.add(new DataPoint(1, ingresos));
        ArrayList<DataPoint> egresosDP = new ArrayList<>();
        egresosDP.add(new DataPoint(2, egresos));
        configurarGrafico(ingresosDP, egresosDP);
    }

    private String convertirMes(int mes) {

        String respuesta = "";

        if (mes < 10) {
            respuesta = "0" + mes;
        } else {
            respuesta = String.valueOf(mes);
        }

        return respuesta;
    }

    public void atras(View v) {

        if (banderaDia == false) {
            mes -= 1;
            if (mes < 1) {
                mes = 12;
                anio -= 1;
            }
            actualizarDatos(mes, anio);
        } else {
            dia -= 1;
            if (dia < 1) {
                dia = 31;
                mes -= 1;
                if (mes < 1) {
                    mes = 12;
                    anio -= 1;
                }
            }
            actualizarDatosADia(dia, mes, anio);
        }

    }

    public void adelante(View v) {

        if (banderaDia == false) {
            mes += 1;
            if (mes > 12) {
                mes = 1;
                anio += 1;
            }
            actualizarDatos(mes, anio);
        } else {
            dia += 1;
            if (dia > 31) {
                dia = 1;
                mes += 1;
                if (mes > 12) {
                    mes = 1;
                    anio += 1;
                }
            }
            actualizarDatosADia(dia, mes, anio);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fnc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_fnc_inicio:
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                break;
            case R.id.menu_fnc_historial:
                Intent i2 = new Intent(this, MovimientosActivity.class);
                i2.putExtra("dia", dia);
                i2.putExtra("mes", mes);
                i2.putExtra("anio", anio);
                i2.putExtra("diario", banderaDia);
                startActivity(i2);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
