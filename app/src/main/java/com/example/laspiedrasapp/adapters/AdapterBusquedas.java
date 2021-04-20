package com.example.laspiedrasapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.models.ProductoModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterBusquedas extends FirebaseRecyclerAdapter<ProductoModel, AdapterBusquedas.myviewholder>
{
    public AdapterBusquedas(@NonNull FirebaseRecyclerOptions<ProductoModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ProductoModel model)
    {
        holder.nombre.setText(model.getNombre());
        holder.precio.setText(String.valueOf(model.getPrecio()));
        holder.cantidad.setText(String.valueOf(model.getCantidad()));
        holder.categoria.setText(model.getCategoria());
        Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_productos,parent,false);
       return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView image;
        TextView nombre,precio,cantidad,categoria;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            image=(CircleImageView)itemView.findViewById(R.id.image_p);
            nombre=(TextView)itemView.findViewById(R.id.nombre_p);
            precio=(TextView)itemView.findViewById(R.id.precio_p);
            cantidad=(TextView)itemView.findViewById(R.id.cantidad_p);
            categoria=(TextView)itemView.findViewById(R.id.categoria_p);
        }
    }
}
