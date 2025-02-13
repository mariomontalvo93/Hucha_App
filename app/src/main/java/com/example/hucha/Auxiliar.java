package com.example.hucha;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.hucha.BBDD.AppDataBase;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Auxiliar {

    private static AppDataBase appDataBase = null;

    public static AppDataBase getAppDataBaseInstance(Context context)
    {
        if(appDataBase == null)
        {
            appDataBase = Room.databaseBuilder(context, AppDataBase.class, "HuchaApp").fallbackToDestructiveMigration().build();
        }

        return appDataBase;
    }

    public static void eliminarDatosUsuario(Context context)
    {
        SharedPreferences sharedPreferences = getPreferenciasCompartidas(context);
        String usuario = sharedPreferences.getString("usuario", "");
        getAppDataBaseInstance(context).transaccionDao().eliminarTransaccionesUsuario(usuario);
        getAppDataBaseInstance(context).metaDao().eliminarMetasUsuario(usuario);
    }

    public static void eliminarUsuario(Context context)
    {
        SharedPreferences sharedPreferences = getPreferenciasCompartidas(context);
        String usuario = sharedPreferences.getString("usuario", "");
        eliminarDatosUsuario(context);
        getAppDataBaseInstance(context).usuarioDao().eliminarUsuario(usuario);
    }

    public static String encriptarContrasenha(String usuario, String contrasenha) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String usuarioPadded = String.format("%-16s", usuario).substring(0, 16);
        Key key = new SecretKeySpec(usuarioPadded.getBytes(),"AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key);
        return new String (c.doFinal(contrasenha.getBytes()));
    }

    public static SharedPreferences getPreferenciasCompartidas(Context context)
    {
        return context.getSharedPreferences("user_preferences",Context.MODE_PRIVATE);
    }
}