package com.example.delivery.delivery.api;

import com.example.delivery.delivery.entity.Delivery;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCall {
    @GET("deliveries")
    Call<List<Delivery>> getDeliveries(@Query("offset") int offset,
                                      @Query("limit") int limit);
}
