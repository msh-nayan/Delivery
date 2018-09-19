package com.example.delivery.delivery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class InternetCheckerTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private int mAuthStatus;
    private boolean mIsRunning;

    private OnInternetCheckListener mOnInternetCheckListener;

    public InternetCheckerTask(Context context, OnInternetCheckListener onInternetCheckListener) {
        mContext = context;
        mOnInternetCheckListener = onInternetCheckListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        mIsRunning = true;
        mAuthStatus = isInternetReallyAvailable() ? Constant.CONNECTED : Constant.NOT_CONNECTED;
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if(mOnInternetCheckListener != null) {
            mOnInternetCheckListener.onConnectionListener(mAuthStatus);
        }
        mIsRunning = false;
    }

    // should not be called from ui thread, can be called only from background service
    private boolean isInternetReallyAvailable() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork == null || !activeNetwork.isAvailable() || !activeNetwork.isConnected()) {
            return false;
        } else {
            // TCP/HTTP/DNS (depending on the port, 53=DNS, 80=HTTP, etc.)
            try {
                int timeoutMs = 1500;
                Socket sock = new Socket();
                SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

                sock.connect(sockaddr, timeoutMs);
                sock.close();

                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }

    public boolean ismIsRunning() {
        return mIsRunning;
    }

    public interface OnInternetCheckListener {
        void onConnectionListener(int status);
    }
}
