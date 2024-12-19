package com.example.hucha.modelo;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Usuario implements Serializable {
    private String nombre;
    private String apellido;
    private String email;
    private String contrasenha;

    public Usuario(String nombre, String apellido, String email, String contrasenha) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenha = contrasenha;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }

    public static String encriptarContrasenha(String usuario, String contrasenha) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String usuarioPadded = String.format("%-24s", usuario).substring(0, 24);
        Key key = new SecretKeySpec(usuarioPadded.getBytes(),"AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key);
        return new String (c.doFinal(contrasenha.getBytes()));
    }
}