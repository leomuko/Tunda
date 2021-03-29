package com.example.tunda.helpers;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tunda.R;
import com.example.tunda.models.TechnicianModel;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;

import de.hdodenhof.circleimageview.CircleImageView;

public class technicianItem extends com.xwray.groupie.Item{
    private TechnicianModel mTechnicianModel;
    Context mContext;

    public technicianItem(TechnicianModel technicianModel, Context context){
        mTechnicianModel = technicianModel;
        mContext = context;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView name = viewHolder.itemView.findViewById(R.id.technicianItem_Name);
        TextView role = viewHolder.itemView.findViewById(R.id.technicianItem_role);
        CircleImageView imageView = viewHolder.itemView.findViewById(R.id.homeItem_itemImage);

        name.setText(mTechnicianModel.getTechnicianName());
        role.setText(mTechnicianModel.getTcnRole());
        Picasso.get().load(mTechnicianModel.getTcnCoverPic())
                .into(imageView);
    }

    @Override
    public int getLayout() {
        return R.layout.item_technician;
    }
}
