package com.example.laspiedrasapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.models.ProfileProductModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterBusquedas extends FirebaseRecyclerAdapter<ProfileProductModel, AdapterBusquedas.myviewholder>
{
    public AdapterBusquedas(@NonNull FirebaseRecyclerOptions<ProfileProductModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ProfileProductModel model)
    {
        holder.nombre.setText(model.getProduct_name());
        holder.precio.setText(model.getProduct_price());
        holder.descripcion.setText(model.getProduct_description());
        holder.categoria.setText(model.getProduct_category());
        holder.stock.setText(model.getProduct_stock());
        Glide.with(holder.image.getContext()).load(model.getProduct_image_url()).into(holder.image);
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
        TextView nombre,precio,descripcion,categoria,stock;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            nombre=(TextView)itemView.findViewById(R.id.nombre_p);
            precio=(TextView)itemView.findViewById(R.id.precio_p);
            descripcion=(TextView)itemView.findViewById(R.id.descripcion_p);
            categoria=(TextView)itemView.findViewById(R.id.categoria_p);
            stock=(TextView)itemView.findViewById(R.id.stock_p);
            image=(CircleImageView)itemView.findViewById(R.id.image_p);
        }
    }
}
