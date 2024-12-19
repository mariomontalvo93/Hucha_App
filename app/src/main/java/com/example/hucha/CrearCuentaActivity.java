package com.example.hucha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hucha.databinding.ActivityCrearCuentaBinding;
import com.example.hucha.databinding.ActivityLoginBinding;
import com.example.hucha.modelo.Usuario;

public class CrearCuentaActivity extends AppCompatActivity {

    private ActivityCrearCuentaBinding binding;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCrearCuentaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=this;

        binding.btnCreacionCuenta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    Usuario u = new Usuario(binding.etNombre.getText().toString(), binding.etApellidos.getText().toString(),binding.etEmailCrearCuenta.getText().toString(),Usuario.encriptarContrasenha(binding.etEmailCrearCuenta.getText().toString(), binding.etPasswordCrearCuenta.getText().toString()));
                    if(Auxiliar.registrarUsuario(u,context)){
                        goToPantallaPrincipal(v);
                    } else {
                        Toast.makeText(context,"El usuario ya existe.",Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    Toast.makeText(context,"Ha ocurrido un error al registrarse. Inténtelo más tarde.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void goToPantallaPrincipal(View v)
    {
        Intent intent = new Intent(CrearCuentaActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}