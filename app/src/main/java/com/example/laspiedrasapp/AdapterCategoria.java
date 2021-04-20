package com.example.laspiedrasapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.models.CategoriaModel;

import java.util.List;


public class AdapterCategoria extends RecyclerView.Adapter<AdapterCategoria.ViewHolder>  {

    private List<CategoriaModel> mData;
    private LayoutInflater mInflater;
    private Context context;
    private int pos;

    final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CategoriaModel item);
    }

    public AdapterCategoria(List<CategoriaModel> itemList, Context context, OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_category,null);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
        pos =position;
    }

    public void setItems(List<CategoriaModel> items) {mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView image;

        ViewHolder(View itemView){
            super(itemView);
            // Aca van todos los view
            title = itemView.findViewById(R.id.catTitle);
            image = itemView.findViewById(R.id.catImage);
        }

        void bindData(final CategoriaModel item){
            // Aca va lo que se hace con los view
            title.setText(item.getTitle());
            //image.setImageResource(item.getImage());
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
