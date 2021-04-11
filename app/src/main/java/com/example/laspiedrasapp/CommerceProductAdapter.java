package com.example.laspiedrasapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.laspiedrasapp.models.CommerceProductModel;

import java.util.List;


public class CommerceProductAdapter extends RecyclerView.Adapter<CommerceProductAdapter.ViewHolder> {
    // Modelo: CommerceProductModel
    private List<CommerceProductModel> mData;
    private LayoutInflater mInflater;
    private Context context;

    final CommerceProductAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CommerceProductModel item);
    }

    public CommerceProductAdapter(List<CommerceProductModel> itemList, Context context, CommerceProductAdapter.OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }


    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public CommerceProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_commerce_product,null);
        return new CommerceProductAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(final CommerceProductAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<CommerceProductModel> items) {mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, price;
        ImageView image;

        ViewHolder(View itemView){
            super(itemView);
            // Aca van todos los view
            name = itemView.findViewById(R.id.tvProductCommerceName);
            price = itemView.findViewById(R.id.tvItemProductCommercePrice);
//            image = itemView.findViewById(R.id.ivItemProductCommerce);
        }

        void bindData(final CommerceProductModel item){
            // Aca va lo que se hace con los view
            name.setText(item.getName());
            price.setText(item.getPrice());
//            Glide.with(context).load(item.getProduct_image_url()).into(image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
