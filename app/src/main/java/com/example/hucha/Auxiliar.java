package com.example.hucha;

import android.content.Context;
import android.widget.Toast;

import com.example.hucha.modelo.Usuario;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Auxiliar {

    private static String fileName = "Usuarios.dat";

    public static boolean registrarUsuario(Usuario usuario, Context context) throws IOException, ClassNotFoundException {
        ArrayList<Usuario> usuarios = obtenerDatosUsuarios(context);
        for(Usuario u :usuarios){
            if(u.getEmail().equals(usuario.getEmail())){
                return false;
            }
        }
        File f = new File(context.getFilesDir(), fileName);

        FileOutputStream fOs = new FileOutputStream(f,true);
        ObjectOutputStream oOs = new ObjectOutputStream(fOs);
        oOs.writeObject(usuario);
        oOs.close();
        return true;
    }

    public static ArrayList<Usuario> obtenerDatosUsuarios(Context context) throws IOException, ClassNotFoundException {
        ArrayList<Usuario>usuarios= new ArrayList<>();
        File f = new File(context.getFilesDir(), fileName);
        if(f.exists()) {
            FileInputStream fI = new FileInputStream(f);
            ObjectInputStream oIs = new ObjectInputStream(fI);
            try {
                while (true) {
                    usuarios.add((Usuario) oIs.readObject());
                }
            } catch (EOFException e) {

            }
            oIs.close();
        }else{
            f.createNewFile();
        }

        return usuarios;
    }


    public static boolean iniciarSesionUsuario(String usuario, String contrasenha, Context context) throws IOException, ClassNotFoundException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        ArrayList<Usuario> usuarios = obtenerDatosUsuarios(context);
        for(Usuario u :usuarios){
            String usus = u.getEmail();
            if(u.getEmail().equals(usuario)){
                return u.getContrasenha().equals(Usuario.encriptarContrasenha(usuario, contrasenha));

            }
        }
        return false;
    }
}