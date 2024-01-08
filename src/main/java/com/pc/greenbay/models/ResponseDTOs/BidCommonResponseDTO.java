package com.pc.greenbay.models.ResponseDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BidCommonResponseDTO {
    private String name;
    private String description;
    private String photoURL;
    @JsonProperty("seller")
    private String sellerUsername;

    public BidCommonResponseDTO() {
    }

    public BidCommonResponseDTO(String name, String description, String photoURL, String sellerUsername) {
        this.name = name;
        this.description = description;
        this.photoURL = photoURL;
        this.sellerUsername = sellerUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }
}
