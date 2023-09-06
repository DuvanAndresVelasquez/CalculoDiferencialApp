package com.smeagtechnology.diferencial;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterActividad extends RecyclerView.Adapter<AdapterActividad.ProductsViewHolder> implements View.OnClickListener{
    private View.OnClickListener listener;
    private Context miCtextactividad;
    private List<Actividad> actividadlist;

    public AdapterActividad(Context miCtexttemas, List<Actividad>actividadlist){
        this.miCtextactividad=miCtexttemas;
        this.actividadlist=actividadlist;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(miCtextactividad);
        View view = inflater.inflate(R.layout.listaractividad, null);
view.setOnClickListener(this);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Actividad productosCategoria = actividadlist.get(position);
        byte[] decodeString  = Base64.decode(productosCategoria.getImagenenunciado(), Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Glide.with(miCtextactividad)
                .load(decodeByte)
                .into(holder.img_actividad);
        holder.textview_enunciado.setText(productosCategoria.getEnunciadotema());
    }

    @Override
    public int getItemCount() {
        return actividadlist.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }


    class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView textview_enunciado;
        ImageView img_actividad;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
          textview_enunciado = itemView.findViewById(R.id.textview_enunciado);
            img_actividad = itemView.findViewById(R.id.img_actividad);
        }
    }
}
