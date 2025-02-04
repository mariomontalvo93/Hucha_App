package com.example.hucha;

import android.content.Context;
import android.content.SharedPreferences;

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