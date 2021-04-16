package com.example.laspiedrasapp;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.example.laspiedrasapp.models.WholesaleProductModel;

        import java.util.List;

public class WholesaleProductAdapter extends RecyclerView.Adapter<WholesaleProductAdapter.ViewHolder> {
    private List<WholesaleProductModel> mData;
    private LayoutInflater mInflater;
    private Context context;

    final WholesaleProductAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(WholesaleProductModel item);
    }

    public WholesaleProductAdapter(List<WholesaleProductModel> itemList, Context context, WholesaleProductAdapter.OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }


    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public WholesaleProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_wholesale_product,null);
        return new WholesaleProductAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(final WholesaleProductAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<WholesaleProductModel> items) {mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;

        ViewHolder(View itemView){
            super(itemView);
            // Aca van todos los view
            title = itemView.findViewById(R.id.rvWholesalePrductTitle);
        }

        void bindData(final WholesaleProductModel item){
            // Aca va lo que se hace con los view
            title.setText(item.getTitle());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


}
