package com.example.delivery.delivery;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class Popup {
    private Context mContext;
    private DialogClickListener mDialogClickListener;

    public Popup(Context context, DialogClickListener dialogClickListener) {
        mContext = context;
        mDialogClickListener = dialogClickListener;
    }

    public void showDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialogClickListener.onNegativeButtonClick();
            }
        });
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialogClickListener.onPositiveButtonClick();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface DialogClickListener {
        void onNegativeButtonClick();

        void onPositiveButtonClick();
    }
}
