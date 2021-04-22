package com.example.laspiedrasapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laspiedrasapp.R;
import com.example.laspiedrasapp.models.CommerceModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MainCommerceAdapter extends FirestoreRecyclerAdapter<CommerceModel, MainCommerceAdapter.MainCommerceViewHolder> {
    private Context context;

    public MainCommerceAdapter(@NonNull FirestoreRecyclerOptions<CommerceModel> options, Context context)
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
