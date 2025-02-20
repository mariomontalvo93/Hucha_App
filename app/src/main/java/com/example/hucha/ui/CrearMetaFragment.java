package com.example.hucha.ui;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.hucha.Auxiliar;
import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.R;
import com.example.hucha.databinding.FragmentCrearMetaBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CrearMetaFragment extends Fragment {

    private static final int REQUEST_CAMERA = 100;
    private static final int REQUEST_GALLERY = 200;
    private static final String colorMetaDefecto = "#71B44E";

    private FragmentCrearMetaBinding binding;

    private Meta meta;

    private String colorMeta;

    private Bitmap imagenActual = null;

    private Uri imagenCamara = null;

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

        CrearMetaFragmentArgs args = CrearMetaFragmentArgs.fromBundle(getArguments());
        meta = args.getMeta();

        colorMeta = colorMetaDefecto;

        if(meta!=null)
        {
            binding.tvCrearMeta.setText(R.string.title_edit_meta);
            binding.btnCrearMeta.setText(R.string.title_edit_meta);

            binding.etNombreMeta.setText(meta.nombre);
            binding.etCantidadCrearMeta.setText(String.valueOf(meta.dineroObjetivo));
            binding.etCantidadInicialMeta.setText(String.valueOf(meta.dineroActual));

            binding.etCantidadInicialMeta.setEnabled(false);
            colorMeta = meta.color;

            if(meta.rutaIcono!=null) {
                File imgFile = new File(meta.rutaIcono);
                Bitmap bitmap = null;
                if (imgFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                }

                if (bitmap != null) {
                    binding.ivIconoMeta.setImageBitmap(bitmap);
                }
            }
        }

        binding.btnColor.setBackgroundColor(Color.parseColor(colorMeta));

        binding.btnColor.setOnClickListener(v -> seleccionarColorMeta());

        binding.ivAtrasCrearMeta.setOnClickListener(v->volverAtras());

        binding.ivIconoMeta.setOnClickListener(v -> seleccionarImagen());

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
        if(binding.etNombreMeta.getText().toString().isEmpty())
        {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getResources().getString(R.string.faltan_campos_rellenar))
                    .setMessage(getResources().getString(R.string.rellenar_campo_nombre))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setNegativeButton(getResources().getString(R.string.aceptar), (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        }else if(binding.etCantidadInicialMeta.getText().toString().isEmpty())
        {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getResources().getString(R.string.faltan_campos_rellenar))
                    .setMessage(getResources().getString(R.string.rellenar_campo_cantidad_inicial))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setNegativeButton(getResources().getString(R.string.aceptar), (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        }else if(binding.etCantidadCrearMeta.getText().toString().isEmpty())
        {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getResources().getString(R.string.faltan_campos_rellenar))
                    .setMessage(getResources().getString(R.string.rellenar_campo_cantidad_objetivo))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setNegativeButton(getResources().getString(R.string.aceptar), (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        }else if(Float.parseFloat(binding.etCantidadCrearMeta.getText().toString()) <= 0)
        {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getResources().getString(R.string.faltan_campos_rellenar))
                    .setMessage(getResources().getString(R.string.cantidad_superior_0))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setNegativeButton(getResources().getString(R.string.aceptar), (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        }
        else if(Float.parseFloat(binding.etCantidadInicialMeta.getText().toString())>Float.parseFloat(binding.etCantidadCrearMeta.getText().toString()))
        {
            new AlertDialog.Builder(requireContext())
                    .setTitle(getResources().getString(R.string.faltan_campos_rellenar))
                    .setMessage(getResources().getString(R.string.rellenar_campo_cantidad_objetivo))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setNegativeButton(getResources().getString(R.string.aceptar), (dialog, which) -> {
                        dialog.cancel();
                    })
                    .show();
        }
        else if(meta != null)
        {
            if(imagenActual != null)
            {
                String path = Auxiliar.guardarImagenEnAlmacenamientoExterno(imagenActual, getContext(), meta.id);
                meta.rutaIcono = path;
            }
            meta.nombre = binding.etNombreMeta.getText().toString();
            meta.dineroActual = Float.parseFloat(binding.etCantidadInicialMeta.getText().toString());
            meta.dineroObjetivo = Float.parseFloat(binding.etCantidadCrearMeta.getText().toString());
            meta.color = colorMeta;

            Bundle result = new Bundle();
            result.putSerializable("meta", meta);

            getParentFragmentManager().setFragmentResult("editarMeta", result);
            requireActivity().getSupportFragmentManager().popBackStack();

        }else{
            SharedPreferences sharedPreferences = Auxiliar.getPreferenciasCompartidas(getContext());
            String usuario = sharedPreferences.getString("usuario", "");

            Drawable dw= getResources().getDrawable(R.drawable.cerdito);
            Bitmap bm = ((BitmapDrawable) dw).getBitmap();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

            meta = new Meta(binding.etNombreMeta.getText().toString(),
                    Float.parseFloat(binding.etCantidadCrearMeta.getText().toString()),
                    Float.parseFloat(binding.etCantidadInicialMeta.getText().toString()),
                    colorMeta, false, null, -1, false,usuario);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        long id = Auxiliar.getAppDataBaseInstance(getContext()).metaDao().inserMeta(meta);

                        meta.id = id;

                        if(imagenActual != null)
                        {
                            String path = Auxiliar.guardarImagenEnAlmacenamientoExterno(imagenActual, getContext(), meta.id);
                            meta.rutaIcono = path;
                            Auxiliar.getAppDataBaseInstance(getContext()).metaDao().updateMeta(meta);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bundle result = new Bundle();
                                result.putSerializable("meta", meta);

                                getParentFragmentManager().setFragmentResult("crearMeta", result);
                                requireActivity().getSupportFragmentManager().popBackStack();
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
    }

    private void seleccionarColorMeta()
    {
        String[] colorNames = {"Rojo", "Azul claro", "Azul oscuro", "Verde", "Amarillo", "Naranja", "Morado", "Gris claro", "Gris oscuro", "Marrón"};
        final int[] colors = {
                Color.parseColor("#a12312"), // Rojo
                Color.parseColor("#5edced"), // Azul claro
                Color.parseColor("#5b39c6"), // Azul oscuro
                Color.parseColor("#008f39"), // Verde
                Color.parseColor("#ffdf00"), // Amarillo
                Color.parseColor("#ff7514"), // Naranja
                Color.parseColor("#7f69a5"), // Morado
                Color.parseColor("#D3D3D3"), // Gris claro
                Color.parseColor("#4d5645"), // Gris oscuro
                Color.parseColor("#8e402a")  // Marrón
        };

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Selecciona un color")
                .setItems(colorNames, (dialog, which) -> {
                    //Conversión de int a hexadecimal
                    String hexColor = String.format("#%06X", (0xFFFFFF & colors[which]));
                    colorMeta = hexColor;
                    binding.btnColor.setBackgroundColor(Color.parseColor(colorMeta));
                })
                .show();
    }

    private void volverAtras()
    {
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void seleccionarImagen()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getContext().getString(R.string.seleccionar_imagen))
                .setItems(new CharSequence[]{"📷 Cámara", "🖼 Galería"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            openCamera();
                        } else {
                            openGallery();
                        }
                    }
                })
                .show();
    }

    // Método para abrir la cámara
    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            // Crear un URI para la imagen
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "Nueva Imagen");
            values.put(MediaStore.Images.Media.DESCRIPTION, "Desde la cámara");

            File fotoArchivo = null;

            try {
                fotoArchivo = crearArchivoImagen();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (fotoArchivo != null) {
                imagenCamara = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", fotoArchivo);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imagenCamara);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        }
    }

    // Método para abrir la galería
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                // Mostrar la imagen capturada
                try {
                    imagenActual = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imagenCamara);
                    imagenActual = corregirOrientacion(imagenActual, new File(imagenCamara.getPath()).getPath());
                    binding.ivIconoMeta.setImageBitmap(imagenActual);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (requestCode == REQUEST_GALLERY && data != null) {
                // Obtener la imagen de la galería
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    try {
                        imagenActual = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContext().getContentResolver(), data.getData()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                binding.ivIconoMeta.setImageBitmap(imagenActual);
            }
        }
    }

    private File crearArchivoImagen() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nombreArchivo = "IMG_" + timeStamp;
        File directorio = getContext().getExternalFilesDir(null);

        return File.createTempFile(nombreArchivo, ".jpg", directorio);
    }

    private Bitmap corregirOrientacion(Bitmap bitmap, String rutaImagen) {
        try {
            int angulo = 90;

            if (angulo != 0) {
                Matrix matrix = new Matrix();
                matrix.postRotate(angulo);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}