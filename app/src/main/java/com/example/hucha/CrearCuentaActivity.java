package com.example.hucha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hucha.BBDD.Modelo.Usuario;
import com.example.hucha.databinding.ActivityCrearCuentaBinding;

import java.util.regex.Pattern;

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
                if(comprobarDatosValidos()) {
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
            }
        });
    }

    private boolean comprobarDatosValidos()
    {
        String errores ="";
        boolean errorEncontrado = false;
        if(binding.etNombre.getText().toString().isEmpty() || binding.etApellidos.getText().toString().isEmpty() || binding.etEmailCrearCuenta.getText().toString().isEmpty() || binding.etPasswordCrearCuenta.getText().toString().isEmpty())
        {
            errores += getString(R.string.faltan_campos_rellenar) + "\n";
            errorEncontrado = true;
        }

        if(!validarEmail(binding.etEmailCrearCuenta.getText().toString()))
        {
            errores += getString(R.string.email_erroneo) + "\n";
            errorEncontrado = true;
        }

        String passwordOk = contrasenaCorrecta(binding.etPasswordCrearCuenta.getText().toString());
        if(!passwordOk.isEmpty())
        {
            errores += passwordOk + "\n";
            errorEncontrado = true;
        }

        if(errorEncontrado)
        {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.error_crear_cuenta))
                    .setMessage(errores)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(getResources().getString(R.string.aceptar), (dialog, which) -> {
                    })
                    .show();

            return false;
        }
        return true;
    }

    private boolean validarEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private String contrasenaCorrecta(String contrasena)
    {
        if(contrasena.length() < 8) return getString(R.string.password_mas_8_caracteres);

        boolean cumpleRequisitos = Pattern.compile("\\d").matcher(contrasena).find();
        if(!cumpleRequisitos) return getString(R.string.password_contener_numero);

        cumpleRequisitos = contrasena.matches(".*[A-Z].*") && contrasena.matches(".*[a-z].*");
        if(!cumpleRequisitos) return getString(R.string.password_minuscula_mayuscula);

        cumpleRequisitos = contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>?/].*");
        if(!cumpleRequisitos) return getString(R.string.password_caracter_especial);

        return "";
    }

    public void goToPantallaLogin(View v)
    {
        Intent intent = new Intent(CrearCuentaActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}