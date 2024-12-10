package com.example.hucha.ui.home;

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

import com.example.hucha.R;
import com.example.hucha.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

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

        binding.llDetalles.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                goToVisualizarMetaFragment(v);
            }
        });

        return root;
    }

    public void goToCreationFragment(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_crear_meta);
    }

    public void goToVisualizarMetaFragment(View view){
        Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_detalles_meta);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}