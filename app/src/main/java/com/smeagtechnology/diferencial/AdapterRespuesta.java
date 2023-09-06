package com.smeagtechnology.diferencial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterRespuesta extends RecyclerView.Adapter<AdapterRespuesta.ProductsViewHolder> implements View.OnClickListener{
    private View.OnClickListener listener;
    private Context miCtextrta;
    private List<Respuesta> rtalist;

    public AdapterRespuesta(Context miCtextrta, List<Respuesta>rtalist){
        this.miCtextrta=miCtextrta;
        this.rtalist=rtalist;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(miCtextrta);
        View view = inflater.inflate(R.layout.listarrespuesta, null);
view.setOnClickListener(this);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Respuesta productosCategoria = rtalist.get(position);

        holder.contenedor_respuesta.setAnimation(AnimationUtils.loadAnimation(miCtextrta, R.anim.fade_transition_animation));
        byte[] decodeString  = Base64.decode(productosCategoria.getImagenrespuesta(), Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Glide.with(miCtextrta)
                .load(decodeByte)
                .into(holder.img_punto);
        holder.textview_punto.setText(productosCategoria.getNombrerespuesta());
    }

    @Override
    public int getItemCount() {
        return rtalist.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }


    class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView textview_punto;
        ImageView img_punto;
        RelativeLayout contenedor_respuesta;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            textview_punto = itemView.findViewById(R.id.textview_punto);
            img_punto = itemView.findViewById(R.id.img_punto);
            contenedor_respuesta = itemView.findViewById(R.id.contenedor_respuestas);
        }
    }
}
