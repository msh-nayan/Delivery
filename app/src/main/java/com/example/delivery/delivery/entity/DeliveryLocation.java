package com.example.delivery.delivery.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeliveryLocation implements Serializable {

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    @SerializedName("address")
    private String address;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeliveryLocation :: ")
                .append("lat : " + lat).append(" || ")
                .append("lng : " + lng).append(" || ")
                .append("address : " + address);

        return sb.toString();
    }
}
