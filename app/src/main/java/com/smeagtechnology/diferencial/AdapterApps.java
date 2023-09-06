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

public class AdapterApps extends RecyclerView.Adapter<AdapterApps.ProductsViewHolder> implements View.OnClickListener{
    private View.OnClickListener listener;
    private Context miCtextapps;
    private List<Apps> appslist;

    public AdapterApps(Context miCtextapps, List<Apps>appslist){
        this.miCtextapps=miCtextapps;
        this.appslist=appslist;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(miCtextapps);
        View view = inflater.inflate(R.layout.listarapps, null);
view.setOnClickListener(this);
        return new ProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        Apps productosCategoria = appslist.get(position);
        byte[] decodeString  = Base64.decode(productosCategoria.getImagenaplicacion(), Base64.DEFAULT);
        Bitmap decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        Glide.with(miCtextapps)
                .load(decodeByte)
                .into(holder.imagep);
        holder.tv1p.setText(productosCategoria.getNombreaplicacion());
        holder.tv2p.setText(productosCategoria.getDescripcionaplicacion());
    }

    @Override
    public int getItemCount() {
        return appslist.size();
    }


    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }


    class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView tv1p, tv2p, tv3p;
        ImageView imagep;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1p = itemView.findViewById(R.id.textview_title_app);
            tv2p = itemView.findViewById(R.id.textview_description_app);
            tv3p = itemView.findViewById(R.id.ver);
            imagep = itemView.findViewById(R.id.img_app);
        }
    }









}
