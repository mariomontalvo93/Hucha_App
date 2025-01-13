package com.example.hucha.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hucha.BBDD.Modelo.Meta;
import com.example.hucha.R;

import java.sql.SQLException;
import java.util.List;

public class MetaAdapter extends RecyclerView.Adapter<MetaAdapter.ViewHolder> {

    private List<Meta> metasList;
    private OnClickItem mClickItem;

    public MetaAdapter(List<Meta> metas, OnClickItem clickItem)
    {
        metasList = metas;
        mClickItem = clickItem;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvTitle;

        public ImageView ivImagen;

        public OnClickItem onClickItem;

        public ViewHolder(View itemView, OnClickItem clickItem) {
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitleMetaItem);
            ivImagen = (ImageView) itemView.findViewById(R.id.ivMetaItem);

            onClickItem = clickItem;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){ onClickItem.onClickItem(getAdapterPosition());}
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View metaView = inflater.inflate(R.layout.meta_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(metaView, mClickItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Meta meta = metasList.get(position);

        holder.tvTitle.setText(meta.nombre);

        Bitmap bm = BitmapFactory.decodeByteArray(meta.icono, 0 ,meta.icono.length);

        holder.ivImagen.setImageBitmap(bm);
    }

    @Override
    public int getItemCount() {
        return metasList.size();
    }


    public interface OnClickItem {
        void onClickItem(int position);
    }
}
