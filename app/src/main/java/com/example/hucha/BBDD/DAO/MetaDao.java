package com.example.hucha.BBDD.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hucha.BBDD.Modelo.Meta;

import java.util.List;

@Dao
public interface MetaDao {
    @Query("SELECT * FROM META WHERE idUsuario = :idUsuario")
    List<Meta> getMetasByUsuarioId(String idUsuario);

    @Insert
    Long inserMeta(Meta meta);

    @Update
    void updateMeta(Meta meta);
}
