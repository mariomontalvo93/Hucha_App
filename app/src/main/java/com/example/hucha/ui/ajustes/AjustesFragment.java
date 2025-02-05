package com.example.hucha.ui.ajustes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
        binding.ivContacto.setOnClickListener(v -> telefonoContacto());

        return root;
    }

    private void cerrarSesion(View v)
    {
        new AlertDialog.Builder(requireContext())
                .setTitle(getResources().getString(R.string.cerrar_sesion))
                .setMessage(getResources().getString(R.string.aviso_cerrar_sesion))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getResources().getString(R.string.eliminar), (dialog, which) -> {
                    SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usuario", null);
                    editor.apply();

                    Toast.makeText(context,context.getString(R.string.cierre_sesion_correcto), Toast.LENGTH_LONG);

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton(getResources().getString(R.string.cancelar), (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
    }

    private void telefonoContacto()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:123456789")); // Reemplaza con el número deseado
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}