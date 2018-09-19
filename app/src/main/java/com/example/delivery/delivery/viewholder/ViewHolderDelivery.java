package com.example.delivery.delivery.viewholder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.delivery.delivery.CircleImageView;
import com.example.delivery.delivery.R;
import com.example.delivery.delivery.Utility;
import com.example.delivery.delivery.entity.Delivery;
import com.example.delivery.delivery.interfaces.ItemClickListener;

public class ViewHolderDelivery extends RecyclerView.ViewHolder implements View.OnClickListener {

    private String TAG = ViewHolderDelivery.class.getSimpleName();

    private Context mContext;

    private CardView mParent;
    private CircleImageView mCivDelivery;
    private TextView mTvDescription;

    private Delivery mDelivery;

    private ItemClickListener mItemClickListener;

    public ViewHolderDelivery(Context context, View view, ItemClickListener itemClickListener) {
        super(view);
        mContext = context;
        mItemClickListener = itemClickListener;

        mParent = view.findViewById(R.id.item_delivery);
        mCivDelivery = view.findViewById(R.id.image_delivery);
        mTvDescription = view.findViewById(R.id.text_description);

        mParent.setOnClickListener(this);
    }

    public void onBind(Delivery delivery) {
        if (delivery == null) return;

        mDelivery = delivery;

        String message = !TextUtils.isEmpty(mDelivery.getDescription()) ? mDelivery.getDescription() : "";
        mTvDescription.setText(mDelivery.getId() + " || " + message);

        Utility.loadImage(mContext, mCivDelivery, mDelivery.getImageUrl());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_delivery:
                mItemClickListener.onItemClick(mDelivery);
                break;
        }
    }
}
