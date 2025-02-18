package com.example.hucha.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hucha.Auxiliar;
import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.BBDD.Modelo.Transaccion;
import com.example.hucha.LoginActivity;
import com.example.hucha.R;
import com.example.hucha.adapter.MetaAdapter;
import com.example.hucha.adapter.TransaccionAdapter;
import com.example.hucha.databinding.FragmentCrearMetaBinding;
import com.example.hucha.databinding.FragmentDetallesMetaBinding;
import com.example.hucha.ui.home.HomeFragmentDirections;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DetallesMetaFragment extends Fragment{
    private Meta meta;
    FragmentDetallesMetaBinding binding;

    TransaccionAdapter adapter;

    List<Transaccion> transaccionesList;

    TextView tvTitle, tvCantidad;
    ImageView ivImagen;
    ImageView ivDelete, ivEdit, ivCompletada;
    ConstraintLayout cl;
    ProgressBar pb;

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

        tvTitle = cabecera.findViewById(R.id.tvTitleMetaItem);
        tvCantidad = cabecera.findViewById(R.id.tvAmountItem);
        ivImagen = cabecera.findViewById(R.id.ivMetaItem);
        ivDelete = cabecera.findViewById(R.id.btnActionDeleteMetaItem);
        ivEdit = cabecera.findViewById(R.id.btnActionEditMetaItem);
        ivCompletada = cabecera.findViewById(R.id.ivMetaCompletadaMetaItem);
        cl = cabecera.findViewById(R.id.cvMetaItem);
        pb = cabecera.findViewById(R.id.pbMetaItem);

        chargeMeta();

        comprobarMetaCompletada();

        ivDelete.setOnClickListener(v -> onClickDelete());
        ivEdit.setOnClickListener(v -> onClickEdit());

        binding.datosCabeceraDetalles.addView(cabecera);

        binding.ivAtrasDetallesMeta.setOnClickListener(v->volverAtras());

        if(meta.icono != null) {
            Bitmap bm = BitmapFactory.decodeByteArray(meta.icono, 0, meta.icono.length);
            ivImagen.setImageBitmap(bm);
        }else{
            ivImagen.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_no_image));
        }

        if(transaccionesList == null) obtenerTransacciones();
        initRecyclerView();

        binding.btnAnadirIngreso.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mostrarDialogoAnadirTransaccion(1);
            }
        });

        binding.btnAnadirRetirada.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mostrarDialogoAnadirTransaccion(2);
            }
        });

        getParentFragmentManager().setFragmentResultListener("editarMeta", this, (requestKey, result) -> {
            Meta metaParam = (Meta) result.getSerializable("meta");
            editMeta(metaParam);
        });

        return root;
    }

    private void comprobarMetaCompletada()
    {
        if(meta.logrado)
        {
            ivDelete.setVisibility(View.GONE);
            ivEdit.setVisibility(View.GONE);
            ivCompletada.setVisibility(View.VISIBLE);
            binding.btnAnadirRetirada.setVisibility(View.GONE);
            binding.btnAnadirIngreso.setVisibility(View.GONE);
        }else{
            ivDelete.setVisibility(View.VISIBLE);
            ivEdit.setVisibility(View.VISIBLE);
        }
    }

    private void chargeMeta()
    {
        tvTitle.setText(meta.nombre);
        tvCantidad.setText(meta.dineroActual + "€/" + meta.dineroObjetivo );

        Bitmap bm = BitmapFactory.decodeByteArray(meta.icono, 0 ,meta.icono.length);

        ivImagen.setImageBitmap(bm);

        pb.setMax(Math.round(meta.dineroObjetivo));
        pb.setProgress(Math.round(meta.dineroActual));

        cl.setBackgroundColor(Color.parseColor(meta.color));
    }

    private void editMeta(Meta metaParam)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Auxiliar.getAppDataBaseInstance(getContext()).metaDao().updateMeta(metaParam);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            meta.nombre = metaParam.nombre;
                            meta.dineroActual = metaParam.dineroActual;
                            meta.dineroObjetivo = metaParam.dineroObjetivo;
                            chargeMeta();
                        }
                    });
                }catch(Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Ha ocurrido un error al añadir la meta. Inténtelo más tarde.",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void initRecyclerView()
    {
        RecyclerView rv = (RecyclerView) binding.rvTrasaccionesDetalles;

        adapter = new TransaccionAdapter(transaccionesList);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.notifyDataSetChanged();
    }


    private void mostrarDialogoAnadirTransaccion (int tipoTransaccion)
    {
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint(getResources().getString(R.string.introducir_cantidad));

        final EditText inputConcepto = new EditText(getActivity());
        inputConcepto.setInputType(InputType.TYPE_CLASS_TEXT);
        inputConcepto.setHint(getResources().getString(R.string.introducir_concepto));


        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);
        layout.addView(input);
        layout.addView(inputConcepto);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.anadir_transaccion));

        if (tipoTransaccion == 1) {
            builder.setMessage(getResources().getString(R.string.introducir_abono));
        } else {
            builder.setMessage(getResources().getString(R.string.introducir_retirada));
        }

        builder.setView(layout);


        builder.setPositiveButton(getResources().getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String amount = input.getText().toString();
                if (!amount.isEmpty()) {
                    if(comprobarTransaccionCorrecta(tipoTransaccion,  Float.parseFloat(amount)))
                    {
                        Transaccion tran = new Transaccion(meta.id, tipoTransaccion, Float.parseFloat(amount),System.currentTimeMillis(), inputConcepto.getText().toString());
                        anadirTransaccionBBDD(tran);
                    }else{
                        if(tipoTransaccion == 1) {
                            new AlertDialog.Builder(requireContext())
                                    .setTitle(getResources().getString(R.string.atencion))
                                    .setMessage(getResources().getString(R.string.aviso_cantidad_actual_superior))
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(getResources().getString(R.string.aceptar), (dialog2, which2) -> {
                                        dialog2.cancel();
                                    })
                                    .show();
                        }else{
                            new AlertDialog.Builder(requireContext())
                                    .setTitle(getResources().getString(R.string.atencion))
                                    .setMessage(getResources().getString(R.string.aviso_cantidad_actual_menor_0))
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(getResources().getString(R.string.aceptar), (dialog2, which2) -> {
                                        dialog2.cancel();
                                    })
                                    .show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.cantidad_no_introducida), Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel(); // Cierra el diálogo
            }
        });

        // Mostrar el diálogo
        builder.show();
    }

    private boolean comprobarTransaccionCorrecta(int tipoTransaccion, Float cantidad)
    {
        if(tipoTransaccion == 1)
        {
            return meta.dineroObjetivo >= meta.dineroActual + cantidad;
        }else{
            return meta.dineroActual - cantidad >= 0;
        }
    }

    private void anadirTransaccionBBDD(Transaccion tran)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long id = Auxiliar.getAppDataBaseInstance(getContext()).transaccionDao().inserTransaccion(tran);
                    tran.id = id;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            transaccionesList.add(tran);
                            adapter.notifyDataSetChanged();
                            binding.tvNoHayTransaccionesDetalles.setVisibility(View.GONE);

                            Meta metaAux = meta;
                            if(tran.tipoTransaccion == 1)
                            {
                                metaAux.dineroActual += tran.cantidad;
                            }else{
                                metaAux.dineroActual -= tran.cantidad;
                            }

                            if(metaAux.dineroActual == metaAux.dineroObjetivo)
                            {
                                metaAux.logrado = true;
                            }

                            editMeta(metaAux);
                            meta = metaAux;
                            comprobarMetaCompletada();
                        }
                    });
                }catch(Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Ha ocurrido un error al añadir la transacción. Inténtelo más tarde.",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void obtenerTransacciones()
    {
        transaccionesList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Transaccion> transacciones = Auxiliar.getAppDataBaseInstance(getContext()).transaccionDao().getTransaccionMetaId(meta.id);

                    if(transacciones != null)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (transacciones.size() == 0) {
                                    binding.tvNoHayTransaccionesDetalles.setVisibility(View.VISIBLE);
                                } else {
                                    binding.tvNoHayTransaccionesDetalles.setVisibility(View.GONE);
                                    transaccionesList.clear();

                                    for(Transaccion transaccion : transacciones)
                                    {
                                        transaccionesList.add(transaccion);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.tvNoHayTransaccionesDetalles.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(),"No se han podido obtener las transacciones",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch(Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Ha ocurrido un error al obtener las transacciones. Inténtelo más tarde.",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void onClickDelete() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getResources().getString(R.string.title_eliminar_meta))
                .setMessage(getResources().getString(R.string.aviso_eliminar))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getResources().getString(R.string.eliminar), (dialog, which) -> {
                    Bundle result = new Bundle();
                    result.putSerializable("meta", meta);

                    getParentFragmentManager().setFragmentResult("eliminarMeta", result);
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton(getResources().getString(R.string.cancelar), (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
    }

    public void onClickEdit() {
        DetallesMetaFragmentDirections.ActionNavigationDetallesMetaToNavigationCrearMeta action = DetallesMetaFragmentDirections.actionNavigationDetallesMetaToNavigationCrearMeta(meta);

        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    private void volverAtras()
    {
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}