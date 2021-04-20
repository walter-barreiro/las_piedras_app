package com.example.laspiedrasapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.models.ProductoModel;

import java.util.ArrayList;
import java.util.List;


public class AdapterProductoFiltrado extends RecyclerView.Adapter<AdapterProductoFiltrado.ViewHolderpruebas> implements View.OnClickListener{
    final LayoutInflater inflater;
    final List<ProductoModel> model;
    private Context context;

    //Listener
    private View.OnClickListener listener;

    public AdapterProductoFiltrado(Context context, ArrayList<ProductoModel> model) {
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
        ProductoModel pr = model.get(position);

        //holder.uid.setText(String.valueOf(pr.getUid()));
        holder.nombres.setText(pr.getNombre());
        holder.precio.setText(String.valueOf(pr.getPrecio()));
        holder.cantidad.setText(String.valueOf(pr.getCantidad()));
        holder.categoria.setText(pr.getCategoria());
        Glide.with(context).load(pr.getImage()).into(holder.image);
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
        final TextView nombres;
        final TextView precio;
        final TextView cantidad;
        final TextView categoria;
        final ImageView image;

        public ViewHolderpruebas(@NonNull View itemView) {
            super(itemView);
            nombres = itemView.findViewById(R.id.nombre_p);
            precio = itemView.findViewById(R.id.precio_p);
            cantidad = itemView.findViewById(R.id.cantidad_p);
            categoria = itemView.findViewById(R.id.categoria_p);
            image = itemView.findViewById(R.id.image_p);

        }
    }
}
