package com.desarrollandoapps.senamarket.presupuesto;

import java.util.Date;

/**
 * Created by JoseO on 14/10/16.
 */

public class Presupuesto {

    //------------
    // Atributos
    //------------

    private int id;
    private Date fecha;
    private String categoria;
    private int monto;
    private int ejecutado;

    //------------
    // Constructor
    //------------

    public Presupuesto(int id, Date fecha, String categoria, int monto, int ejecutado) {
        this.id = id;
        this.categoria = categoria;
        this.monto = monto;
        this.ejecutado = ejecutado;
        this.fecha = fecha;
    }

    public Presupuesto(Date fecha, String categoria, int monto, int ejecutado) {
        this.fecha = fecha;
        this.categoria = categoria;
        this.monto = monto;
        this.ejecutado = ejecutado;
    }

    //------------
    // Métodos
    //------------


    public String darCategoria() {
        return categoria;
    }

    public int darMonto() {
        return monto;
    }

    public double darPorcentajeEjecutado() {
        if (monto == 0) {
            return 0;
        } else {
            return (double) ejecutado / monto;
        }
    }

    public int darEjecutado() {
        return ejecutado;
    }

    public int darSaldo() {
        return monto - ejecutado;
    }

    public Date darFecha() {
        return fecha;
    }

    public int darId() {
        return id;
    }

    public void actualizarPorcentajeEjecutado(int ejecutado) {
        this.ejecutado = ejecutado;
    }

    public void cambiarCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void cambiarMonto(int monto) {
        this.monto = monto;
    }
}
