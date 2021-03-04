package com.example.tunda.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tunda.R;

import java.util.List;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.MyViewHolder> {

    private ProductImageListener mProductImageListener;
    private List<Bitmap> dataList;
    Context mContext;

    public ProductImagesAdapter(Context context, List<Bitmap> dataList, ProductImageListener productImageListener){
        this.mContext = context;
        this.dataList = dataList;
        mProductImageListener = productImageListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_training_images, parent, false);
        return new MyViewHolder(itemView, mProductImageListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.theImage.setImageBitmap(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    public void removeImageAt(int position){
        dataList.remove(dataList.get(position));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ProductImageListener mProductImageListener;
        ImageView theImage, deleteIcon;

        public MyViewHolder(@NonNull View itemView, ProductImageListener productImageListener) {
            super(itemView);
            mProductImageListener = productImageListener;
            theImage = (ImageView) itemView.findViewById(R.id.theImage);
            deleteIcon = (ImageView) itemView.findViewById(R.id.deleteIcon);

            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mProductImageListener.onDeleteItemClick(getAdapterPosition());
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
    public interface ProductImageListener{
        void onDeleteItemClick(int position);
    }
}
