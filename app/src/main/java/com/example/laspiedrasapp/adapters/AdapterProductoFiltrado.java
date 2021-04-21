package com.example.laspiedrasapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.models.ProfileProductModel;

import java.util.ArrayList;
import java.util.List;


public class AdapterProductoFiltrado extends RecyclerView.Adapter<AdapterProductoFiltrado.ViewHolderpruebas> implements View.OnClickListener{
    final LayoutInflater inflater;
    final List<ProfileProductModel> model;
    private Context context;

    //Listener
    private View.OnClickListener listener;

    public AdapterProductoFiltrado(Context context, ArrayList<ProfileProductModel> model) {
        this.inflater = LayoutInflater.from(context);
        this.model = model;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolderpruebas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_productos,parent,false);  //layout
        ViewHolderpruebas holder = new ViewHolderpruebas(v);
        v.setOnClickListener(this);
        return new ViewHolderpruebas(v);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderpruebas holder, int position) {
        ProfileProductModel pr = model.get(position);

        //holder.uid.setText(String.valueOf(pr.getUid()));
        holder.nombre.setText(pr.getProduct_name());
        holder.precio.setText(pr.getProduct_price());
        holder.descripcion.setText(pr.getProduct_description());
        holder.categoria.setText(pr.getProduct_category());
        holder.stock.setText(pr.getProduct_stock());
        Glide.with(holder.image.getContext()).load(pr.getProduct_image_url()).into(holder.image);

    }

    //@Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null){
            listener.onClick(v);
        }
    }

    public static class ViewHolderpruebas extends RecyclerView.ViewHolder{
        final TextView nombre;
        final TextView precio;
        final TextView descripcion;
        final TextView categoria;
        final TextView stock;
        final ImageView image;

        public ViewHolderpruebas(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_p);
            precio = itemView.findViewById(R.id.precio_p);
            descripcion = itemView.findViewById(R.id.descripcion_p);
            categoria = itemView.findViewById(R.id.categoria_p);
            stock = itemView.findViewById(R.id.stock_p);
            image = itemView.findViewById(R.id.image_p);

        }
    }
}
