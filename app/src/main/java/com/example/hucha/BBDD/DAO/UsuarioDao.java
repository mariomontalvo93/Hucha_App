package com.example.hucha.BBDD.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hucha.BBDD.Modelo.Usuario;

@Dao
public interface UsuarioDao {
    @Query("SELECT * FROM USUARIO WHERE email = :email")
    Usuario getUsuarioByEmail(String email);

    @Insert
    Long insertUsuario(Usuario usuario);

    @Query("DELETE FROM USUARIO WHERE id = :idUsuario")
    void eliminarUsuario(String idUsuario);
}
