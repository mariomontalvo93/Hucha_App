package com.example.hucha.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hucha.Auxiliar;
import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.BBDD.Modelo.Usuario;
import com.example.hucha.R;
import com.example.hucha.adapter.MetaAdapter;
import com.example.hucha.databinding.FragmentHomeBinding;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements MetaAdapter.OnClickItem {

    private FragmentHomeBinding binding;

    private List<Meta> metasList;
    private Context context;

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

        context = getContext();

        if(metasList==null) obtenerMetas();

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

    private void obtenerMetas(){
        metasList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(context);
                    String usuario = sharedPreferences.getString("usuario", "");
                    List<Meta> metas = Auxiliar.getAppDataBaseInstance(context).metaDao().getMetasByUsuarioId(usuario);

                    if(metas != null)
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (metas.size() == 0) {
                                    binding.tvNoHayMetasHome.setVisibility(View.VISIBLE);
                                } else {
                                    binding.tvNoHayMetasHome.setVisibility(View.GONE);
                                    metasList.clear();

                                    for(Meta meta : metas)
                                    {
                                        metasList.add(meta);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.tvNoHayMetasHome.setVisibility(View.VISIBLE);
                                Toast.makeText(context,"No se han podido obtener las metas",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch(Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"Ha ocurrido un error al obtener las metas. Inténtelo más tarde.",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
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