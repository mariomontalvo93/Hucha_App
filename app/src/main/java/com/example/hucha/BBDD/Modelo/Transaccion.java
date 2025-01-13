package com.example.hucha.BBDD.Modelo;

import androidx.room.Entity;

import java.util.Date;

@Entity
public class Transaccion {

    public int id;

    public int metaId;

    public int tipoTransaccion;

    public float cantidad;

    public Date fecha;

    public Transaccion(int metaId, int tipoTransaccion, float cantidad, Date fecha) {
        this.metaId = metaId;
        this.tipoTransaccion = tipoTransaccion;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }
}
