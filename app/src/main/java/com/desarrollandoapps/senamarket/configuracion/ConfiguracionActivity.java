package com.desarrollandoapps.senamarket.configuracion;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.desarrollandoapps.senamarket.BaseDeDatos;
import com.desarrollandoapps.senamarket.MainActivity;
import com.desarrollandoapps.senamarket.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConfiguracionActivity extends AppCompatActivity implements BaseFragment.NuevaBase {

    private int IMPORTAR = 1;
    private int EXPORTAR = 2;

    BaseDeDatos dbTienda;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbTienda = new BaseDeDatos(this, "Tienda", null, 1);

    }

    public void cambiarBase(View v) {
        BaseFragment dialogo = new BaseFragment();
        dialogo.show(getSupportFragmentManager(), "base");

        android.app.Fragment fragment = getFragmentManager().findFragmentByTag("base");
        if (fragment != null) {
            getFragmentManager().beginTransaction().remove(fragment).commit();
        }

    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static String DB_FILEPATH = "/data/"+ "com.desarrollandoapps.senamarket" + "/databases/" + "Tienda";

    public void exportarDBaSD(View v) {

        verifyStoragePermissions(this);
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = DB_FILEPATH;

        String timeStamp = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String backupDBPath = "Tienda_" + timeStamp; //"TiendaB";

        File currentDB = new File(data, currentDBPath);
        //File backupDB = new File("/data/data/com.desarrollandoapps.senamarket/databases/", backupDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(this, "Copia guardada en Tarjeta SD", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "No se guardó la copia", Toast.LENGTH_SHORT).show();
        }

    }

    public void importarDB(View v) {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Elegir Archivo"), IMPORTAR);

    }

    public void enviarMail(View v) {
        verifyStoragePermissions(this);
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = DB_FILEPATH;

        String timeStamp = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String backupDBPath = "Tienda_" + timeStamp; //"TiendaB";

        File currentDB = new File(data, currentDBPath);
        //File backupDB = new File("/data/data/com.desarrollandoapps.senamarket/databases/", backupDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        Intent intentMail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null));
        intentMail.putExtra(Intent.EXTRA_SUBJECT,
                "SENA Market - Copia de seguridad");
        intentMail.putExtra(Intent.EXTRA_TEXT,
                "Enviamos la copia de seguridad según su solicitud.");
        intentMail.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(backupDB));
        startActivity(Intent.createChooser(intentMail, "Enviar Mail"));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_CANCELED) {
            //Cancelado por el usuario
        }

        //Importar DB
        if ((resultCode == RESULT_OK) && (requestCode == IMPORTAR)) {
            Uri uri = intent.getData();
            String path = uri.getPath();
            List<String> paths = uri.getPathSegments();
            String archivo = paths.get(1);
            //
            verifyStoragePermissions(this);
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
            FileChannel source=null;
            FileChannel destination=null;
            String currentDBPath = DB_FILEPATH;
            String backupDBPath = archivo.substring(archivo.indexOf(":") + 1);
            File backupDB = new File(data, currentDBPath);
            File currentDB = new File(sd, backupDBPath);
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toast.makeText(this, "Restaurado", Toast.LENGTH_SHORT).show();
            } catch(IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "No se restauró", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ir_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_ir_inicio:
                Intent i = new Intent(this, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finalizarDialogoBase(String monto) {
        monto = monto.replace(".", "");
        int montoInt = Integer.parseInt(monto);

        dbTienda.modificarBase(montoInt);
    }
}
