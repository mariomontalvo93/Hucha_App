package com.example.hucha.BBDD;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.hucha.BBDD.DAO.UsuarioDao;
import com.example.hucha.BBDD.Modelo.Usuario;

@Database(entities = {Usuario.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
}
