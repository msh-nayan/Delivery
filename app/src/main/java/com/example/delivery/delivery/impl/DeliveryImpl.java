package com.example.delivery.delivery.impl;

import android.content.Context;
import android.util.Log;

import com.example.delivery.delivery.api.ApiCall;
import com.example.delivery.delivery.api.ApiClient;
import com.example.delivery.delivery.db.DbHelper;
import com.example.delivery.delivery.entity.Delivery;
import com.example.delivery.delivery.interfaces.DeliveryListListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryImpl implements DeliveryListListener {

    private String TAG = DeliveryImpl.class.getSimpleName();

    @Override
    public void getDeliveryList(int offset, int limit, final DeliveryListener listener) {
        ApiCall apiService = ApiClient.getClient().create(ApiCall.class);
        Call<List<Delivery>> call = apiService.getDeliveries(offset, limit);
        Log.e(TAG, "_log : getDeliveryList : url : " + apiService.getDeliveries(offset, limit).request().url().toString());
        call.enqueue(new Callback<List<Delivery>>() {
            @Override
            public void onResponse(Call<List<Delivery>> call, Response<List<Delivery>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onError();
                }
            }

            @Override
            public void onFailure(Call<List<Delivery>> call, Throwable t) {
                listener.onError();
            }
        });
    }

    @Override
    public void getDeliveryListOffline(Context context, int offset, int limit, DeliveryListener listener) {
        DbHelper dbHelper = new DbHelper(context);
        List<Delivery> deliveries = dbHelper.getDeliveries();

        if(deliveries == null)
            listener.onError();
        else listener.onSuccess(deliveries);
    }
}
