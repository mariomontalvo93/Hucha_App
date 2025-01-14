package com.example.hucha.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.R;
import com.example.hucha.databinding.FragmentCrearMetaBinding;
import com.example.hucha.databinding.FragmentDetallesMetaBinding;

public class DetallesMetaFragment extends Fragment {
    private Meta meta;
    FragmentDetallesMetaBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentDetallesMetaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DetallesMetaFragmentArgs args = DetallesMetaFragmentArgs.fromBundle(getArguments());
        meta = args.getMeta();

        View cabecera = inflater.inflate(R.layout.meta_item, binding.datosCabeceraDetalles, false);

        TextView tvTitle = (TextView) cabecera.findViewById(R.id.tvTitleMetaItem);
        TextView tvCantidad = (TextView) cabecera.findViewById(R.id.tvAmountItem);
        ImageView ivImagen = (ImageView) cabecera.findViewById(R.id.ivMetaItem);
        ConstraintLayout cl = (ConstraintLayout) cabecera.findViewById(R.id.cvMetaItem);

        tvTitle.setText(meta.nombre);
        tvCantidad.setText(meta.dineroActual + "€/" + meta.dineroObjetivo );

        Bitmap bm = BitmapFactory.decodeByteArray(meta.icono, 0 ,meta.icono.length);

        ivImagen.setImageBitmap(bm);
        cl.setBackgroundColor(Color.parseColor(meta.color));

        binding.datosCabeceraDetalles.addView(cabecera);

        return root;
    }
}