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
import com.example.laspiedrasapp.models.BusinessModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MainBusinessAdapter extends FirebaseRecyclerAdapter<BusinessModel, MainBusinessAdapter.MainBusinessViewHolder> {
    private Context context;

    public MainBusinessAdapter(@NonNull FirebaseRecyclerOptions<BusinessModel> options, Context context){
        super(options);
        this.context = context;
    }


    @NonNull
    @Override
    public MainBusinessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_business, parent, false);
        return new MainBusinessAdapter.MainBusinessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainBusinessViewHolder holder, int position, @NonNull BusinessModel model) {
        Glide.with(context).load(model.getImgUrl()).into(holder.imgUrl);
        holder.profession.setText(model.getProfession());
        holder.description.setText(model.getDescription());

    }

    class MainBusinessViewHolder extends RecyclerView.ViewHolder{
        TextView profession;
        TextView description;
        ImageView imgUrl;

        public MainBusinessViewHolder(@NonNull View itemView) {
            super(itemView);
            profession = itemView.findViewById(R.id.tvItemBusinnesProfession);
            description = itemView.findViewById(R.id.tvItemBusinnesDescription);
            imgUrl = itemView.findViewById(R.id.imgItemBusiness);
        }
    }
}
