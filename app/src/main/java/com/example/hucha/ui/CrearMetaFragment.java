package com.example.hucha.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.R;
import com.example.hucha.databinding.FragmentCrearMetaBinding;
import com.example.hucha.databinding.FragmentHuchaGeneralBinding;

import java.io.ByteArrayOutputStream;

public class CrearMetaFragment extends Fragment {

    private FragmentCrearMetaBinding binding;

    private Meta meta;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCrearMetaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CrearMetaFragmentArgs args = CrearMetaFragmentArgs.fromBundle(getArguments());
        meta = args.getMeta();

        if(meta!=null)
        {
            binding.tvCrearMeta.setText(R.string.title_edit_meta);

            binding.etNombreMeta.setText(meta.nombre);
            binding.etCantidadCrearMeta.setText(String.valueOf(meta.dineroObjetivo));
            binding.etCantidadInicialMeta.setText(String.valueOf(meta.dineroActual));
        }

        binding.btnCrearMeta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                guardarMeta(v);
            }
        });

        return root;
    }

    public void guardarMeta(View view)
    {
        if(binding.etNombreMeta.getText().toString().isEmpty())
        {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getResources().getString(R.string.faltan_campos_rellenar))
                    .setMessage(getResources().getString(R.string.rellenar_campo_nombre))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setNegativeButton(getResources().getString(R.string.aceptar), (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        }else if(binding.etCantidadInicialMeta.getText().toString().isEmpty())
        {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getResources().getString(R.string.faltan_campos_rellenar))
                    .setMessage(getResources().getString(R.string.rellenar_campo_cantidad_inicial))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setNegativeButton(getResources().getString(R.string.aceptar), (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        }else if(binding.etCantidadCrearMeta.getText().toString().isEmpty())
        {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getResources().getString(R.string.faltan_campos_rellenar))
                    .setMessage(getResources().getString(R.string.rellenar_campo_cantidad_objetivo))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setNegativeButton(getResources().getString(R.string.aceptar), (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        }
        else if(meta != null)
        {
            meta.nombre = binding.etNombreMeta.getText().toString();
            meta.dineroActual = Float.parseFloat(binding.etCantidadInicialMeta.getText().toString());
            meta.dineroObjetivo = Float.parseFloat(binding.etCantidadCrearMeta.getText().toString());

            Bundle result = new Bundle();
            result.putSerializable("meta", meta);

            getParentFragmentManager().setFragmentResult("editarMeta", result);
            requireActivity().getSupportFragmentManager().popBackStack();

        }else{
            Drawable dw= getResources().getDrawable(R.drawable.iconomotocicleta);
            Bitmap bm = ((BitmapDrawable) dw).getBitmap();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] bytes = outputStream.toByteArray();

            meta = new Meta(binding.etNombreMeta.getText().toString(),
                    Float.parseFloat(binding.etCantidadCrearMeta.getText().toString()),
                    Float.parseFloat(binding.etCantidadInicialMeta.getText().toString()),
                    "#ffaa9c", false, bytes, -1, false);

            Bundle result = new Bundle();
            result.putSerializable("meta", meta);

            getParentFragmentManager().setFragmentResult("crearMeta", result);
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }
}