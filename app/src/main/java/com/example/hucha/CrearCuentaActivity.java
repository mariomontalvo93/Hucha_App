package com.example.hucha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hucha.BBDD.Modelo.Usuario;
import com.example.hucha.databinding.ActivityCrearCuentaBinding;

public class CrearCuentaActivity extends AppCompatActivity {

    private ActivityCrearCuentaBinding binding;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCrearCuentaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=this;

        binding.btnCreacionCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Usuario u = new Usuario(binding.etNombre.getText().toString(), binding.etApellidos.getText().toString(), binding.etEmailCrearCuenta.getText().toString(), Auxiliar.encriptarContrasenha(binding.etEmailCrearCuenta.getText().toString(), binding.etPasswordCrearCuenta.getText().toString()));
                            long usuario = Auxiliar.getAppDataBaseInstance(context).usuarioDao().insertUsuario(u);
                            if (usuario != -1) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(context);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("usuario", String.valueOf(usuario));
                                        editor.apply();
                                        Toast.makeText(context, "El usuario se ha creado correctamente.", Toast.LENGTH_LONG).show();
                                        goToPantallaLogin(v);
                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Ha habido un error al intentar crear el usuario.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        } catch (Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "Ha ocurrido un error al registrarse. Inténtelo más tarde.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                }).start();
            }
        });
    }

    public void goToPantallaLogin(View v)
    {
        Intent intent = new Intent(CrearCuentaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}