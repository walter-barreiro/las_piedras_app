package com.example.laspiedrasapp.adapters;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.recyclerview.widget.RecyclerView;

        import com.example.laspiedrasapp.R;
        import com.example.laspiedrasapp.models.ReviewCommerceModel;

        import java.util.List;


public class ReviewCommerceAdapter  extends RecyclerView.Adapter<ReviewCommerceAdapter.ViewHolder> {
    private List<ReviewCommerceModel> mData;
    private LayoutInflater mInflater;
    private Context context;

    final ReviewCommerceAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ReviewCommerceModel item);
    }

    public ReviewCommerceAdapter(List<ReviewCommerceModel> itemList, Context context, ReviewCommerceAdapter.OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }


    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ReviewCommerceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.item_review_commerce,null);
        return new ReviewCommerceAdapter.ViewHolder(view);
    }

    public void onBindViewHolder(final ReviewCommerceAdapter.ViewHolder holder, final int position){
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ReviewCommerceModel> items) {mData = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView review;

        ViewHolder(View itemView){
            super(itemView);
            // Aca van todos los view
            review = itemView.findViewById(R.id.tvReviewCommerce);
        }

        void bindData(final ReviewCommerceModel item){
            // Aca va lo que se hace con los view
            review.setText(item.getReview());
//            price.setText(item.getProduct_price());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


}
