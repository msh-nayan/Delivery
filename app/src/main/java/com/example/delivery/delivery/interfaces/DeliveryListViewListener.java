package com.example.delivery.delivery.interfaces;

import com.example.delivery.delivery.entity.Delivery;

import java.util.List;

public interface DeliveryListViewListener extends BaseListener {
    void onDeliveryListError();

    void onDeliveryListSuccess(List<Delivery> deliveries);
}
