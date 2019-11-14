package com.desarrollandoapps.senamarket.cartera;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;
import com.desarrollandoapps.senamarket.adaptadores.AdaptadorMovimientosCliente;
import com.desarrollandoapps.senamarket.caja.MovimientoCaja;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ClienteActivity extends AppCompatActivity {

    public static final int RESULT_ABONO = 1;
    public static final int RESULT_DEUDA = 2;
    public static final int REQUEST_CODE_ACTUALIZAR_CLIENTE = 3;
    private boolean abonar;
    private boolean fiar;
    private int monto;
    private String cedula;
    private String nombre;
    private String direccion;
    private String telefono;

    TextView lblNombre;
    TextView lblDeuda;
    ListView lvMovimientosCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lblNombre = (TextView) findViewById(R.id.lblNombreCliente);
        lblDeuda = (TextView) findViewById(R.id.lblDeudaCliente);
        lvMovimientosCliente = (ListView) findViewById(R.id.lvMovimientosCliente);

        ArrayList<String> fechas = new ArrayList<>();
        ArrayList<String> montos = new ArrayList<>();


        Intent i = getIntent();
        if (i.hasExtra("cedula")) {
            cedula = i.getExtras().getString("cedula");
            int cedulaInt = Integer.parseInt(cedula);
            DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat("#,###,###", simbolo);
            String cedulaFormateada = df.format(cedulaInt);
        }
        if (i.hasExtra("nombre")) {
            nombre = i.getExtras().getString("nombre");
            lblNombre.setText(nombre);
        }
        if (i.hasExtra("direccion")) {
            direccion = i.getExtras().getString("direccion");
        }
        if (i.hasExtra("telefono")) {
            telefono = i.getExtras().getString("telefono");
        }
        if (i.hasExtra("deuda")) {
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            String deuda = format.format(i.getExtras().getInt("deuda"));
            lblDeuda.setText(deuda);
        }

        BaseDeDatos dbTienda = new BaseDeDatos(this, "Tienda", null, 1);
        ArrayList<MovimientoCaja> movimientos = dbTienda.darMovimientosCarteraMes(cedula);
        AdaptadorMovimientosCliente adaptador = new AdaptadorMovimientosCliente(this, movimientos);
        lvMovimientosCliente.setAdapter(adaptador);

    }

    public void btnAbonar(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Abono");

        // Set up the input
        final EditText txtMonto = new EditText(this);
        txtMonto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtMonto.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        txtMonto.setTextSize(20);

        txtMonto.addTextChangedListener(new TextWatcher() {
            boolean isEditing;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
                    String nf = df.format(s1);
                    s.replace(0, s.length(), nf);


                } catch (Exception e) {}

                isEditing = false;
            }
        });

        builder.setView(txtMonto);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!txtMonto.getText().toString().isEmpty()) {
                    monto = Integer.parseInt(txtMonto.getText().toString().replace(".", ""));
                    abonar = true;
                    finish();
                } else {
                    Toast.makeText(ClienteActivity.this, "Debe ingresar un monto válido", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    public void btnFiar(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Fiar");

        // Set up the input
        final EditText txtMonto = new EditText(this);
        txtMonto.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        txtMonto.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        txtMonto.setTextSize(20);
        txtMonto.addTextChangedListener(new TextWatcher() {
            boolean isEditing;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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


                } catch (Exception e) {}

                isEditing = false;
            }
        });
        builder.setView(txtMonto);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!txtMonto.getText().toString().isEmpty()) {
                    monto = Integer.parseInt(txtMonto.getText().toString().replace(".", ""));
                    fiar = true;
                    finish();
                } else {
                    Toast.makeText(ClienteActivity.this, "Debe ingresar un monto válido", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    @Override
    public void finish() {

        if (abonar || fiar) {
            Intent i = getIntent();
            i.putExtra("monto", monto);
            i.putExtra("idCliente", cedula);

            if (abonar) {
                setResult(RESULT_ABONO, i);
            } else if (fiar) {
                setResult(RESULT_DEUDA, i);
            }
        }
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_inicio:
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_verDatos:
                lanzarVerDatos(null);
                break;
            case R.id.menu_actualizar_datos:
                lanzarActualizarDatos(null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void lanzarVerDatos(View v) {
        Intent intent = new Intent(this, DatosContactoClienteActivity.class);
        intent.putExtra("cedula", cedula);
        intent.putExtra("nombre", nombre);
        intent.putExtra("direccion", direccion);
        intent.putExtra("telefono", telefono);
        startActivity(intent);
    }

    public void lanzarActualizarDatos(View v) {
        Intent intent = new Intent(this, ActualizarDatosClienteActivity.class);
        intent.putExtra("cedula", cedula);
        intent.putExtra("nombre", nombre);
        intent.putExtra("direccion", direccion);
        intent.putExtra("telefono", telefono);
        startActivityForResult(intent, REQUEST_CODE_ACTUALIZAR_CLIENTE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_ACTUALIZAR_CLIENTE && resultCode == RESULT_OK) {

            boolean modificado = false;

            if (data.hasExtra("nombre")) {
                if (!nombre.equals(data.getExtras().getString("nombre"))) {
                    nombre = data.getExtras().getString("nombre");
                    modificado = true;
                }
            }
            if (data.hasExtra("direccion")) {
                if (!direccion.equals(data.getExtras().getString("direccion"))) {
                    direccion = data.getExtras().getString("direccion");
                    modificado = true;
                }
            }
            if (data.hasExtra("telefono")) {
                if (!telefono.equals(data.getExtras().getString("telefono"))) {
                    telefono = data.getExtras().getString("telefono");
                    modificado = true;
                }
            }

            BaseDeDatos Tienda = new BaseDeDatos(this, "Tienda", null, 1);
            SQLiteDatabase bd = Tienda.getWritableDatabase();

            if (modificado) {
                try {
                    bd.execSQL("UPDATE Cliente SET " +
                            "nombre = '" + nombre + "', " +
                            "direccion = '" + direccion + "'," +
                            "telefono = '" + telefono + "' " +
                            "WHERE idCliente = '" + cedula + "'");
                    Toast.makeText(this, "Modificado satisfactoriamente", Toast.LENGTH_SHORT).show();
                    lblNombre.setText(nombre);
                } catch (SQLException e) {
                    Toast.makeText(this, "Error al ingresar los datos", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

}
