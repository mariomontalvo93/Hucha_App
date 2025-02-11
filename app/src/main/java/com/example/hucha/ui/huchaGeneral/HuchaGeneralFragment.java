package com.example.hucha.ui.huchaGeneral;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.hucha.Auxiliar;
import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.BBDD.Modelo.Transaccion;
import com.example.hucha.R;
import com.example.hucha.databinding.FragmentHuchaGeneralBinding;

import java.util.List;

public class HuchaGeneralFragment extends Fragment {

    private FragmentHuchaGeneralBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHuchaGeneralBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cargarDatosHuchaGeneral();

        return root;
    }

    private void cargarDatosHuchaGeneral()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(getContext());
                    String usuario = sharedPreferences.getString("usuario", "");

                    int metasAlcanzadas = Auxiliar.getAppDataBaseInstance(getContext()).metaDao().getMetasAlcanzadas(usuario);
                    int metasPendientes = Auxiliar.getAppDataBaseInstance(getContext()).metaDao().getMetasPendientes(usuario);
                    int dineroAhorrado = Auxiliar.getAppDataBaseInstance(getContext()).metaDao().getDineroAhorradoTotal(usuario);
                    int dineroTotal = Auxiliar.getAppDataBaseInstance(getContext()).metaDao().getDineroMetasPendientesTotal(usuario);
                    List<Transaccion> transaciones = Auxiliar.getAppDataBaseInstance(getContext()).transaccionDao().getTransaccionesEsteMes(usuario);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.tvMetasAlcanzadas.setText(getActivity().getString(R.string.metas_alcanzadas) + " "+ metasAlcanzadas);
                            binding.tvMetasPendientes.setText(getActivity().getString(R.string.metas_pendientes) + " "+ metasPendientes);

                            int dineroAhorradoMensual = 0;
                            for(Transaccion t: transaciones)
                            {
                                if(t.tipoTransaccion == 1) dineroAhorradoMensual += t.cantidad;
                                else dineroAhorradoMensual-= t.cantidad;
                            }

                            binding.tvAhorroMensual.setText(getActivity().getString(R.string.ahorro_mensual) + " " + dineroAhorradoMensual);
                            if(dineroTotal != 0)
                            {
                                binding.tvProgresoHuchaGeneral.setText(getActivity().getString(R.string.progreso_total) + " "+ (Integer)(dineroAhorrado*100/dineroTotal) + "%");
                            }else{
                                binding.tvProgresoHuchaGeneral.setText("0%");
                            }


                            String ahorroHuchaGeneral = getActivity().getString(R.string.ahorro_general, dineroAhorrado);
                            String totalHuchaGeneral = getActivity().getString(R.string.total_hucha_general, dineroTotal);

                            binding.tvTotalAhorradoHuchaGeneral.setText(Html.fromHtml(ahorroHuchaGeneral, Html.FROM_HTML_MODE_LEGACY));
                            binding.tvTotalDineroHuchaGeneral.setText(Html.fromHtml(totalHuchaGeneral, Html.FROM_HTML_MODE_LEGACY));

                            binding.pbHuchaGeneral.setMax(dineroTotal);
                            binding.pbHuchaGeneral.setProgress(dineroAhorrado);
                            binding.pbHuchaGeneral.setMin(0);

                        }
                    });

                }catch(Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),"Han ocurrido errores al intentar recuperar los datos de hucha general. Intentelo más tarde.",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}