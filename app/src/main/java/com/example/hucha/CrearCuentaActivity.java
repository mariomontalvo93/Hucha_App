package com.example.hucha;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hucha.databinding.ActivityCrearCuentaBinding;
import com.example.hucha.databinding.ActivityLoginBinding;

public class CrearCuentaActivity extends AppCompatActivity {

    private ActivityCrearCuentaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCrearCuentaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCreacionCuenta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToPantallaPrincipal(v);
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