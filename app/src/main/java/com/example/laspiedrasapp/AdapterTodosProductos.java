package com.example.laspiedrasapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.models.ProductoModel;

import java.util.List;

public class AdapterTodosProductos extends RecyclerView.Adapter<AdapterTodosProductos.ViewHolder>{

    private List<ProductoModel> mData;
    private LayoutInflater mInflater;
    private Context context;

    final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductoModel item);
    }

    public AdapterTodosProductos(List<ProductoModel> itemList, Context context, OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.lista_productos,null);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ProductoModel> items) {mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nombre;
        TextView precio;
        TextView cantidad;
        TextView categoria;
        ImageView image;

        ViewHolder(View itemView){
            super(itemView);
            // Aca van todos los view
            nombre = itemView.findViewById(R.id.nombre_p);
            precio = itemView.findViewById(R.id.precio_p);
            cantidad = itemView.findViewById(R.id.cantidad_p);
            categoria = itemView.findViewById(R.id.categoria_p);
            image = itemView.findViewById(R.id.image_p);
        }

        void bindData(final ProductoModel item){
            // Aca va lo que se hace con los view
            nombre.setText(item.getNombre());
            precio.setText(String.valueOf(item.getPrecio()));
            cantidad.setText(String.valueOf(item.getCantidad()));
            categoria.setText(item.getCategoria());
            Glide.with(context).load(item.getImage()).into(image);

            itemView.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(mData.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }


}