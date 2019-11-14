package com.desarrollandoapps.senamarket.calendario;

import java.util.Date;

/**
 * Created by JoseO on 2/11/16.
 */

public class EventoCalendario {

    //Atributos

    private int id;
    private Date fecha;
    private String acreedor;
    private int monto;
    private boolean pagado;

    public EventoCalendario(int id, Date fecha, String acreedor, int monto, boolean pagado) {
        this.id = id;
        this.fecha = fecha;
        this.acreedor = acreedor;
        this.monto = monto;
        this.pagado = pagado;
    }

    public EventoCalendario(Date fecha, String acreedor, int monto) {
        this.id = 0;
        this.fecha = fecha;
        this.acreedor = acreedor;
        this.monto = monto;
        this.pagado = false;
    }

    public int darId() {
        return id;
    }

    public Date darFecha() {
        return fecha;
    }

    public String darAcreedor() {
        return acreedor;
    }

    public int darMonto() {
        return monto;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void pagar() {
        pagado = true;
    }
}
