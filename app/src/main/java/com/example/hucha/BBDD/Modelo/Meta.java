package com.example.hucha.BBDD.Modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.sql.Blob;

@Entity
public class Meta implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "nombre")
    @NotNull
    public String nombre;

    @ColumnInfo(name = "dineroObjetivo")
    @NotNull
    public float dineroObjetivo;

    @ColumnInfo(name = "dineroActual")
    @NotNull
    public float dineroActual;

    @ColumnInfo(name = "color")
    @NotNull
    public String color;

    @ColumnInfo(name = "logrado")
    @NotNull
    public boolean logrado;

    @ColumnInfo(name = "icono")
    public byte[] icono;

    @ColumnInfo(name = "iconoGenerico")
    public int iconoGenerico;

    @ColumnInfo(name = "online")
    @NotNull
    public boolean online;

    @ColumnInfo(name = "idUsuario")
    @NotNull
    public String idUsuario;

    public Meta(String nombre, float dineroObjetivo, float dineroActual, String color, boolean logrado, byte[] icono, int iconoGenerico, boolean online, String idUsuario) {
        this.nombre = nombre;
        this.dineroObjetivo = dineroObjetivo;
        this.dineroActual = dineroActual;
        this.color = color;
        this.logrado = logrado;
        this.icono = icono;
        this.iconoGenerico = iconoGenerico;
        this.online = online;
        this.idUsuario = idUsuario;
    }
}
