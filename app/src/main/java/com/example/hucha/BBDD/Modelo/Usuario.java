package com.example.hucha.BBDD.Modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "nombre")
    @NotNull
    public String nombre;

    @ColumnInfo(name = "apellidos")
    @NotNull
    public String apellidos;

    @ColumnInfo(name = "email")
    @NotNull
    public String email;

    @ColumnInfo(name = "password")
    @NotNull
    public String password;

    public Usuario(String nombre, String apellidos, String email, String password) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.password = password;
    }
}
