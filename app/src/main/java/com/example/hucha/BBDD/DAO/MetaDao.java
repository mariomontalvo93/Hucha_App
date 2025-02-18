package com.example.hucha.BBDD.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hucha.BBDD.Modelo.Meta;

import java.util.List;

@Dao
public interface MetaDao {
    @Query("SELECT * FROM META WHERE idUsuario = :idUsuario ORDER BY Logrado ASC")
    List<Meta> getMetasByUsuarioId(String idUsuario);

    @Query("SELECT COUNT(*) FROM META WHERE Logrado = 1 AND idUsuario = :idUsuario")
    int getMetasAlcanzadas(String idUsuario);

    @Query("SELECT COUNT(*) FROM META WHERE Logrado = 0 AND idUsuario = :idUsuario")
    int getMetasPendientes(String idUsuario);

    @Query("SELECT SUM(m.dineroActual) FROM META m WHERE m.Logrado = 0 AND m.idUsuario = :idUsuario")
    int getDineroAhorradoTotal(String idUsuario);

    @Query("SELECT SUM(m.dineroObjetivo) FROM META m WHERE m.Logrado = 0 AND m.idUsuario = :idUsuario")
    int getDineroMetasPendientesTotal(String idUsuario);

    @Insert
    Long inserMeta(Meta meta);

    @Update
    void updateMeta(Meta meta);

    @Delete
    void deleteMeta(Meta meta);

    @Query("DELETE FROM META WHERE idUsuario = :idUsuario")
    void eliminarMetasUsuario(String idUsuario);
}
