package com.example.delivery.delivery.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.delivery.delivery.R;

public class ViewHolderEmpty extends RecyclerView.ViewHolder {

    private String TAG = ViewHolderEmpty.class.getSimpleName();

    private ImageView mImageView;
    private TextView mTvTitle;
    private TextView mTvBody;

    private String mFrom;

    public ViewHolderEmpty(View view) {
        super(view);

        mImageView = itemView.findViewById(R.id.image);
        mTvTitle = itemView.findViewById(R.id.text_title);
        mTvBody = itemView.findViewById(R.id.text_message);
    }

    public void onBind(String title, String message) {
        if(!TextUtils.isEmpty(title))
            mTvTitle.setText(title);
        if(!TextUtils.isEmpty(message))
            mTvBody.setText(message);
    }
}