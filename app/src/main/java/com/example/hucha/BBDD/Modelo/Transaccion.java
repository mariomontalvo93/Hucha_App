package com.example.hucha.BBDD.Modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Entity
public class Transaccion {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "metaId")
    @NotNull
    public long metaId;

    @ColumnInfo(name = "tipoTransaccion")
    @NotNull
    public int tipoTransaccion;

    @ColumnInfo(name = "cantidad")
    @NotNull
    public float cantidad;

    @ColumnInfo(name = "concepto")
    public String concepto;

    @ColumnInfo(name = "fecha")
    @NotNull
    public Long fecha;

    public Transaccion(long metaId, int tipoTransaccion, float cantidad, Long fecha, String concepto) {
        this.metaId = metaId;
        this.tipoTransaccion = tipoTransaccion;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.concepto = concepto;
    }
}
