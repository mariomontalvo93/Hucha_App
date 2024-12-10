package com.example.hucha.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hucha.R;
import com.example.hucha.databinding.FragmentCrearMetaBinding;
import com.example.hucha.databinding.FragmentHuchaGeneralBinding;

public class CrearMetaFragment extends Fragment {

    private FragmentCrearMetaBinding binding;

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
        Navigation.findNavController(view).popBackStack();
    }
}