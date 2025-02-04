package com.example.hucha.ui.ajustes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hucha.Auxiliar;
import com.example.hucha.LoginActivity;
import com.example.hucha.MainActivity;
import com.example.hucha.R;
import com.example.hucha.databinding.FragmentAjustesBinding;

public class AjustesFragment extends Fragment {

    private FragmentAjustesBinding binding;
    Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAjustesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getActivity();

        binding.btnCerrarSesion.setOnClickListener(v -> cerrarSesion(v));

        return root;
    }

    public void cerrarSesion(View v)
    {
        SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario", null);
        editor.apply();

        Toast.makeText(context,context.getString(R.string.cierre_sesion_correcto), Toast.LENGTH_LONG);

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}