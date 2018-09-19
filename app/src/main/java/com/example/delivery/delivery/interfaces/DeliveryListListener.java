package com.example.delivery.delivery.interfaces;

import android.content.Context;

import com.example.delivery.delivery.entity.Delivery;

import java.util.List;

public interface DeliveryListListener {
    interface DeliveryListener {
        void onError();

        void onSuccess(List<Delivery> deliveries);
    }

    void getDeliveryList(int offset, int limit, DeliveryListener listener);

    void getDeliveryListOffline(Context context, int offset, int limit, DeliveryListener listener);
}
