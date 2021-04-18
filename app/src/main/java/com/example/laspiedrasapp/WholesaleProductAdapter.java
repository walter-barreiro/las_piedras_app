package com.example.laspiedrasapp;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

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
        TextView title, wholesaleprice, unitprice,purchased,minAmount,description, quantity;
        ImageView imageView;
        Button less, plus;

        ViewHolder(View itemView){
            super(itemView);
            // Aca van todos los view
            title = itemView.findViewById(R.id.tvItemWholesaleName);
            wholesaleprice = itemView.findViewById(R.id.tvItemWholesalePrice);
            unitprice = itemView.findViewById(R.id.tvItemUnitPrice);
            purchased = itemView.findViewById(R.id.tvItemWholesalePurchased);
            minAmount = itemView.findViewById(R.id.tvItemWholesaleMinAmount);
            description = itemView.findViewById(R.id.tvItemWholesaleDescription);
            quantity = itemView.findViewById(R.id.tvQuantityWholesale);
            less = itemView.findViewById(R.id.btnLessWholesale);
            plus = itemView.findViewById(R.id.btnPlusWholesale);

            imageView = itemView.findViewById(R.id.ivItemWholesaleImage);
        }

        void bindData(final WholesaleProductModel item){
            // Aca va lo que se hace con los view
            final int[] quantityNum = {1};
            quantity.setText(String.valueOf(quantityNum[0]));

            title.setText(item.getTitle());
            if(item.getWholesalePrice()!=null){
                wholesaleprice.setText("Precio al por mayor: $"+item.getWholesalePrice());
            }
            unitprice.setText("Precio por unidad: $"+item.getUnitPrice());
            purchased.setText("Cantidad de productos comprados: "+item.getPurchased());
            minAmount.setText("Cantidad minima: "+item.getMinimumAmount());
            description.setText(item.getDescription());

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantityNum[0]++;
                    quantity.setText(String.valueOf(quantityNum[0]));
                }
            });

            less.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(quantityNum[0]>1){
                        quantityNum[0]--;
                        quantity.setText(String.valueOf(quantityNum[0]));
                    }

                }
            });


            Glide.with(context).load(item.getImgUrl()).into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


}
