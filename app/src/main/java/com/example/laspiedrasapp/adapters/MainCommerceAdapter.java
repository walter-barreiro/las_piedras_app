package com.example.laspiedrasapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.activities.CommerceActivity;
import com.example.laspiedrasapp.models.CommerceModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MainCommerceAdapter extends FirebaseRecyclerAdapter<CommerceModel, MainCommerceAdapter.MainCommerceViewHolder> {
    private Context context;

    public MainCommerceAdapter(@NonNull FirebaseRecyclerOptions<CommerceModel> options, Context context)
    {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public MainCommerceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_main_commerce, parent, false);
        return new MainCommerceAdapter.MainCommerceViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull MainCommerceViewHolder holder, int position, @NonNull CommerceModel model) {
        holder.firstname.setText(model.getName());
        Glide.with(context).load(model.getBanner_url()).into(holder.banner);
        holder.banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ownerId",model.getOwnerId());
                Intent intent = new Intent(holder.banner.getContext(), CommerceActivity.class);
                intent.putExtras(bundle);
                holder.banner.getContext().startActivity(intent);
            }
        });


    }

    class MainCommerceViewHolder extends RecyclerView.ViewHolder {
        TextView firstname;
        ImageView banner;
        public MainCommerceViewHolder(@NonNull View itemView)
        {
            super(itemView);
            firstname = itemView.findViewById(R.id.tvItemMainCommerceName);
            banner = itemView.findViewById(R.id.ivItemMainCommerceBanner);
        }
    }


}
