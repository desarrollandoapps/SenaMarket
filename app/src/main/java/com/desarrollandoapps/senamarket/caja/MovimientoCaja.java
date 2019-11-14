package com.desarrollandoapps.senamarket.caja;

/**
 * Created by JoseO on 9/10/16.
 */

public class MovimientoCaja {

    private String tipoMovimiento;
    private String fecha;
    private String monto;
    private String claseMovimiento;

    public MovimientoCaja(String tipoMovimiento, String fecha, String monto, String claseMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
        this.fecha = fecha;
        this.monto = monto;
        this.claseMovimiento = claseMovimiento;
    }

    public String darTipoMovimiento() {
        return tipoMovimiento;
    }

    public String darFecha() {
        return fecha;
    }

    public String darMonto() {
        return monto;
    }

    public String darClaseMovimiento() {
        return claseMovimiento;
    }

}
