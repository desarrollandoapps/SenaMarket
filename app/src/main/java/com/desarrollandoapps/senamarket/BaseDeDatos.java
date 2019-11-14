package com.desarrollandoapps.senamarket;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.desarrollandoapps.senamarket.caja.MovimientoCaja;
import com.desarrollandoapps.senamarket.cartera.Cliente;
import com.desarrollandoapps.senamarket.calendario.EventoCalendario;
import com.desarrollandoapps.senamarket.presupuesto.Presupuesto;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by JoseO on 23/08/16.
 */
public class BaseDeDatos extends SQLiteOpenHelper {

    //-------------------
    // Atributos
    //-------------------

    private String sqlCrearBase = "CREATE TABLE ValoresFijos(" +
            "idValoresFijos INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "inicialCajaFuerte INTEGER, " +
            "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "base INTEGER, " +
            "cajaFuerte INTEGER);";
    private String sqlCrearMovimiento = "CREATE TABLE Movimiento ( " +
            "idMovimiento INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "tipoMovimiento VARCHAR(7) NOT NULL, " +
            "claseMovimiento VARCHAR(20) NOT NULL, " +
            "monto INTEGER NOT NULL);";
    private String sqlCrearMovCajaFuerte = "CREATE TABLE MovCajaFuerte ( " +
            "idMovimientoCF INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "tipoMovimiento VARCHAR(7) NOT NULL, " +
            "monto INTEGER NOT NULL);";
    private String sqlCrearCliente = "CREATE TABLE Cliente ( "+
            "idCliente VARCHAR(10) NOT NULL, "+
            "nombre VARCHAR(30) NULL, "+
            "telefono VARCHAR(10) NULL, "+
            "direccion VARCHAR(30) NULL, "+
            "deuda INTEGER NOT NULL, " +
            "PRIMARY KEY (idCliente));";
    private String sqlCrearPresupuesto = "CREATE TABLE Presupuesto ( " +
            "idPresupuesto INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            "categoria VARCHAR(20) NOT NULL, " +
            "monto INTEGER NOT NULL DEFAULT 0," +
            "ejecutado INTEGER NOT NULL DEFAULT 0);";
    private String sqlCrearMoviemientoCartera = "CREATE TABLE MovimientoCartera ( "+
            "idMovimientoCartera INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "tipoMovimiento VARCHAR(45) NOT NULL, " +
            "fecha INTEGER NOT NULL, " +
            "monto INTEGER NOT NULL, " +
            "Cliente_idCliente VARCHAR(10) NOT NULL, " +
            "FOREIGN KEY (Cliente_idCliente) " +
            "REFERENCES Cliente (idCliente))";
    private String sqlCrearCalendario = "CREATE TABLE Calendario ( " +
            "idCalendario INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "fecha TIMESTAMP NOT NULL, " +
            "acreedor VARCHAR(20) NOT NULL, " +
            "monto INTEGER NOT NULL," +
            "pagado INTEGER NOT NULL DEFAULT 0);";

    private String sqlPoblarPresupuestos = "INSERT INTO Presupuesto(categoria) " +
            "VALUES('Proveedores'), ('Bancos'), ('Arriendo'), ('Servicios públicos')";

    //-------------------
    // Constructor
    //-------------------

    public BaseDeDatos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //-------------------
    // Métodos
    //-------------------

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(sqlCrearBase);
        db.execSQL(sqlCrearMovimiento);
        db.execSQL(sqlCrearMovCajaFuerte);
        db.execSQL(sqlCrearCliente);
        db.execSQL(sqlCrearPresupuesto);
        db.execSQL(sqlCrearMoviemientoCartera);
        db.execSQL(sqlPoblarPresupuestos);
        db.execSQL(sqlCrearCalendario);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Escribir sentencias cuando se necesite actualizar la base de datos
    }

    public int darValorBase() {
        int respuesta = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        if (hayBase()) {
            Cursor registro = db.rawQuery("SELECT base FROM ValoresFijos", null);
            if (registro.moveToFirst()) {
                respuesta = registro.getInt(0);
            }
        }

        return respuesta;
    }

    public boolean modificarBase(int monto) {
        boolean respuesta = false;
        SQLiteDatabase db = this.getWritableDatabase();

        if (hayBase()) {
            db.execSQL("UPDATE ValoresFijos SET base = " + monto);
        } else {
            crearBase(monto);
        }

        return respuesta;
    }

    public boolean crearBase(int monto) {
        boolean respuesta = false;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("base", monto);

        try {
            db.update("ValoresFijos", valores, "idValoresFijos = ?", new String[] {"1"});
            respuesta = true;
        } catch (SQLException e) {
            respuesta = false;
        }

        return respuesta;
    }

    public boolean crearCajaFuerte(int monto) {
        boolean respuesta = false;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("inicialCajaFuerte", monto);

        try {
            db.insertOrThrow("ValoresFijos", null, valores);
            respuesta = true;
        } catch (SQLException e) {
            respuesta = false;
        }

        return respuesta;
    }

    public boolean hayBase() {
        boolean respuesta = false;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor registro = db.rawQuery("SELECT base FROM ValoresFijos", null);

        if (registro.moveToFirst()) {
            respuesta = true;
        }

        return respuesta;
    }

    public int darValorInicialCajaFuerte() {
        int respuesta = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        if (hayBase()) {
            Cursor registro = db.rawQuery("SELECT inicialCajaFuerte FROM ValoresFijos", null);
            if (registro.moveToFirst()) {
                respuesta = registro.getInt(0);
            }
        }

        return respuesta;
    }

    public boolean esMesInicio(String mes, String anio) {
        boolean respuesta = false;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor registro = db.rawQuery("SELECT * FROM ValoresFijos WHERE strftime('%m', fecha) = '" + mes + "' " +
                "AND strftime('%Y', fecha) = '" + anio + "'", null);

        if (registro.moveToFirst()) {
            respuesta = true;
        }

        return respuesta;
    }

    public String mesInicio() {
        String respuesta = "";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor registro = db.rawQuery("SELECT strftime('%m', fecha) FROM ValoresFijos ORDER BY fecha ASC", null);

        if (registro.moveToFirst()) {
            respuesta = registro.getString(0);
        }

        return respuesta;
    }

    public String anioInicio() {
        String respuesta = "";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor registro = db.rawQuery("SELECT strftime('%Y', fecha) FROM ValoresFijos ORDER BY fecha ASC", null);

        if (registro.moveToFirst()) {
            respuesta = String.valueOf(registro.getInt(0));
        }

        return respuesta;
    }

    public boolean esMayorFechaInicio(String mes, String anio) {
        boolean respuesta = false;

        int mesInt = Integer.parseInt(mes);
        int anioInt = Integer.parseInt(anio);


        if (anioInt > Integer.parseInt(anioInicio())) {
            respuesta = true;
        } else if (anioInt == Integer.parseInt(anioInicio()) && mesInt > Integer.parseInt(mesInicio())) {
            respuesta = true;
        }

        return respuesta;
    }

    public int darValorCajaFuerte() {
        int respuesta = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        if (hayBase()) {
            Cursor registro = db.rawQuery("SELECT cajaFuerte FROM ValoresFijos", null);
            if (registro.moveToFirst()) {
                respuesta = registro.getInt(0);
            }
        }

        return respuesta;
    }

    public int darOtrosIngresos() {
        int respuesta = -1;

        SQLiteDatabase db = this.getWritableDatabase();

        if (hayBase()) {
            Cursor registro = db.rawQuery("SELECT SUM(monto) FROM Movimiento WHERE tipoMovimiento = 'Entrada' AND " +
                    "claseMovimiento != 'Cierre de Caja' " +
                    "AND strftime('%d', fecha) = strftime('%d', date('now')) " +
                    "AND strftime('%m', fecha) = strftime('%m', date('now')) " +
                    "AND strftime('%Y', fecha) = strftime('%Y', date('now')) ", null);
            if (registro.moveToFirst()) {
                respuesta = registro.getInt(0);
            }
        }

        return respuesta;
    }

    public boolean cierreDeCaja(String monto) {
        boolean respuesta = false;

        SQLiteDatabase db = this.getWritableDatabase();

        int montoInt = Integer.parseInt(monto) - darValorBase();
        int montoCajaFuerte = darValorCajaFuerte();

        ContentValues valores = new ContentValues();
        valores.put("tipoMovimiento", "Ingreso");
        valores.put("monto", montoInt);

        ContentValues valorCajaFuerte = new ContentValues();
        valorCajaFuerte.put("cajaFuerte", montoCajaFuerte + montoInt);

        try {
            db.insertOrThrow("MovCajaFuerte", null, valores);
            db.update("ValoresFijos", valorCajaFuerte, "idValoresFijos = ?", new String[] {"1"});
            respuesta = true;
        } catch (SQLException e) {
            respuesta = false;
        }

        return respuesta;
    }

    public ArrayList<String> darNombresClientes() {
        ArrayList<String> respuesta = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String consulta = "SELECT nombre FROM Cliente ORDER BY nombre ASC";

        Cursor registros = db.rawQuery(consulta, null);

        if (registros.moveToFirst()) {
            do {
                respuesta.add(registros.getString(0));
            } while (registros.moveToNext());
        }

        return respuesta;
    }

    public ArrayList<Cliente> darClientes() {
        ArrayList<Cliente> clientes = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String consulta = "SELECT * FROM Cliente ORDER BY nombre ASC";

        Cursor registros = db.rawQuery(consulta, null);

        if (registros.moveToFirst()) {
            do {
                Cliente cliente = new Cliente(registros.getString(0), registros.getString(1), registros.getString(2),
                        registros.getString(3), registros.getInt(4));
                clientes.add(cliente);
            } while (registros.moveToNext());
        }

        return clientes;
    }

    public int darDeudaCliente(String idCliente) {
        int respuesta = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        String consulta = "SELECT deuda FROM Cliente WHERE idCliente = '" + idCliente + "'";

        Cursor registros = db.rawQuery(consulta, null);

        if (registros.moveToFirst()) {
            respuesta = registros.getInt(0);
        }

        return respuesta;
    }

    public boolean actualizarDeuda(String idCliente, int monto) {
        boolean respuesta = true;

        SQLiteDatabase db = this.getWritableDatabase();

        String sqlActualizar ="UPDATE Cliente SET deuda = " + monto + " WHERE idCliente = '" + idCliente + "'";

        db.execSQL(sqlActualizar);

        return respuesta;
    }

    public boolean agregarMovimientoCartera(String idCliente, String tipoMoviemiento, int monto) {
        boolean respuesta = true;

        SQLiteDatabase db = this.getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        ContentValues valores = new ContentValues();

        valores.put("fecha", calendar.getTime().getTime());
        valores.put("tipoMovimiento", tipoMoviemiento);
        valores.put("monto", monto);
        valores.put("Cliente_idCliente", idCliente);

        try {
            db.insertOrThrow("MovimientoCartera", null, valores);
        } catch (SQLException e) {
            respuesta = false;
        }

        int nuevoMonto;

        if (tipoMoviemiento == "Abono") {
            nuevoMonto = darDeudaCliente(idCliente) - monto;
            respuesta = actualizarDeuda(idCliente, nuevoMonto);
        } else if (tipoMoviemiento == "Fia"){
            nuevoMonto = darDeudaCliente(idCliente) + monto;
            respuesta = actualizarDeuda(idCliente, nuevoMonto);
        }

        return respuesta;
    }

    public ArrayList<MovimientoCaja> darMovimientosCarteraMes(String idCliente) {
        ArrayList<MovimientoCaja> movimientos = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String consulta = "SELECT * FROM MovimientoCartera WHERE Cliente_idCliente = '" + idCliente +"' ORDER BY fecha DESC";

        //Cursor registros = db.query("MovimientoCartera", new String[] {"fecha"}, "Cliente_idCliente", new String[] {idCliente},null, null, null);

        Cursor registros = db.rawQuery(consulta, null);

        MovimientoCaja movimientoCaja;

        if (registros.moveToFirst()) {
            do {

                String tipo = registros.getString(1);
                Date fechaDate = new java.util.Date(registros.getLong(2));
                String fechaStr = new java.text.SimpleDateFormat("dd/MM/yy HH:mm").format(fechaDate);
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
                String monto = format.format(registros.getInt(3));

                movimientoCaja = new MovimientoCaja(tipo, fechaStr, monto, idCliente);
                movimientos.add(movimientoCaja);
            } while (registros.moveToNext());
        }

        return movimientos;
    }

    public int calcularIngresosMes(String mes, String anio) {

        int respuesta = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlIngresosMes = "SELECT SUM(monto) FROM movimiento " +
                "WHERE tipoMovimiento = 'Entrada' AND strftime('%m', fecha) = '" + mes +
                "' AND strftime('%Y', fecha) = '" + anio + "' ";

        Cursor datos = db.rawQuery(sqlIngresosMes, null);

        if (datos.moveToFirst()) {
            respuesta = datos.getInt(0);
        }

        return respuesta;

    }

    public int calcularIngresosCFMes(String mes, String anio) {

        int respuesta = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlIngresosMes = "SELECT SUM(monto) FROM MovCajaFuerte " +
                "WHERE tipoMovimiento = 'Ingreso' AND strftime('%m', fecha) = '" + mes +
                "' AND strftime('%Y', fecha) = '" + anio + "' ";

        Cursor datos = db.rawQuery(sqlIngresosMes, null);

        if (datos.moveToFirst()) {
            respuesta = datos.getInt(0);
        }

        return respuesta;

    }

    public int calcularEgresosMes(String mes, String anio) {

        int respuesta = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlIngresosMes = "SELECT SUM(monto) FROM movimiento " +
                "WHERE tipoMovimiento = 'Salida' AND strftime('%m', fecha) = '" + mes +
                "' AND strftime('%Y', fecha) = '" + anio + "' ";

        Cursor datos = db.rawQuery(sqlIngresosMes, null);

        if (datos.moveToFirst()) {
            respuesta = datos.getInt(0);
        }

        return respuesta;

    }

    public int calcularIngresosDia(String dia, String mes, String anio) {

        int respuesta = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlIngresosMes = "SELECT SUM(monto) FROM movimiento " +
                "WHERE tipoMovimiento = 'Entrada' AND strftime('%d', fecha) = '" + dia +
        "' AND strftime('%m', fecha) = '" + mes + "' AND strftime('%Y', fecha) = '" + anio + "' ";

        Cursor datos = db.rawQuery(sqlIngresosMes, null);

        if (datos.moveToFirst()) {
            respuesta = datos.getInt(0);
        }

        return respuesta;

    }

    public int calcularEgresosDia(String dia, String mes, String anio) {

        int respuesta = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlIngresosMes = "SELECT SUM(monto) FROM movimiento " +
                "WHERE tipoMovimiento = 'Salida' AND strftime('%d', fecha) = '" + dia +
                "' AND strftime('%m', fecha) = '" + mes + "' AND strftime('%Y', fecha) = '" + anio + "' ";

        Cursor datos = db.rawQuery(sqlIngresosMes, null);

        if (datos.moveToFirst()) {
            respuesta = datos.getInt(0);
        }

        return respuesta;

    }

    public ArrayList<Cliente> darClientesConDeuda() {

        ArrayList<Cliente> respuesta = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String consulta = "SELECT * FROM Cliente WHERE deuda > 0";

        Cursor registros = db.rawQuery(consulta, null);

        if (registros.moveToFirst()) {
            do {
                respuesta.add(new Cliente(registros.getString(0), registros.getString(1), registros.getString(2),
                        registros.getString(3), registros.getInt(4)));
            } while (registros.moveToNext());
        }

        return respuesta;

    }

    public boolean agregarPresupuesto(Presupuesto presupuesto) {
        boolean respuesta = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("categoria", presupuesto.darCategoria());
        valores.put("monto", presupuesto.darMonto());

        if (!existePresupuestoMes(presupuesto.darCategoria())) {

            try {
                db.insertOrThrow("Presupuesto", null, valores);
                respuesta = true;
            } catch (SQLException e) {
                respuesta = false;
            }
        }

        return respuesta;
    }

    public void modificarPresupuesto(int id, String monto) {
        monto = monto.replace(".", "");
        int montoInt = Integer.parseInt(monto);
        SQLiteDatabase db = this.getWritableDatabase();

        String consulta = "UPDATE Presupuesto SET monto = " + montoInt +
                " WHERE idPresupuesto = " + id;

        db.execSQL(consulta);

    }

    public ArrayList<Presupuesto> darPresupuestos() {
        ArrayList<Presupuesto> respuesta = new ArrayList<>();
        Presupuesto presupuesto;
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlConsulta = "SELECT * FROM Presupuesto";
        Cursor registros = db.rawQuery(sqlConsulta, null);

        if (registros.moveToFirst()) {
            do {
                Date fechaDate = new java.util.Date(registros.getLong(1));
                presupuesto = new Presupuesto(registros.getInt(0),fechaDate, registros.getString(2), registros.getInt(3), registros.getInt(4));
                respuesta.add(presupuesto);
            } while (registros.moveToNext());
        }

        return respuesta;
    }

    public ArrayList<Presupuesto> darPresupuestosMes() {
        ArrayList<Presupuesto> respuesta = new ArrayList<>();
        Presupuesto presupuesto;
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlConsulta = "SELECT * FROM Presupuesto WHERE strftime('%m', fecha) = strftime('%m', date('now')) AND strftime('%Y', fecha) = strftime('%Y', date('now'))";
        Cursor registros = db.rawQuery(sqlConsulta, null);

        if (registros.moveToFirst()) {
            do {
                Date fechaDate = new java.util.Date(registros.getLong(1));
                presupuesto = new Presupuesto(registros.getInt(0),fechaDate, registros.getString(2), registros.getInt(3), registros.getInt(4));
                respuesta.add(presupuesto);
            } while (registros.moveToNext());
        }

        return respuesta;
    }

    public Presupuesto darPresupuesto(String categoria) {
        Presupuesto presupuesto = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String sqlConsulta = "SELECT * FROM Presupuesto WHERE categoria = '" + categoria + "' ORDER BY fecha DESC";

        Cursor registros = db.rawQuery(sqlConsulta, null);



        if (registros.moveToFirst()) {
            Date fechaDate = new java.util.Date(registros.getLong(1));
            presupuesto = new Presupuesto(registros.getInt(0), fechaDate, registros.getString(2), registros.getInt(3), registros.getInt(4));
        }

        return presupuesto;
    }

    public boolean existePresupuestoMes(String categoria) {
        boolean respuesta = false;

        SQLiteDatabase db = this.getReadableDatabase();

        String sqlConsulta = "SELECT * FROM Presupuesto WHERE categoria = '" + categoria + "' AND strftime('%m', fecha) = strftime('%m', date('now'))"
                + " AND strftime('%Y', fecha) = strftime('%Y', date('now'))";

        Cursor registros = db.rawQuery(sqlConsulta, null);

        if (registros.moveToFirst()) {
            respuesta = true;
        }

        return respuesta;
    }

    public boolean existenPresupuestosFecha(String mes) {
        boolean respuesta = false;

        SQLiteDatabase db = this.getReadableDatabase();
        String anio = String.valueOf(Calendar.YEAR);
        String sqlConsulta = "SELECT * FROM Presupuesto WHERE strftime('%m-%Y', fecha) = '" + mes + "-" + anio + "'";

        Cursor registros = db.rawQuery(sqlConsulta, null);



        if (registros.moveToFirst()) {
            respuesta = true;
        }

        return respuesta;
    }

    public boolean agregarEventoCalendario(EventoCalendario ec) {
        boolean respuesta = false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("fecha", ec.darFecha().getTime());
        valores.put("acreedor", ec.darAcreedor());
        valores.put("monto", ec.darMonto());

        try {
            db.insertOrThrow("Calendario", null, valores);
            respuesta = true;
        } catch (SQLException e) {
            respuesta = false;
        }

        return respuesta;
    }

    public ArrayList<EventoCalendario> darEventosCalendario() {
        ArrayList<EventoCalendario> respuesta = new ArrayList<>();
        EventoCalendario eventoCalendario;
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlConsulta = "SELECT * FROM Calendario WHERE pagado = 0 ORDER BY fecha ASC";
        Cursor registros = db.rawQuery(sqlConsulta, null);

        if (registros.moveToFirst()) {
            do {
                Date fechaDate = new java.util.Date(registros.getLong(1));
                eventoCalendario = new EventoCalendario(registros.getInt(0),fechaDate, registros.getString(2), registros.getInt(3), Boolean.getBoolean(String.valueOf(registros.getInt(4))));
                respuesta.add(eventoCalendario);
            } while (registros.moveToNext());
        }

        return respuesta;
    }

    public boolean eliminarEvento(String id) {
        boolean respuesta = false;
        SQLiteDatabase db = this.getWritableDatabase();

        if (db.delete("Calendario", "idCalendario = ?", new String[] {id}) > 0) {
            respuesta = true;
        }
        return respuesta;
    }

    public boolean pagarEvento(EventoCalendario evento) {
        boolean respuesta = false;
        String id = String.valueOf(evento.darId());

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("pagado", 1);

        if (db.update("Calendario", valores, "idCalendario = ?", new String[]{id}) > 0) {
            ContentValues valores2 = new ContentValues();
            valores2.put("tipoMovimiento", "Salida");
            valores2.put("claseMovimiento", "Pago a " + evento.darAcreedor());
            valores2.put("monto", evento.darMonto());

            db.insert("Movimiento", null, valores2);
            respuesta = true;
        }

        return respuesta;
    }

    public int darCuentasPorPagar() {
        int respuesta = 0;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor registro = db.rawQuery("SELECT sum(monto) FROM Calendario WHERE pagado = 0", null);

        if (registro.moveToFirst()) {
            respuesta = registro.getInt(0);
        }

        return respuesta;
    }

    public ArrayList<MovimientoCaja> darMovimientosCajaMes(String dia, String mes, String anio) {
        ArrayList<MovimientoCaja> respuesta = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor registros = db.rawQuery("SELECT tipoMovimiento, fecha, monto, claseMovimiento FROM Movimiento " +
                "WHERE strftime('%m', fecha) = '" + mes + "' " +
                "AND strftime('%Y', fecha) = '" + anio + "' ORDER BY fecha DESC", null);

        if (registros.moveToFirst()) {
            do {
                String tipoMovimiento = registros.getString(0);
                //String fechaStr = dia + "/" + mes + "/" + anio;
                String fechaStr = registros.getString(1);
                fechaStr = fechaStr.substring(8, 10) + "/" + fechaStr.substring(5, 7) + "/" + fechaStr.substring(0, 4);
                String monto = registros.getString(2);
                String clase = registros.getString(3);
                respuesta.add(new MovimientoCaja(tipoMovimiento, fechaStr, monto, clase));
            } while (registros.moveToNext());
        }

        return  respuesta;
    }
}
