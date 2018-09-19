package com.example.delivery.delivery.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.delivery.delivery.R;

public class ViewHolderProgress extends RecyclerView.ViewHolder {

    private ProgressBar progressBar;

    public ViewHolderProgress(View view) {
        super(view);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    public void onBind() {
        progressBar.setIndeterminate(true);
    }
}