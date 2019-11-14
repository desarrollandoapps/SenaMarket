package com.desarrollandoapps.senamarket.cartera;

/**
 * Created by JoseO on 5/10/16.
 */

public class Cliente {

    private String cedula;
    private String nombre;
    private String telefono;
    private String direccion;
    private int deuda;

    public Cliente(String cedula, String nombre, String telefono, String direccion, int deuda) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.deuda = deuda;
    }

    public Cliente(String cedula, String nombre, int deuda) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.deuda = deuda;
    }

    public String darCedula() {
        return cedula;
    }

    public String darNombre() {
        return nombre;
    }

    public int darDeuda() {
        return deuda;
    }

    public String darTelefono() {
        return telefono;
    }

    public String darDireccion() {
        return direccion;
    }
}
