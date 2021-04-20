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
import com.example.laspiedrasapp.models.CommerceProductModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CommerceMainProductAdapter extends FirebaseRecyclerAdapter<CommerceProductModel, CommerceMainProductAdapter.myviewholder>
{
    public CommerceMainProductAdapter(@NonNull FirebaseRecyclerOptions<CommerceProductModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull CommerceProductModel model)
    {
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice());
        Glide.with(holder.imgUrl.getContext()).load(model.getImgUrl()).into(holder.imgUrl);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_commerce_product,parent,false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView imgUrl;
        TextView name,price ;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            imgUrl=(ImageView) itemView.findViewById(R.id.ivItemProductCommerce);
            name=(TextView)itemView.findViewById(R.id.tvProductCommerceName);
            price=(TextView)itemView.findViewById(R.id.tvItemProductCommercePrice);

        }
    }
}