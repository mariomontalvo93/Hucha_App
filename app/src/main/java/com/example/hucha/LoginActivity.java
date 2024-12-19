package com.example.hucha;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hucha.databinding.ActivityLoginBinding;
import com.example.hucha.databinding.ActivityMainBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=this;

        binding.btnCrearCuenta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToPantallaCrearCuenta(v);
            }
        });

        binding.btnIniciarSesion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    if(Auxiliar.iniciarSesionUsuario(binding.etEmailInicioSesion.getText().toString(),binding.etPasswordInicioSesion.getText().toString(),context)){
                        goToPantallaPrincipal(v);
                    } else {
                        Toast.makeText(context,"El usuario o la contraseña son incorrectos.",Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    Toast.makeText(context,"Ha ocurrido un error al iniciar sesión. Inténtelo más tarde.",Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.btnIniciarSesionGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //goToPantallaPrincipal(v);
            }
        });

        checkAndRequestPermissions();
    }

    public void goToPantallaPrincipal(View v)
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToPantallaCrearCuenta(View v)
    {
        Intent intent = new Intent(LoginActivity.this, CrearCuentaActivity.class);
        startActivity(intent);
    }

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Lista de permisos necesarios
            String[] permissions = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            // Lista para verificar permisos no otorgados
            boolean shouldRequest = false;
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    shouldRequest = true;
                    break;
                }
            }

            // Solicitar permisos si alguno no está otorgado
            if (shouldRequest) {
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            boolean allGranted = true;

            // Verificar cada permiso
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    allGranted = false;
                }
            }

            if (!allGranted) {
                // Todos los permisos fueron otorgados
                Toast.makeText(this, "Faltan permisos para que la app funcione correctamente", Toast.LENGTH_LONG).show();
            }
        }
    }

}