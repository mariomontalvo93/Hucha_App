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

    @Insert
    Long inserTransaccion(Transaccion transaccion);
}