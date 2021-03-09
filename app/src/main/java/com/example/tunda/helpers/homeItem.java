package com.example.tunda.helpers;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.tunda.R;
import com.example.tunda.models.ProductModel;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;

public class homeItem extends com.xwray.groupie.Item {
    private ProductModel mProductModel;
    Context mContext;

    public homeItem(ProductModel productModel, Context context){
        mProductModel = productModel;
        mContext = context;
    }


    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView itemName = viewHolder.itemView.findViewById(R.id.homeItem_title);
        TextView itemPrice = viewHolder.itemView.findViewById(R.id.homeItem_price);
        ImageView itemImage = viewHolder.itemView.findViewById(R.id.homeItem_itemImage);

        itemName.setText(mProductModel.getPdtName());
        itemPrice.setText( addCommasToNumericString( mProductModel.getPdtPrice()));

        Picasso.get().load(mProductModel.getCoverPic())
                .fit()
                .centerCrop()
                .into(itemImage);
    }

    public String addCommasToNumericString(String digits) {
        String result = "";
        for (int i=1; i <= digits.length(); ++i) {
            char ch = digits.charAt(digits.length() - i);
            if (i % 3 == 1 && i > 1) {
                result = "," + result;
            }
            result = ch + result;
        }

        return result;
    }

    @Override
    public int getLayout() {
        return R.layout.item_home;
    }
}
