package com.example.hucha.BBDD;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.hucha.BBDD.DAO.MetaDao;
import com.example.hucha.BBDD.DAO.UsuarioDao;
import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.BBDD.Modelo.Transaccion;
import com.example.hucha.BBDD.Modelo.Usuario;

@Database(entities = {Usuario.class, Meta.class, Transaccion.class}, version = 2)
public abstract class AppDataBase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
    public abstract MetaDao metaDao();
}
