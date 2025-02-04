package com.example.hucha.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
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

import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.BBDD.Modelo.Transaccion;
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

        TextView tvTitle = cabecera.findViewById(R.id.tvTitleMetaItem);
        TextView tvCantidad = cabecera.findViewById(R.id.tvAmountItem);
        ImageView ivImagen = cabecera.findViewById(R.id.ivMetaItem);
        ImageView ivDelete = cabecera.findViewById(R.id.btnActionDeleteMetaItem);
        ImageView ivEdit = cabecera.findViewById(R.id.btnActionEditMetaItem);
        ConstraintLayout cl = cabecera.findViewById(R.id.cvMetaItem);
        ProgressBar pb = cabecera.findViewById(R.id.pbMetaItem);

        tvTitle.setText(meta.nombre);
        tvCantidad.setText(meta.dineroActual + "€/" + meta.dineroObjetivo );

        Bitmap bm = BitmapFactory.decodeByteArray(meta.icono, 0 ,meta.icono.length);

        ivImagen.setImageBitmap(bm);
        ivDelete.setOnClickListener(v -> onClickDelete());
        ivEdit.setOnClickListener(v -> onClickEdit());

        pb.setMax(Math.round(meta.dineroObjetivo));
        pb.setProgress(Math.round(meta.dineroActual));

        ivDelete.setVisibility(View.VISIBLE);
        ivEdit.setVisibility(View.VISIBLE);

        cl.setBackgroundColor(Color.parseColor(meta.color));

        binding.datosCabeceraDetalles.addView(cabecera);

        if(transaccionesList == null) transaccionesList = datosTransaccionesPrueba();
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
            Meta meta = (Meta) result.getSerializable("meta");
            editMeta(meta);
        });

        return root;
    }

    private void editMeta(Meta metaParam)
    {
        meta.nombre = metaParam.nombre;
        meta.dineroActual = metaParam.dineroActual;
        meta.dineroObjetivo = metaParam.dineroObjetivo;
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
                    Transaccion tran = new Transaccion(meta.id, tipoTransaccion,Float.parseFloat(amount),System.currentTimeMillis(), inputConcepto.getText().toString());
                    transaccionesList.add(tran);
                    adapter.notifyDataSetChanged();
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

    private List<Transaccion> datosTransaccionesPrueba()
    {
        List<Transaccion> transaccions =
                new ArrayList<>();

    /*transaccions.add(new Transaccion(1, 1, 100, new Date(1734780001200L)));
        transaccions.add(new Transaccion(1, 2, 50, new Date(1734680035000L)));
        transaccions.add(new Transaccion(1, 1, 200, new Date(1734980000000L)));
        transaccions.add(new Transaccion(1, 2, 500, new Date(1734480002800L)));
        transaccions.add(new Transaccion(1, 1, 140, new Date(1734280012000L)));*/

        return transaccions;
    }

    public void onClickDelete() {
        new AlertDialog.Builder(requireContext())
                .setTitle(getResources().getString(R.string.title_eliminar_meta))
                .setMessage(getResources().getString(R.string.aviso_eliminar))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getResources().getString(R.string.eliminar), (dialog, which) -> {
                    Bundle result = new Bundle();
                    result.putLong("idMeta", meta.id);

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
}