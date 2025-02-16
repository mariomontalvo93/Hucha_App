package com.example.hucha.BBDD.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.BBDD.Modelo.Transaccion;

import java.util.List;

@Dao
public interface TransaccionDao {
    @Query("SELECT * FROM TRANSACCION WHERE metaId = :metaId")
    List<Transaccion> getTransaccionMetaId(long metaId);

    @Query("SELECT * FROM TRANSACCION WHERE metaId IN (SELECT m.id FROM META m WHERE m.idUsuario = :idUsuario)")
    List<Transaccion> getTransaccionPorUsuario(String idUsuario);

    @Query("SELECT * FROM TRANSACCION t WHERE strftime('%Y-%m', datetime(fecha / 1000, 'unixepoch')) = strftime('%Y-%m', 'now') AND t.metaId IN (SELECT m.id FROM META m WHERE m.Logrado = 0 AND m.idUsuario = :idUsuario)")
    List<Transaccion> getTransaccionesEsteMes(String idUsuario);

    @Insert
    Long inserTransaccion(Transaccion transaccion);

    @Query("DELETE FROM TRANSACCION WHERE metaId IN (SELECT m.id FROM META m WHERE m.idUsuario = :idUsuario)")
    void eliminarTransaccionesUsuario(String idUsuario);
}