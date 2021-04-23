package com.example.tunda.helpers;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tunda.R;
import com.example.tunda.models.AdvisoryModel;
import com.xwray.groupie.GroupieViewHolder;

public class AdvisoryItem extends com.xwray.groupie.Item {
    private AdvisoryModel mAdvisoryModel;
    private Context mContext;

    public AdvisoryItem(AdvisoryModel advisoryModel, Context context){
        mAdvisoryModel = advisoryModel;
        mContext = context;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView title = viewHolder.itemView.findViewById(R.id.advisory_title);
        TextView details = viewHolder.itemView.findViewById(R.id.advisory_txt);

        title.setText(mAdvisoryModel.getAdvisoryTitle());
        details.setText(mAdvisoryModel.getAdvisoryDetails());
    }

    @Override
    public int getLayout() {
        return R.layout.item_advisory;
    }
}
