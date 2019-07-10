package com.example.mike.pedidos.adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mike.pedidos.R;
import com.example.mike.pedidos.items.ItemProduct;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<ItemProduct> mItemList;
    Context context;

    public ProductAdapter(Context context ,List<ItemProduct> mItemList) {
        this.context = context;
        this.mItemList = mItemList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product,parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textDescripcion,textStock,textPrecio;
        ImageView imageFoto;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textDescripcion = itemView.findViewById(R.id.textDescripcion);
            textStock = itemView.findViewById(R.id.textStock);
            textPrecio = itemView.findViewById(R.id.textPrecio);
            imageFoto = itemView.findViewById(R.id.imageFoto);
        }
    }
}
