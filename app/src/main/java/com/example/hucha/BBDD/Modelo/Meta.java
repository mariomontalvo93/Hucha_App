package com.example.hucha.BBDD.Modelo;

import androidx.room.Entity;

import java.sql.Blob;

@Entity
public class Meta {
    public int id;

    public String nombre;

    public float dineroObjetivo;

    public float dineroActual;

    public String color;

    public boolean logrado;

    public byte[] icono;

    public int iconoGenerico;

    public boolean online;

    public Meta(String nombre, float dineroObjetivo, float dineroActual, String color, boolean logrado, byte[] icono, int iconoGenerico, boolean online) {
        this.nombre = nombre;
        this.dineroObjetivo = dineroObjetivo;
        this.dineroActual = dineroActual;
        this.color = color;
        this.logrado = logrado;
        this.icono = icono;
        this.iconoGenerico = iconoGenerico;
        this.online = online;
    }
}
