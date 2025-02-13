package com.example.hucha.ui.ajustes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.hucha.Auxiliar;
import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.BBDD.Modelo.Transaccion;
import com.example.hucha.LoginActivity;
import com.example.hucha.R;
import com.example.hucha.databinding.FragmentAjustesBinding;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class AjustesFragment extends Fragment {

    private FragmentAjustesBinding binding;
    Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAjustesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getActivity();

        binding.btnCerrarSesion.setOnClickListener(v -> cerrarSesion(v));
        binding.ivContacto.setOnClickListener(v -> telefonoContacto());
        binding.ivTwitter.setOnClickListener(v -> abrirX());
        binding.ivEmail.setOnClickListener(v -> contactarViaEmail());
        binding.btnReiniciarDatos.setOnClickListener(v -> reiniciarDatos());
        binding.btnEliminarCuenta.setOnClickListener(v -> reiniciarUsuario());
        binding.btnExportarDatos.setOnClickListener(v -> exportarDatosAExcel());

        return root;
    }

    private void exportarDatosAExcel()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(getContext());
                    String usuario = sharedPreferences.getString("usuario", "");
                    List<Meta> metas = Auxiliar.getAppDataBaseInstance(getContext()).metaDao().getMetasByUsuarioId(usuario);
                    List<Transaccion> transacciones =Auxiliar.getAppDataBaseInstance(getContext()).transaccionDao().getTransaccionPorUsuario(usuario);

                    // Crear archivo Excel
                    Workbook workbook = new XSSFWorkbook();

                    // Agregar hojas
                    createMetaSheet(workbook, metas);
                    createTransaccionSheet(workbook, transacciones);

                    // Guardar archivo
                    File file = new File(context.getExternalFilesDir(null), "datos_room_usuario_"+usuario+".xlsx");
                    FileOutputStream fileOut = new FileOutputStream(file);
                    workbook.write(fileOut);
                    fileOut.close();
                    workbook.close();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                File file = new File(context.getExternalFilesDir(null), "datos_room_usuario_"+usuario+".xlsx");
                                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(uri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                context.startActivity(Intent.createChooser(intent, "Abrir archivo Excel"));
                            } catch (Exception e) {
                                Toast.makeText(context, "No se pudo abrir el archivo", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }catch(Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),context.getString(R.string.eliminar_datos_error),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    // Método para crear la hoja "Meta"
    private void createMetaSheet(Workbook workbook, List<Meta> metas) {
        Sheet sheet = workbook.createSheet("Metas");
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Nombre", "Dinero Objetivo", "Dinero Actual", "Color", "Logrado", "Icono", "Icono Genérico", "Online", "ID Usuario"};

        // Crear encabezados
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Agregar datos
        int rowNum = 1;
        for (Meta meta : metas) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(meta.id);
            row.createCell(1).setCellValue(meta.nombre);
            row.createCell(2).setCellValue(meta.dineroObjetivo);
            row.createCell(3).setCellValue(meta.dineroActual);
            row.createCell(4).setCellValue(meta.color);
            row.createCell(5).setCellValue(meta.logrado ? "Sí" : "No");
            row.createCell(6).setCellValue(meta.icono != null ? "Sí" : "No");
            row.createCell(7).setCellValue(meta.iconoGenerico);
            row.createCell(8).setCellValue(meta.online ? "Sí" : "No");
            row.createCell(9).setCellValue(meta.idUsuario);
        }
    }

    // Método para crear la hoja "Transacciones"
    private void createTransaccionSheet(Workbook workbook, List<Transaccion> transacciones) {
        Sheet sheet = workbook.createSheet("Transacciones");
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Meta ID", "Tipo Transacción", "Cantidad", "Concepto", "Fecha"};

        // Crear encabezados
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Agregar datos
        int rowNum = 1;
        for (Transaccion transaccion : transacciones) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(transaccion.id);
            row.createCell(1).setCellValue(transaccion.metaId);
            row.createCell(2).setCellValue(transaccion.tipoTransaccion);
            row.createCell(3).setCellValue(transaccion.cantidad);
            row.createCell(4).setCellValue(transaccion.concepto != null ? transaccion.concepto : "");
            row.createCell(5).setCellValue(transaccion.fecha);
        }
    }

    private void reiniciarDatos()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Auxiliar.eliminarDatosUsuario(getContext());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),context.getString(R.string.eliminar_datos_correcto),Toast.LENGTH_LONG).show();
                        }
                    });
                }catch(Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),context.getString(R.string.eliminar_datos_error),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void reiniciarUsuario()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Auxiliar.eliminarUsuario(getContext());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(context);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("usuario", null);
                            editor.apply();

                            Toast.makeText(context,context.getString(R.string.cierre_sesion_correcto), Toast.LENGTH_LONG);

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });

                }catch(Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(),context.getString(R.string.eliminar_datos_error),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void cerrarSesion(View v)
    {
        new AlertDialog.Builder(requireContext())
                .setTitle(getResources().getString(R.string.cerrar_sesion))
                .setMessage(getResources().getString(R.string.aviso_cerrar_sesion))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getResources().getString(R.string.aceptar), (dialog, which) -> {
                    SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("usuario", null);
                    editor.apply();

                    Toast.makeText(context,context.getString(R.string.cierre_sesion_correcto), Toast.LENGTH_LONG);

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton(getResources().getString(R.string.cancelar), (dialog, which) -> {
                    dialog.cancel();
                })
                .show();
    }

    private void telefonoContacto()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:123456789")); // Reemplaza con el número deseado
        startActivity(intent);
    }

    private void abrirX()
    {
        String url = "https://x.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void contactarViaEmail()
    {
        Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
        emailSelectorIntent.setData(Uri.parse("mailto:"));

        SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(getContext());
        String usuario = sharedPreferences.getString("usuario", "");

        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"huchaSoporte@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "UsuarioId: "+ usuario);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        emailIntent.setSelector( emailSelectorIntent );

        startActivity(emailIntent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}