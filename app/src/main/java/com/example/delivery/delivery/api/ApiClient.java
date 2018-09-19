package com.example.delivery.delivery.api;

import com.example.delivery.delivery.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiClient {

    private static final String URL = "https://mock-api-mobile.dev.lalamove.com/";
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;

    private static Retrofit getRetrofitInstance(String baseUrl) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(Constant.RESPONSE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(Constant.RESPONSE_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(Constant.RESPONSE_TIMEOUT, TimeUnit.SECONDS)
                    .build();
        }

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = getRetrofitInstance(URL);
        }
        return retrofit;
    }
}
