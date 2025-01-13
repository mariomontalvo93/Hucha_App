package com.example.hucha.ui.home;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
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

        return root;
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

        metas.add(new Meta("Meta1", 20, 20, "FFFFF", false, bytes, -1, false));
        metas.add(new Meta("Meta2", 30, 30, "FFFFF", false, bytes, -1, false));
        metas.add(new Meta("Meta3", 40, 40, "FFFFF", false, bytes, -1, false));
        metas.add(new Meta("Meta4", 50, 50, "FFFFF", false, bytes, -1, false));
        metas.add(new Meta("Meta5", 60, 60, "FFFFF", false, bytes, -1, false));

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
        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_crear_meta);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClickItem(int position) {
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigation_home_to_navigation_detalles_meta);
    }
}