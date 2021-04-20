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

import java.util.List;


public class AdapterProducto extends RecyclerView.Adapter<AdapterProducto.viewholderproductos> {
    Context mContext;

    final List<ProductoModel> productoModelList;  // Lista

    public AdapterProducto(List<ProductoModel> productoModelL) {  //Constructor
        this.productoModelList = productoModelL;
    }
    @NonNull
    @Override
    public viewholderproductos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_productos,parent,false);  //layout
        View v = inflater.inflate(R.layout.lista_productos,parent,false);  //layout
        return new viewholderproductos(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholderproductos holder, int position) {

        ProductoModel pr = productoModelList.get(position);
        holder.nombre.setText(pr.getNombre());
        holder.precio.setText(String.valueOf(pr.getPrecio()));
        holder.cantidad.setText(String.valueOf(pr.getCantidad()));
        holder.categoria.setText(pr.getCategoria());
        Glide.with(mContext).load(pr.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return productoModelList.size();
    }

    public static class viewholderproductos extends RecyclerView.ViewHolder {
        final TextView nombre;
        final TextView precio;
        final TextView cantidad;
        final TextView categoria;
        final ImageView image;

        public viewholderproductos(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_p);
            precio = itemView.findViewById(R.id.precio_p);
            cantidad = itemView.findViewById(R.id.cantidad_p);
            categoria =itemView.findViewById(R.id.categoria_p);
            image = itemView.findViewById(R.id.image_p);
        }
    }
}
