package com.example.delivery.delivery;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.Toast;

public class Utility {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void launchClass(Context context, Class<?> cla) {
        Intent intent = new Intent(context, cla);
        context.startActivity(intent);
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isAvailable() && netInfo.isConnectedOrConnecting();
    }

    public static void loadImage(Context context, ImageView iv, String url) {
        GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable.ic_shipping)
                .error(R.drawable.ic_empty)
                .into(iv);
    }

}
