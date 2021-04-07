package com.example.laspiedrasapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.laspiedrasapp.models.ProfileProductModel;

import java.util.List;


public class ProfileProductAdapter  extends RecyclerView.Adapter<ProfileProductAdapter.ViewHolder> {
    private List<ProfileProductModel> mData;
    private LayoutInflater mInflater;
    private Context context;

    final ProfileProductAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProfileProductModel item);
    }

    public ProfileProductAdapter(List<ProfileProductModel> itemList, Context context, ProfileProductAdapter.OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }


    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ProfileProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_profile_product,null);
        return new ProfileProductAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(final ProfileProductAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ProfileProductModel> items) {mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, price;

        ViewHolder(View itemView){
            super(itemView);
            // Aca van todos los view
            name = itemView.findViewById(R.id.tvName);
            price = itemView.findViewById(R.id.tvItemProductPrice);
        }

        void bindData(final ProfileProductModel item){
            // Aca va lo que se hace con los view
            name.setText(item.getProduct_name());
            price.setText(item.getProduct_price());


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


}
