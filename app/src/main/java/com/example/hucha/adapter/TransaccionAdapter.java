package com.example.hucha.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.BBDD.Modelo.Transaccion;
import com.example.hucha.R;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class TransaccionAdapter  extends RecyclerView.Adapter<TransaccionAdapter.ViewHolder> {

    private List<Transaccion> transaccionList;

    public TransaccionAdapter(List<Transaccion> transacciones)
    {
        transaccionList = transacciones;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvFecha;
        public TextView tvCantidad;

        public ImageView ivImagen;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFecha = (TextView) itemView.findViewById(R.id.tvFechaTransaccion);
            tvCantidad = (TextView) itemView.findViewById(R.id.tvImporteTransaccion);
            ivImagen = (ImageView) itemView.findViewById(R.id.ivTipoTransaccion);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View transaccionView = inflater.inflate(R.layout.transaccion_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(transaccionView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaccion tran = transaccionList.get(position);

        Instant instant = tran.fecha.toInstant();
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'del' yyyy HH:mm", new Locale("es", "ES"));

        String formattedDate = localDateTime.format(formatter);

        holder.tvFecha.setText(formattedDate);

        Context context = holder.itemView.getContext();

        if(tran.tipoTransaccion == 1)
        {
            holder.tvCantidad.setText("+" + tran.cantidad + "€");
            holder.ivImagen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_flecha_arriba));
            holder.ivImagen.setBackgroundColor(ContextCompat.getColor(context, R.color.colorIngreso));
        }else{
            holder.tvCantidad.setText("-" + tran.cantidad + "€");
            holder.ivImagen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_flecha_abajo));
            holder.ivImagen.setBackgroundColor(ContextCompat.getColor(context, R.color.colorFin));
        }
    }

    @Override
    public int getItemCount() {
        return transaccionList.size();
    }
}
