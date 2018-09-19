package com.example.delivery.delivery.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Delivery implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("description")
    private String description;

    @SerializedName("imageUrl")
    private String imageUrl;

    @SerializedName("location")
    private DeliveryLocation location;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public DeliveryLocation getLocation() {
        return location;
    }

    public void setLocation(DeliveryLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DeliveryLocation :: ")
                .append("id : " + id).append(" || ")
                .append("description : " + description).append(" || ")
                .append("imageUrl : " + imageUrl).append(" || ")
                .append("location : " + location);

        return sb.toString();
    }
}
