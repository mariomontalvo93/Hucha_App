package com.example.hucha.ui.home;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.R;
import com.example.hucha.adapter.MetaAdapter;
import com.example.hucha.databinding.FragmentHomeBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MetaAdapter.OnClickItem {

    private FragmentHomeBinding binding;

    private List<Meta> metasList;

    private MetaAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.fabCrearMeta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToCreationFragment(v);
            }
        });

        if(metasList == null) metasList = datosMetasPrueba();

        initRecyclerView();

        // Escucha el resultado desde FragmentCrearMeta
        getParentFragmentManager().setFragmentResultListener("crearMeta", this, (requestKey, result) -> {
            Meta meta = (Meta) result.getSerializable("meta");
            anadirMeta(meta);
        });

        getParentFragmentManager().setFragmentResultListener("eliminarMeta", this, (requestKey, result) -> {
            int idMeta = result.getInt("idMeta");
            eliminarMeta(idMeta);
        });

        return root;
    }

    private void anadirMeta(Meta meta)
    {
        metasList.add(meta);
        adapter.notifyDataSetChanged();
    }

    private void eliminarMeta(int idMeta)
    {
        for(int i=0; i<metasList.size();i++)
        {
            if(metasList.get(i).id == idMeta)
            {
                metasList.remove(i);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    private List<Meta> datosMetasPrueba()
    {
        List<Meta> metas =
                new ArrayList<>();
        Drawable dw= getResources().getDrawable(R.drawable.iconomotocicleta);
        Bitmap bm = ((BitmapDrawable) dw).getBitmap();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();

        metas.add(new Meta("Meta1", 100, 20, "#ffaa9c", false, bytes, -1, false));
        metas.add(new Meta("Meta2", 100, 30, "#FFC000", false, bytes, -1, false));
        metas.add(new Meta("Meta3", 100, 40, "#b0ff9c", false, bytes, -1, false));
        metas.add(new Meta("Meta4", 100, 50, "#51c3be", false, bytes, -1, false));
        metas.add(new Meta("Meta5", 100, 60, "#517cc3", false, bytes, -1, false));
        metas.add(new Meta("Meta6", 100, 70, "#ba51c3", false, bytes, -1, false));
        metas.add(new Meta("Meta7", 100, 80, "#c351a0", false, bytes, -1, false));

        return metas;
    }

    private void initRecyclerView()
    {
        RecyclerView rv = (RecyclerView) binding.rvMetas;

        adapter = new MetaAdapter(metasList, this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter.notifyDataSetChanged();
    }

    public void goToCreationFragment(View view){
        HomeFragmentDirections.ActionNavigationHomeToNavigationCrearMeta action = HomeFragmentDirections.actionNavigationHomeToNavigationCrearMeta(null);

        Navigation.findNavController(binding.getRoot()).navigate(action);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClickItem(int position) {
        HomeFragmentDirections.ActionNavigationHomeToNavigationDetallesMeta action = HomeFragmentDirections.actionNavigationHomeToNavigationDetallesMeta(metasList.get(position));

        Navigation.findNavController(binding.getRoot()).navigate(action);
    }
}